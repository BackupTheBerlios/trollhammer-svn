package trollhammer;

/** Classe englobante pour la partie Client.
*/
public class Client {

    /* champs du Design */
    Onglet mode;
    int prix_courant;
    int nouveau_prix;
    String dernier_enchérisseur; // IDUtilisateur n'est pas définissable comme type
    long date; // long (temps UNIX en ms), remplace le int par défaut
    String superviseur;

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
