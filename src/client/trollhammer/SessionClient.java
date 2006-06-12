package trollhammer;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.KeyStore;

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
        Socket socket; // le socket qui va être créé
        try {

            // magie noire SSL.
            // つくりましょう、 つくりましょう、
            // さて さて なに が できる か な 。。。
            socket = getSSLSocket(s, PORT);
            // でみかした !!!

            Logger.log("SessionClient", 1, LogType.INF, "[net] Tentative de connexion sur " + s + ", port " + PORT + "...");

            Logger.log("SessionClient", 1, LogType.INF, "[net] Connecté sur le serveur " + s + " (" + socket.getInetAddress() + ")");
            return new SessionClient(i, m, s, socket);
        } catch (Exception e) {
            Logger.log("SessionClient", 0, LogType.ERR, "[net] Exception fatale :"
                    +e.getMessage());
            return null;
        }
    }


    /** Méthode de création de Socket SSL, prend en charge la 'magie noire' nécessaire à l'opération. La clé publique utilisée comme certificat est fournie dans un fichier spécifique à trollhammer. Grandement repris d'un exemple SSL de Sun, ClassFileServer.java.
     */
    private static Socket getSSLSocket(String addr, int port) {
            Logger.log("SessionClient", 2, LogType.DBG, "[net] Création Socket SSL.");
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            SSLSocketFactory sf = null;
            Socket s = null;

            try {
                //set up key manager to do server authentication
                SSLContext ctx;
                TrustManagerFactory tmf;
                KeyStore ts;
                char[] passphrase = "trollhammer".toCharArray();

                // le contexte SSL qui contient le certificat,
                // ainsi que les factories pour sortir le gestionnaire
                // de certifs' que l'on va mettre dans le contexte.
                ctx = SSLContext.getInstance("TLS");
                tmf = TrustManagerFactory.getInstance("SunX509");
                ts = KeyStore.getInstance("JKS");

                // on charge la clé le certif du fichier ad hoc, depuis le
                // fichier s'il est présent, sinon depuis le JAR.
                try {
                    ts.load(new FileInputStream("trolltrust"), passphrase);
                } catch (NullPointerException npe) {
                    ts.load(cl.getResourceAsStream("trolltrust"), passphrase);
                }

                // on initialise le gestionnaires qui le représente.
                tmf.init(ts);
                // puis on en initialise le contexte SSL qui servira à
                // créer le Socket SSL qu'on va utiliser !
                ctx.init(null, tmf.getTrustManagers(), null);

                // on crée la Factory avec le contexte correct.
                sf = ctx.getSocketFactory();

                Logger.log("SessionClient", 2, LogType.DBG, "[net] Magie noire SSL faite");
                // puis, enfin, le Socket SSL (ouf) !
                s = sf.createSocket(addr, port); 

            } catch (IOException ioe) {
                Logger.log("SessionClient", 0, LogType.ERR, "[net] Ne peut pas se connecter à " + s);
                return null;
            } catch (Exception e) {
                Logger.log("SessionClient", 0, LogType.ERR, "[net] Erreur de création Socket SSL : "+e.getMessage());
            }

            return s;
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
            Logger.log("SessionClient", 0, LogType.ERR, "[net] EXCEPTION : ne peut pas créer l'ObjectOutputStream : " + ioe.getMessage());
        }

        // ceci fait, lançons une tentative de login !
        
        Logger.log("SessionClient", 1, LogType.INF, "[net] Tentative de login : login " + i + " pass " + m);
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
            Logger.log("SessionClient", 1, LogType.INF, "[net] Deconnecté de " + this.adresse);
        } catch (IOException ioe) {
            Logger.log("SessionClient", 0, LogType.ERR, "[net] Erreur pendant la déconnexion : " + ioe.getMessage());
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

    private synchronized void envoyer(Message m) {
        try {
            Logger.log("SessionClient", 2, LogType.INF, "[net] Envoi de la requête : " + m);
            oos.writeObject(m);
            oos.reset(); // pour ne pas renvoyer deux fois le même objet !!!
        } catch (IOException ioe) {
            Logger.log("SessionClient", 1, LogType.WRN, "[net] Incapable d'envoyer la requête : " + ioe.getMessage());
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
