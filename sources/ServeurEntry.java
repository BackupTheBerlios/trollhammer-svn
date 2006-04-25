package trollhammer;
import java.io.*;
import java.net.*;

class ServeurEntry {

    /* méthodes du design */

    void login(String u, String motDePasse, String sender) {

    }

    void logout(String sender) {

    }

    void envoyerChat(String msg, String sender) {

    }

    void envoyerCoupdeMASSE(String sender) {

    }

    void kickerUtilisateur(String u, String sender) {

    }

    void enchérir(Integer prix, String sender) {

    }

    void envoyerProposition(Objet proposition, String sender) {

    }

    void validerProposition(int objet, String sender) {

    }

    void invaliderProposition(int objet, String sender) {

    }

    void insérerObjetVente(int objet, int vente, int pos, String sender) {

    }

    void enleverObjetVente(int objet, int vente, String sender) {

    }

    void obtenirUtilisateur(String u, String sender) {

    }

    void utilisateur(Edition e, Utilisateur u, String sender) {

    }

    void obtenirListeObjets(Onglet type, String sender) {

    }

    void obtenirListeUtilisateurs(String sender) {

    }

    void obtenirListeVentes(String sender) {

    }

    void obtenirListeParticipants(String sender) {

    }

    void obtenirVente(int v, String sender) {

    }

    void vente(Edition e, Vente v, String sender) {

    }

    /* fin des méthodes du design */
}

/** Un thread listener qui répond aux commandes d'une connexion particulière.
 * Lancé pour chaque nouvelle connexion établie.
 */
class ServeurEntryThread extends Thread {

    Socket s;

    ServeurEntryThread(Socket socket) {
        this.s = socket;
    }

    /** Boucle de lecture des objets sérialisés reçus du socket.
     */
    public void run() {
        Object o; // l'objet qui va être lu du Socket.

        try {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            do {
                o = ois.readObject();
            } while (!(o instanceof Logout))

