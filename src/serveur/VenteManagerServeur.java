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

    void insererObjetVente(int o, int v, int p, String i) {

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

    void obtenirProchaineVente(String s) {

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
