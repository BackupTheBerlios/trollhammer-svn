package trollhammer;

/** Classe englobante pour la partie Client. C'est un singleton.
*/
public class Client {

    /* champs du Design */
    private Onglet mode;
    private int prix_courant;
    private int nouveau_prix;
    // IDUtilisateur n'est pas définissable comme type
    private String dernier_enchérisseur;
    // NE DOIT PAS ETRE UTILISE, EST UN PLACEHOLDER DU DESIGN (jr)
    // private long date;
    // long (temps UNIX en ms), remplace le int par défaut
    // remplacé par getDate()...
    private String superviseur;

    /* les champs 'globaux' */
    static Client client;
    static ClientEntry cliententry;
    static SessionClient session;
    static HI hi;
    static Humain humain;
    static ObjectManagerClient objectmanager;
    static UserManagerClient usermanager;
    static ParticipantManagerClient participantmanager;
    static VenteManagerClient ventemanager;

    /* la méthode main... */
    public static void main(String[] args) {
        Client.démarrer();

        // ceci est du test, et du test uniquement.
        Client.hi.connecter("tefal", "tefal", "localhost");

        Client.hi.écrireChat("lol");
        //Client.hi.exécuter(Action.Déconnecter);

        attendre();
        /* attente finie => tout quitter, en forçant la main même au threads
         * qui attendent ad eternam (ie. le thread Listener)
         */
        Client.hi.exécuter(Action.Déconnecter);
        System.exit(0);

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
        Client.humain = new Humain(); // prétentions démiurgiques.
        Client.objectmanager = new ObjectManagerClient();
        Client.usermanager = new UserManagerClient();
        Client.participantmanager = new ParticipantManagerClient();
        Client.ventemanager = new VenteManagerClient();

        System.out.println("[sys] Client démarré.");
    }

    /* jr : attendre en acceptant, éventuellement, des commandes simples.
     *      * Permet un peu de contrôle sur les autres threads.
     *           */

    private static void attendre() {
        try {
            String commande;
            java.io.BufferedReader lr = new java.io.BufferedReader(
                    new java.io.InputStreamReader(System.in));

            do {
                commande = lr.readLine();
            } while(!commande.equals("q"));

            lr.close();
        } catch (Exception e) {
            System.out.println("[sys] utilisateur mauvais : exception sur stdin : "+e.getMessage());
        }
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

    /** donne la date. Remplace le champ Client.date.
    */
    long getDate() {
        return System.currentTimeMillis();
    }

    /* getters, setters and BLAH BLAH BLAH */

    Onglet getMode() {
        return this.mode;
    }

    int getPrixCourant() {
        return this.prix_courant;
    }

    int getNouveauPrix() {
        return this.nouveau_prix;
    }

    void setMode(Onglet mode) {
        this.mode = mode;
    }

    void setPrixCourant(int prix_courant) {
        this.prix_courant = prix_courant;
    }

    void setNouveauPrix(int nouveau_prix) {
        this.nouveau_prix = nouveau_prix;
    }

    String getDernierEnchérisseur() {
        return this.dernier_enchérisseur;
    }

    void setDernierEnchérisseur(String dernier_enchérisseur) {
        this.dernier_enchérisseur = dernier_enchérisseur;
    }

    String getSuperviseur() {
        return this.superviseur;
    }

    void setSuperviseur(String superviseur) {
        this.superviseur = superviseur;
    }

}
