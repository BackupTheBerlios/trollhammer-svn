package trollhammer;

/** Classe englobante pour la partie Client. C'est un singleton.
*/
class Client {

    /* champs du Design */
    private Onglet mode;
    private int prix_courant;
    private int nouveau_prix;
    private String dernier_enchérisseur; // IDUtilisateur n'est pas définissable comme type
    private long date; // long (temps UNIX en ms), remplace le int par défaut
    private String superviseur;

    /* les champs 'globaux' */
    static Client client;
    static ClientEntry cliententry;
    static SessionClient session;
    static HI hi;
    static ObjectManagerClient objectmanager;
    static UserManagerClient usermanager;
    static ParticipantManagerClient participantmanager;
    static VenteManagerClient ventemanager;
    
    /* la méthode main... */
    public static void main(String[] args) {
        Client.démarrer();
    }

    static void démarrer() {
        if (Client.client == null) {
            Client.client = new Client();
        }
    }

    /* constructeur du Client, réservé à lui seul (via démarrer()) */
    private Client() {
        System.out.println("[sys] Démarrage Client.");

        Client.cliententry = new ClientEntry();
        Client.session = null; // pas créée initialement, seulement au Login
        Client.hi = new HI(); // crée l'interface graphique !
        Client.objectmanager = new ObjectManagerClient();
        Client.usermanager = new UserManagerClient();
        Client.participantmanager = new ParticipantManagerClient();
        Client.ventemanager = new VenteManagerClient();

        System.out.println("[sys] Client démarré.");
        SessionClient sc = SessionClient.login("tefal", "tefal", "localhost");
        sc.envoyerChat("lol");
        sc.logout();
    }

    /* méthodes du Design */

    void chat(String m, String i) {

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

    /* fin des méthodes du Design */

}
