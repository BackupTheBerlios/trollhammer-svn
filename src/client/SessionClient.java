package trollhammer;
import java.io.*;
import java.net.*;

class SessionClient {

    // jr : socket actif sur le port 1777.
    // choix arbitraire, mais 1777 est un
    // nombre premier.
    //
    // Je suis ouvert à tout changement,
    // particulièrement si le nombre
    // est un symbole très obscur.
    public static final int PORT = 1777;

    Socket s;
    ObjectOutputStream oos;

    /* champs du design */
    private String login;
    private String adresse;
    private boolean connecte;
    private boolean moderateur;

    /* méthodes du design */

    /* agit en lieu et place du constructeur. Vérifie l'accessibilité du serveur,
     * retourne la Session y correspondant si la connexion est possible,
     * null sinon.
     */

    static SessionClient login(String i, String m, String s) {
        try {
            Logger.log("SessionClient", 1, "[net] Tentative de connexion sur "+s
                       +", port "+PORT+"...");
            Socket socket = new Socket(s, PORT);
            Logger.log("SessionClient", 1, "[net] Connecté sur le serveur "+s+
                    " ("+socket.getInetAddress()+")");
            return new SessionClient(i, m, s, socket);
        } catch (IOException ioe) {
            Logger.log("SessionClient", 0, "[net] Ne peut pas se connecter à "+s);
            return null;
        }
    }

    /* parlons-en, du constructeur. L'argument 'adresse' se voit ajouté un Socket,
     * et ce dernier doit déjà être ouvert. login() le garantit.
     */
    private SessionClient(String i, String m, String s, Socket socket) {
        this.login = i;
        this.adresse = s;
        this.connecte = false;
        this.moderateur = false;
        this.s = socket;

        // création de l'output stream utilisé par le reste des méthodes
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ioe) {
            Logger.log("SessionClient", 0, "[net] EXCEPTION : ne peut pas créer l'ObjectOutputStream : "+ioe.getMessage());
        }

        // ceci fait, lançons une tentative de login !
        
        Logger.log("SessionClient", 2, "[net] Tentative de login : login "+i+
                " pass "+m);
        envoyer(new login(i, i, m));

        // a toute session correspond le thread 'handler'. Démarrons-le.
        new ClientEntryHandler(socket).start();
    }

    void encherir(int prix) {
        envoyer(new encherir(login, prix));
    }

    void enleverObjetVente(int o, int v) {
        envoyer(new enleverObjetVente(login, o, v));
    }

    void envoyerChat(String message) {
        envoyer(new envoyerChat(login, message));
    }

    void envoyerCoupdeMASSE() {
        envoyer(new envoyerCoupdeMASSE(login));
    }

    void envoyerProposition(Objet o) {
        envoyer(new envoyerProposition(login, o));
    }

    void insererObjetVente(int o, int v, int p) {
        envoyer(new insererObjetVente(login, o, v, p));
    }

    void invaliderProposition(int i) {
        envoyer(new invaliderProposition(login, i));
    }

    void kickerUtilisateur(String i) {
        envoyer(new kickerUtilisateur(login, i));
    }

    void logout() {
        this.connecte = false;
        this.moderateur = false;
        envoyer(new logout(this.login));
        /* PAS BIEN.
        try {
            this.oos.close();
            this.s.close();
            System.out.println("[net] Déconnecté de "+this.adresse);
        } catch (IOException ioe) {
            System.out.println("[net] Erreur pendant la déconnexion : "
                    + ioe.getMessage());
        }*/
    }

    /* ajout p.r. design : déconnexion barbare pour logins loupés */
    void fermer() {
        try {
            this.oos.close();
            this.s.close();
            Logger.log("SessionClient", 1, "[net] Deconnecté de "+this.adresse);
        } catch (IOException ioe) {
            Logger.log("SessionClient", 0, "[net] Erreur pendant la déconnexion : "
                    + ioe.getMessage());
        }
    }

    void obtenirListeObjets(Onglet quoi) {
        envoyer(new obtenirListeObjets(login, quoi));
    }

    void obtenirListeUtilisateurs() {
        envoyer(new obtenirListeUtilisateurs(login));
    }

    void obtenirListeParticipants() {
        envoyer(new obtenirListeParticipants(login));
    }

    void obtenirListeVentes() {
        envoyer(new obtenirListeVentes(login));
    }

    void obtenirProchaineVente() {
        envoyer(new obtenirProchaineVente(login));
    }

    void obtenirVente(int i) {
        envoyer(new obtenirVente(login, i));
    }

    void utilisateur(Edition e, Utilisateur u) {
        envoyer(new MessageUtilisateur(login, e, u));
    }

    void validerProposition(int i) {
        envoyer(new validerProposition(login, i));
    }

    void vente(Edition e, Vente v) {
        envoyer(new MessageVente(login, e, v));
    }

    /* fin méthodes du design */

    private void envoyer(Message m) {
        try {
            Logger.log("SessionClient", 2, "[net] Envoi de la requête : "+m);
            oos.writeObject(m);
        } catch (IOException ioe) {
            Logger.log("SessionClient", 1, "[net] Incapable d'envoyer la requête : "+ioe.getMessage());
        }
    }

    /* getters-setters and BLAH BLAH BLAH */

    String getLogin() {
        return this.login;
    }

    String getAdresse() {
        return this.adresse;
    }

    boolean getConnecte() {
        return this.connecte;
    }

    boolean getModerateur() {
        return this.moderateur;
    }

    void setlogin(String login) {
        this.login = login;
    }

    void setadresse(String adresse) {
        this.adresse = adresse;
    }

    void setConnecte(boolean connecte) {
        this.connecte = connecte;
    }

    void setModerateur(boolean moderateur) {
        this.moderateur = moderateur;
    }


}
