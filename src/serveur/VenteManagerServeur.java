package trollhammer;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Manager pour les Ventes.
 * Regroupe et gère les Ventes du Serveur.
 *
 * @author squelette : Julien Ruffin, implémentation : Julien Ruffin
 */
class VenteManagerServeur {

    private Set<VenteServeur> ventes;

    VenteManagerServeur() {
        ventes = new HashSet<VenteServeur>();
    }

	/**
	 * Attribution d'un objet à une vente. Si la position p vaut -1, cela signi-
	 * fie qu'il faut ajouter l'objet à la fin de la liste. Les changements sont
	 * répercutés chez le sender et chez tout le monde si la vente est la pro-
	 * aine. On ne peut pas assigner un objet à une vente en cours.
	 *
	 * @param 	o 	identifiant de l'objet
	 * @param 	v 	identifiant de la vente
	 * @param 	p 	position d'insertion de l'objet dans la liste
	 * @param 	i 	identifiant de l'utilisateur sender
	 * @author 	cfrey
	 */
	void insererObjetVente(int o, int v, int p, String i) {
		VenteServeur vte = getVente(v);
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(i);
		long dateCourante = Serveur.serveur.getDate();
		
		if (vte != null) {
			// la vente existe
			if (vte.getDate() >= dateCourante) {
				// vente courante, prochaine ou à venir ?
				VenteServeur venteEnCours = getVenteEnCours();
				
				if (venteEnCours != null
					&& venteEnCours.getId() == vte.getId()) {
					// vte est la vente en cours, pas de modification possible !
					u.resultatEdition(StatutEdition.NonTrouve);
				} else {
					// pas de vente en cours ou vente en cours mais != vte
					ObjetServeur obj = Serveur.objectmanager.getObjet(o);
					VenteServeur prochaineVente = getStarting(); // != null ...
					
					if (obj != null) {
						// obj existe
						StatutObjet s = obj.getObjet().getStatut();
						
						if (s == StatutObjet.Accepte) {
							// il est important de changer le statut d'abord,
							// puis de faire l'insertion, pour des raisons de
							// concurrence ...
							obj.getObjet().setStatut(StatutObjet.EnVente);
							// /!\ je propose que ce soit Vente.insertObject
							// qui s'occupe des histoires de position ...
							vte.insertObject(o, p, i, dateCourante);
							u.resultatEdition(StatutEdition.Reussi);
						} else {
							// obj est Vendu, Proposé, Refusé ou EnVente
							u.resultatEdition(StatutEdition.NonTrouve);
						}
					} else {
						// obj n'existe pas
						u.resultatEdition(StatutEdition.NonTrouve);
					}
					
					if (prochaineVente.getId() == vte.getId()) {
						// vte est la prochaine, changements -> broadcast
						Serveur.broadcaster.detailsVente(vte, vte.getObjets());
					} else {
						// vte pas la prochaine, changements -> sender
						u.detailsVente(vte, vte.getObjets());
					}
				}
			} else {
				// vente passée, pas de modification possible !
				u.resultatEdition(StatutEdition.NonTrouve);
// ??? pas dans la spéc: renvoyer la liste des ventes ???
			}
		} else {
			// la vente n'existe pas !
			u.resultatEdition(StatutEdition.NonTrouve);
// ??? pas dans la spéc: renvoyer la liste des ventes ???
		}
	}

    void enleverObjetVente(int o, int v, String i) {

    }

    void obtenirVente(int v, String sender) {

    }

    void obtenirListeVentes(String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		//ls : Oui, je dois quand même le faire, il semble que java ne gère pas 
		// aussi bien les règle de sous-typage sur des classes paramétrées que 
		// Scala.
		// NB : c'est quand meme plus simple avec le casting implicite, non?
		Set<Vente> liste = new HashSet<Vente>();
		for(VenteServeur v : ventes) {
			liste.add(v);
		}
		u.listeVentes(liste);
    }

    void obtenirProchaineVente(String sender) {
		
    }

    boolean checkEncherisseur(String i) {
        return false;
    }

    void vente(Edition e, VenteServeur v, String sender) {

    }

    void modoLeaving(String sender) {
        VenteServeur v = this.getVenteEnCours();

        /* si une vente est en cours et que le modo la supervise,
         * prévenir et passer en mode auto. */
        if(v != null && sender == v.getSuperviseur()) {
            v.setSuperviseur(null);
            v.setMode(Mode.Automatique);
            // à vérifier : est-ce que la Vente fait un broadcast de son
            // Mode aux participants présents ? Sinon, il faut penser à
            // le faire ici...
        }
    }

    void detailsVente(VenteServeur v, List<Objet> ol) {

    }

    /** Cherche une vente par son identifiant et la retourne,
     * ou null si non trouvée.
     */
    VenteServeur getVente(int i) {
        for(VenteServeur v : ventes) {
            if(v.getId() == i) {
                return v;
            }
        }
        return null;
    }

    /** Retourne la Vente à la date de début la plus proche dans le temps.
     */
    VenteServeur getStarting() {
        long min = Long.MAX_VALUE;
        long prevmin = 0;
        VenteServeur starting = null;

        /* on prend la vente la plus proche dans le temps */
        for(VenteServeur v : ventes) {
            prevmin = min;
            min = Math.min(min, v.getDate());
            if(min < prevmin) {
                starting = v;
            }
        }

        return starting;
    }

    /** Retourne la Vente à la date de début la plus proche dans le temps
     * et dans le passé, null s'il n'y en a pas.
     */
    VenteServeur getVenteEnCours() {
        /* on commence par prendre la vente la plus proche dans le temps */
        VenteServeur encours = getStarting();

        /* puis on vérifie si elle a démarré ! Si pas,
         * alors on retourne null (ie. aucune vente n'est en cours)
         */
        if(encours != null && encours.getDate() < Serveur.serveur.getDate()) {
            return encours;
        } else {
            return null;
        }

    }

}
