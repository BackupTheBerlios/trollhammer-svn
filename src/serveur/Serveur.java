package trollhammer;

/** Serveur Trollhammer. Cette classe est un singleton. */
public class Serveur {
    
    /* champs du design */
    // private long date; // remplacé par getDate() !
    private int marteau;
    private int prix_courant;
    private String dernier_enchérisseur;

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

        // CECI EST UN TEST
        
        Serveur.usermanager.addUtilisateur(
                new UtilisateurServeur("tefal", "", "", "tefal")
                );

        Serveur.usermanager.addUtilisateur(
                new UtilisateurServeur("falte", "", "", "falte")
                );
        
        // FIN DU TEST
        
        attendre();
        /* attente finie => tout quitter, en forçant la main même au threads
         * qui attendent ad eternam (ie. le thread Listener)
         */
        System.exit(0);
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
        Serveur.serveurentry = new ServeurEntry();
        Serveur.broadcaster = new Broadcaster();
        Serveur.objectmanager = new ObjectManagerServeur();
        Serveur.usermanager = new UserManagerServeur();
        Serveur.participantmanager = new ParticipantManagerServeur();
        Serveur.ventemanager = new VenteManagerServeur();

        System.out.println("[sys] Serveur démarré, passage en boucle d'attente");
    }

    /* jr : attendre en acceptant, éventuellement, des commandes simples.
     * Permet un peu de contrôle sur les autres threads.
     */

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

    long getDate() {
        return System.currentTimeMillis();
    }

}