package trollhammer;

/** Classe englobante pour la partie Client. C'est un singleton.
*/
public class Client {

    /* champs du Design */
    private Onglet mode;
    private int prix_courant;
    private int nouveau_prix;
    // IDUtilisateur n'est pas définissable comme type
    private String dernier_encherisseur;
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

    /* Champs/types spécifiques à l'implémentation du Protocol Model */

    static ClientFSM fsm;

    /* la méthode main... */
    public static void main(String[] args) {
        Client.demarrer();

        // ceci est du test, et du test uniquement.
        Client.hi.connecter("tefal", "tefal", "localhost");

        Client.hi.ecrireChat("lol");
        //Client.hi.executer(Action.Deconnecter);

        attendre();
        /* attente finie => tout quitter, en forçant la main même au threads
         * qui attendent ad eternam (ie. le thread Listener)
         */
        Client.hi.executer(Action.Deconnecter);
        System.exit(0);

    }

    static void demarrer() {
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

        // spécifique Protocol Model Client
        Client.fsm = new ClientFSM();

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

    void enchere(int prix, String i) {

    }

    void evenement(Evenement e) {

    }

    void notification(Notification n) {

    }

    void resultatLogin(StatutLogin s) {

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

    String getDernierEncherisseur() {
        return this.dernier_encherisseur;
    }

    void setDernierEncherisseur(String dernier_encherisseur) {
        this.dernier_encherisseur = dernier_encherisseur;
    }

    String getSuperviseur() {
        return this.superviseur;
    }

    void setSuperviseur(String superviseur) {
        this.superviseur = superviseur;
    }

}

/** Classe gardant une trace de l'état actuel du Client,
 * et déterminant quelles transitions sont possibles.
 * Toute opération (au sens de l'Analyse) s'y fie pour savoir si
 * elle doit exécuter la requête ou l'ignorer.
 * La vérification se fait par l'appel à une fonction portant le même
 * nom de l'opération, qui retourne true si c'est possible
 * (et exécute la transition dans ses états), false sinon (et n'effectue
 * aucune transition).
 */
class ClientFSM {

    // les états possibles du Client
    enum Etat {HV1, HV2, HV3, HV4, HV5, HV6, HV7, HV8, HV9, HV10, HV11,
               HV12, HV13, GU1, GU2, GU3, GU4, A1, L1, L2, L3, V1, V2,
               V3, V4, V5, V6, VA1, VA2, VA3, VA4, VA5, VA6, TR1, TR2,
               PL1, PL2, PL3, PL4, PL5, PL6, PL7, PL8, PL9, PL10};

    Etat etat; // l'état actuel du Client

    ClientFSM() {
        // démarrage en mode attente de Login...
        this.etat = Etat.L1;
    }

    /* les fonctions de vérification des requêtes */

    /* venant de HI */

    boolean accepterProposition() {
        return transition(Etat.VA4, Etat.VA5);
    }

    boolean ajouterObjetVente() {
        return transition(Etat.PL4, Etat.PL5);
    }

    boolean choisirObjet() {
        /* modification par rapport au Protocol Model :
         * dans la phase 'Vente', choisir un objet revient
         * toujours sur l'état V3, mais le proposer branche sur l'état V5,
         * directement. (je ne peux qu'adapter le non-déterminisme... - jr)
         */
        return transition(Etat.VA3, Etat.VA4)
            || transition(Etat.V3, Etat.V3);
    }

    boolean choisirUtilisateur() {
        return true;
    }

    boolean choisirVente() {
        return transition(Etat.PL4, Etat.PL5);
    }

    boolean connecter() {
        return transition(Etat.L1, Etat.L2);
    }

    boolean ecrireChat() {
        return true;
    }

    boolean editerUtilisateur() {
        return transition(Etat.GU4, Etat.GU3);
        /* code de branchement conditionnel selon l'édition
         * appliquée (voir le non-déterminisme présent à l'état GU3) :
         * inapplicable ici car l'automate n'a pas de
         * mémoire des événements...
        switch(e) {
            case Modifier:
                return transition(Etat.GU3, Etat.GU4);
            default:
                return transition(Etat.GU3, Etat.GU2);
        }*/
    }

    boolean editerVente() {
        return transition(Etat.PL4, Etat.PL6);
    }

    boolean executer() {
        return transition(Etat.TR1, Etat.TR2);
    }

    boolean executerModo() {
        return true;
    }

    boolean kicker() {
        return true;
    }

    boolean proposerObjet() {
        return transition(Etat.V3, Etat.V5);
    }

    boolean refuserProposition() {
        return transition(Etat.VA4, Etat.VA5);
    }
    
    boolean retirerObjetVente() {
        return transition(Etat.PL4, Etat.PL5);
    }

    boolean voir() {
        return changementPhase(Etat.PL2)
            || changementPhase(Etat.V2)
            || changementPhase(Etat.GU2);
    }

    /* fonctions de vérification de requête venant de ClientEntry */

    boolean resultatLogin() {
        return transition(Etat.L2, Etat.L3);
    }
    
    boolean notification() {
        return transition(Etat.TR2, Etat.L1);
    }

    boolean evenement() {
        return true;
    }

    boolean enchere() {
        return true;
    }

    boolean chat() {
        return true;
    }

    boolean detailsVente() {
        return transition(Etat.PL5, Etat.PL4)
            || transition(Etat.PL9, Etat.PL10)
            || transition(Etat.PL7, Etat.PL4);
    }

    boolean detailsUtilisateur() {
        return true;
    }

    boolean listeObjets() {
        return transition(Etat.PL3, Etat.PL4)
            || transition(Etat.PL10, Etat.PL4)
            || transition(Etat.VA2, Etat.VA3)
            || transition(Etat.VA5, Etat.VA6)
            || transition(Etat.V2, Etat.V3)
            || transition(Etat.V6, Etat.V3);
    }

    boolean listeUtilisateurs() {
        return transition(Etat.GU2, Etat.GU4);
    }

    boolean listeParticipants() {
        return true;
    }

    boolean listeVentes() {
        return transition(Etat.PL2, Etat.PL3);
    }

    boolean resultatEdition() {
        return transition(Etat.PL8, Etat.PL9)
            || transition(Etat.PL6, Etat.PL7)
            || transition(Etat.VA6, Etat.VA3)
            || transition(Etat.V5, Etat.V6)
            || transition(Etat.GU3, Etat.GU2);
    }

    boolean etatParticipant() {
        return true;
    }

    boolean superviseur() {
        return true;
    }

    /** Teste si la FSM est dans l'état e1, et effectue e1 = e2 en retournant true
     * si c'est le cas - n'effectue rien et retourne false sinon.
     */

    private boolean transition(Etat e1, Etat e2) {
        if (etat == e1) {
            e1 = e2;
            return true;
        } else return false;
    }

    /** Retourne true si l'état actuel est un état duquel on peut changer de
     * 'phase' (mode d'utilisation), en effectuant la transition vers l'état e,
     * retourne false sans transition sinon.
     * */
    private boolean changementPhase(Etat e) {
        switch(etat) {
            case V3:
            case V4:
            case VA3:
            case VA4:
            case TR1:
            case HV4:
            case PL4:
            case GU4:
            case A1:
                etat = e; return true;
            default:
                return false;
        }
    }
}
