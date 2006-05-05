package trollhammer;

/**
 * Serveur Trollhammer.
 * Cette classe est un singleton.
 *
 * @author squelette : Julien Ruffin, implémentation : Julien Ruffin
 */
public class Serveur {
    
    /* champs du design */
    // private long date; // remplacé par getDate() !
    private int marteau;
    private int prix_courant;
    private String dernier_encherisseur;

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
        Serveur.demarrer();

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

    /* demarrage du serveur. Il ne peut en y avoir qu'une seule instance.*/
    static void demarrer() {
        if (Serveur.serveur == null) {
            Serveur.serveur = new Serveur();
        }
    }

    /* constructeur du Serveur, à son propre usage uniquement */
    private Serveur() {
        Logger.log("Serveur", 0, "[sys] Démarrage serveur.");
        Serveur.serveurentry = new ServeurEntry();
        Serveur.broadcaster = new Broadcaster();
        Serveur.objectmanager = new ObjectManagerServeur();
        Serveur.usermanager = new UserManagerServeur();
        Serveur.participantmanager = new ParticipantManagerServeur();
        Serveur.ventemanager = new VenteManagerServeur();

        /* le thread démarreur de ventes ! */
        new VenteStarter().start();

        Logger.log("Serveur", 0, "[sys] Serveur démarré, passage en boucle d'attente");
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
                // faire quelque chose de la commande
                // (si ce n'est pas celle pour quitter)
                interpreter(commande);
            } while(!commande.equals("q"));
            
            lr.close();
        } catch (Exception e) {
            Logger.log("Serveur", 0, "[sys] utilisateur mauvais : exception sur stdin : "+e.getMessage());
        }
    }

    private static void interpreter(String commande) {
        // on découpe la commande en tokens. WOOOSH.
        String tokens[] = commande.split("\\s");

        // puis on interprète.
        
        if(tokens[0].equals("nu")) {
            if(tokens.length != 5) {
                System.out.println("nu - créer un nouvel Utilisateur.\nSyntaxe : "+
                        "nu LOGIN NOM PRENOM MOTDEPASSE");
            } else {
                Serveur.usermanager.addUtilisateur(
                        new UtilisateurServeur(tokens[1],
                            tokens[2], tokens[3], tokens[4])
                        );
                System.out.println("Nouveal Utilisateur créé : "+tokens[1]);
            }
        } else if(tokens[0].equals("nm")) {
            if(tokens.length != 5) {
                System.out.println("nm : créer un nouveau Modérateur.\nSyntaxe : "+
                        "nm LOGIN NOM PRENOM MOTDEPASSE");
            } else {
                Serveur.usermanager.addUtilisateur(
                        new UtilisateurServeur(tokens[1],
                            tokens[2], tokens[3], tokens[4])
                        );
                System.out.println("Nouveau Modérateur créé : "+tokens[1]);
            }
        }
    }
    
    /* méthodes du design */

    void envoyerCoupdeMASSE() {

    }

    boolean checkEnchere(int prix, String i) {
        return false;
    }

    void doEnchere(int prix, String i) {

    }

    void encherir(int prix, String i) {

    }

    void envoyerProposition(Objet o, String sender) {

    }

    /* fin méthodes du design */

    long getDate() {
        return System.currentTimeMillis();
    }

}
