package trollhammer;

/** Serveur Trollhammer. Cette classe est un singleton. */
class Serveur {
    
    /* champs du design */
    private long date;
    private String dernier_enchérisseur;
    private Onglet mode;
    private int nouveau_prix;
    private int prix_courant;
    private String superviseur;

    /* champ 'globaux'. Tous doivent se voir avoir une valeur
     * correcte attribuée au démarrage du Serveur. */

    static Serveur serveur = null;
    static ServeurEntry serveurentry;
    static Broadcaster broadcaster;
    static ObjectManagerServeur objectmanager;
    static UserManagerServeur usermanager;
    static ParticipantManagerServeur participantmanager;
    static VenteManagerServeur ventemanager;

    /* la méthode main... */
    public static void main(String[] args) {
        Serveur.démarrer();
    }

    /* démarrage du serveur. Il ne peut en y avoir qu'une seule instance.*/
    static void démarrer() {
        if (Serveur.serveur == null) {
            Serveur.serveur = new Serveur();
        }
    }

    /* constructeur du Serveur, à son propre usage uniquement */
    private Serveur() {
        System.out.println("[sys] Démarrage serveur.");
        Serveur.serveur = this;
        Serveur.serveurentry = new ServeurEntry();
        Serveur.broadcaster = new Broadcaster();
        Serveur.objectmanager = new ObjectManagerServeur();
        Serveur.usermanager = new UserManagerServeur();
        Serveur.participantmanager = new ParticipantManagerServeur();
        Serveur.ventemanager = new VenteManagerServeur();

        System.out.println("[sys] Serveur démarré, passage en boucle d'attente");
        attendre();
    }

    /* jr : attendre stupidement.
     * C'est pour garder les autres threads vivants!
     * Entrer une ligne au clavier quitte le prog.
     */

    private void attendre() {
        try {
            System.in.read();
        } catch (Exception ioe) {
            System.out.println("[sys] utilisateur mauvais : exception sur stdin : "+ioe.getMessage());
        }
    }
    
    /* méthodes du design */

    void envoyerCoupdeMASSE() {

    }

    boolean checkEnchère(int prix, String i) {
        return false;
    }

    void doEnchère(int prix, String i) {

    }

    void enchérir(int prix, String i) {

    }

    void envoyerProposition(Objet o, String sender) {

    }

    /* fin méthodes du design */

}
