package trollhammer;

/** Classe englobante pour la partie Client.
*/
public class Client {

    /* champs du Design */
    private Onglet mode;
    private int prix_courant;
    private int nouveau_prix;
    private String dernier_enchérisseur; // IDUtilisateur n'est pas définissable comme type
    private long date; // long (temps UNIX en ms), remplace le int par défaut
    private String superviseur;

    void Chat(String m, String i) {

    }
    
    void enchère(int prix, String i) {

    }

    void événement(Evénement e) {

    }
    
    void notification(Notification n) {

    }

    void résultatLogin(StatutLogin s) {

    }
    
    void superviseur(String i) {

    }

}
