package trollhammer;

class SessionClient {

    /* champs du design */
    private String login;
    private String adresse;
    private boolean connecté;
    private boolean modérateur;

    /* méthodes du design */

    void enchérir(int prix) {

    }

    void enleverObjetVente(int o, int v) {

    }

    void envoyerChat(String message) {

    }
    
    void envoyerCoupDeMASSE() {

    }

    void envoyerProposition(Objet o) {

    }
    
    void insérerObjetVente(int o, int v, int p) {

    }
    
    void invaliderProposition(int i) {

    }
    
    void kickerUtilisateur(String i) {

    }

    /* jr : à vérifier. Que fait exactement cette fonction ? */
    SessionClient login(String i, String m, String s) {
        return null;
    }
    
    void logout() {

    }
    
    void obtenirListeObjets(Onglet quoi) {

    }

    void obtenirListeUtilisateur() {

    }

    void obtenirListeParticipants() {

    }
    
    void obtenirListeVentes() {

    }
    
    void obtenirProchaineVente() {

    }
    
    void obtenirVente(int i) {

    }
    
    void utilisateur(Edition e, Utilisateur u) {

    }
    
    void validerProposition(int i) {

    }
    
    void vente(Edition e, Vente v) {

    }

    /* fin méthodes du design */
}
