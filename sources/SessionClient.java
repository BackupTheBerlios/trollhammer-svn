package trollhammer;
import java.io.*;
import java.net.*;

class SessionClient {

    Socket s;
    ObjectOutputStream oos;

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
        envoyer(new envoyerChat(login, message));
    }

    void envoyerCoupdeMASSE() {

    }

    void envoyerProposition(Objet o) {

    }

    void insérerObjetVente(int o, int v, int p) {

    }

    void invaliderProposition(int i) {

    }

    void kickerUtilisateur(String i) {

    }

    /* agit en lieu et place du constructeur. Vérifie l'accessibilité du serveur,
     * retourne la Session y correspondant si la connexion est possible,
     * null sinon.
     */

    static SessionClient login(String i, String m, String s) {
        try {
            System.out.println("[net] Tentative de connexion sur "+s
                    +", port 4662...");
            Socket socket = new Socket(s, 4662);
            System.out.println("[net] Connecté sur le serveur "+s+
                    " ("+socket.getInetAddress()+")");
            return new SessionClient(i, m, s, socket);
        } catch (IOException ioe) {
            System.out.println("[net] Ne peut pas se connecter à "+s);
            return null;
        }
    }

    /* parlons-en, du constructeur. L'argument 'adresse' se voit ajouté un Socket,
     * et ce dernier doit déjà être ouvert. login() le garantit.
     */
    private SessionClient(String i, String m, String s, Socket socket) {
        this.login = i;
        this.adresse = s;
        this.connecté = false;
        this.modérateur = false;
        this.s = socket;

        // création de l'output stream utilisé par le reste des méthodes
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("[net] EXCEPTION : ne peut pas créer l'ObjectOutputStream : "+ioe.getMessage());
        }

        // a toute session correspond le thread 'handler'. Démarrons-le.
        new ClientEntryHandler(socket).start();
    }

    void logout() {
        this.connecté = false;
        this.modérateur = false;
        try {
            this.s.close();
            System.out.println("[net] Déconnecté de "+this.adresse);
        } catch (IOException ioe) {
            System.out.println("[net] Erreur pendant la déconnexion : "
                    + ioe.getMessage());
        }
    }

    void obtenirListeObjets(Onglet quoi) {

    }

    void obtenirListeUtilisateurs() {

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

    private void envoyer(Message m) {
        try {
            oos.writeObject(m);
        } catch (IOException ioe) {
            System.out.println("[net] Incapable d'envoyer la requête : "+ioe.getMessage());
        }
    }

    /* getters-setters and BLAH BLAH BLAH */

    String getLogin() {
        return this.login;
    }

    String getAdresse() {
        return this.adresse;
    }

    boolean getConnecté() {
        return this.connecté;
    }

    boolean getModérateur() {
        return this.modérateur;
    }

    void setlogin(String login) {
        this.login = login;
    }

    void setadresse(String adresse) {
        this.adresse = adresse;
    }

    void setConnecté(boolean connecté) {
        this.connecté = connecté;
    }

    void setModérateur(boolean modérateur) {
        this.modérateur = modérateur;
    }


}
