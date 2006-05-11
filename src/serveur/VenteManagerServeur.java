package trollhammer;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Manager pour les Ventes.
 * Regroupe et gère les Ventes du Serveur.
 *
 * @author jruffin
 */
class VenteManagerServeur {

    private Set<VenteServeur> ventes;

    VenteManagerServeur() {
        ventes = new HashSet<VenteServeur>();
    }
	
	//ls : fix tmp, d'ici que chaois mette a jour
	void demarrerVente() {}

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

// spéc étrange: la vente est en cours, on insère un objet en position 0 ...
// donc ça veut dire qu'on vend le 2ème. problème quand l'objet est adjugé, on
// supprime la tête de la liste des objets d'une vente ... donc est-ce qu'on
// ferait pas mieux de bloquer l'ajout/suppression d'objets à une vente en
// cours, tout simplement ? NB: pour l'instant c'est fait comme dans la spéc.
	/**
	 * Suppression d'un objet d'une vente. Les changements sont répercutés chez
	 * le client qui effectue la modification et chez tout le monde si la vente
	 * est en cours. On ne peut pas enlever un objet qui est entrain d'être
	 * vendu. NB: les pre sont checkées au niveau de l'Entry.
	 *
	 * @param	oid		identifiant de l'objet
	 * @param	vid		identifiant de la vente
	 * @param	uid		identifiant utilisateur
	 * @author	cfrey
	 */
    void enleverObjetVente(int oid, int vid, String uid) {
		VenteServeur vte = this.getVente(vid);
// c'est pas la première fois que ça arrive, mais si u est null, et après je
// fais un u.resultatEdition ... problème. alors soit on modifie
// usermanager.getUtilisateur(uid) de telle sorte qu'il ne renvoie pas un null
// si l'uid ne correpond plus à qqch mais plutôt renvoie un truc non null bidon
// ou alors (mais ça requiert pas mal de changements), modifier resultatEdition
// pour qu'elle prenne en paramètre uid et qu'elle gère le cas null (donc ce
// serait plus une méthode de Utilisateur) ...
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(uid);
		
		if (vte != null) {
			// la vente existe
			if (vte.getFirst() == oid) {
				// l'objet n'est pas entrain d'être vendu
				vte.removeOId(oid);
				u.resultatEdition(StatutEdition.Reussi);
			} else {
				// l'objet est entrain d'être vendu
// c'est pas la première fois, mais des résultatsEdition plus variés seraient
// souhaitables ...
				u.resultatEdition(StatutEdition.NonTrouve);
			}
			
			if (getVenteEnCours().getId() == vte.getId()) {
				// vte en cours, changements -> broadcast
				Serveur.broadcaster.detailsVente(vte, vte.getObjets());
			} else {
				// vte pas en cours, changements -> sender
				u.detailsVente(vte, vte.getObjets());
			}
		} else {
			// la vente n'existe pas
			u.resultatEdition(StatutEdition.NonTrouve);
		}
    }

	// ls : A corriger?? : aucune gestion d'erreur...
    void obtenirVente(int v, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		VenteServeur vs = this.getVente(v);
		List<Objet> lo = vs.getObjets();
		u.detailsVente(vs, lo);
    }

    void obtenirListeVentes(String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		//ls : Oui, je dois quand même le faire, il semble que java ne gère pas 
		// aussi bien les règle de sous-typage sur des classes paramétrées que 
		// Scala.
		// NB : c'est quand meme plus simple avec le casting implicite, non?
		// Et surtout ca évite un appel de fonction par itération... 
		Set<Vente> liste = new HashSet<Vente>();
		for(VenteServeur v : ventes) {
			liste.add(v);
		}
		u.listeVentes(liste);
    }

	//ls : modif, le teste concernant le mode et imbriqué dans le if sur la
	// date, plutôt qu'à côté avec le test a double (sur la date). 
	//ls : modif, pour envoyer le message detailsVente, fais appel a la méthode
	// qui le fait, plutot que copier son code...
    // jr : exception si aucune vente en cours : ne rien renvoyer.
    // (sans le fix : NullPointerException pour trouver la prochaine vente inexistante.)
    void obtenirProchaineVente(String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		VenteServeur vs = this.getStarting();
        if(vs != null) {
            this.obtenirVente(vs.getId(), sender);
            if(vs.getDate() < Serveur.serveur.getDate()) {
                u.notification(Notification.VenteEnCours);
                if (vs.getMode() == Mode.Manuel) {
                    u.superviseur(vs.getSuperviseur());
                }
            }
        }
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

	//ls : A VERIFIER : NE DEVRAIT PAS ETRE LA
    void detailsVente(VenteServeur v, List<Objet> ol) {

    }

    /**
	 * Cherche une vente par son identifiant et la retourne,
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

    /**
	 * Retourne la Vente à la date de début la plus proche dans le temps.
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

    /**
	 * Retourne la Vente à la date de début la plus proche dans le temps
     * et dans le passé, null s'il n'y en a pas.
     */
    VenteServeur getVenteEnCours() {
        // on commence par prendre la vente la plus proche dans le temps
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
    
	// ajouter demarrerVente() ...

}
