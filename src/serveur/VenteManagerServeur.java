package trollhammer;
import java.util.Set;
import java.util.List;

class VenteManagerServeur {

    private Set<Vente> ventes;

    void insererObjetVente(int o, int v, int p, String i) {

    }

    void enleverObjetVente(int o, int v, String i) {

    }

    void obtenirVente(int v, String sender) {

    }

    void obtenirListeVentes(String sender) {

    }

    void obtenirProchaineVente(String s) {

    }

    boolean checkEncherisseur(String i) {
        return false;
    }

    void vente(Edition e, Vente v, String sender) {

    }

    void modoLeaving(String sender) {
        Vente v = this.getVenteEnCours();
        if(sender == v.getSuperviseur()) {
            v.setSuperviseur(null);
            v.setMode(Mode.Automatique);
            // à vérifier : est-ce que la Vente fait un broadcast de son
            // Mode aux participants présents ? Sinon, il faut penser à
            // le faire ici...
        }
    }

    void detailsVente(Vente v, List<Objet> ol) {

    }

    /** Cherche une vente par son identifiant et la retourne,
     * ou null si non trouvée.
     */
    Vente getVente(int i) {
        for(Vente v : ventes) {
            if(v.getId() == i) {
                return v;
            }
        }
        return null;
    }

    /** Retourne la Vente à la date de début la plus proche dans le temps.
     */
    Vente getStarting() {
        long min = Long.MAX_VALUE;
        long prevmin = 0;
        Vente starting = null;

        /* on prend la vente la plus proche dans le temps */
        for(Vente v : ventes) {
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
    Vente getVenteEnCours() {
        /* on commence par prendre la vente la plus proche dans le temps */
        Vente encours = getStarting();

        /* puis on vérifie si elle a démarré ! Si pas,
         * alors on retourne null (ie. aucune vente n'est en cours)
         */
        if(encours.getDate() < Serveur.serveur.getDate()) {
            return encours;
        } else {
            return null;
        }

    }

}
