package trollhammer;

/**
 * Classe englobante pour la partie Client. C'est un singleton.
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
        //Client.hi.connecter("tefal", "tefal", "localhost");

        //Client.hi.ecrireChat("lol");
        //Client.hi.executer(Action.Deconnecter);

        new CLIClient().interprete();
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
        Logger.log("Client", 0, "Démarrage Client.");

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

        Logger.log("Client", 0, "Client démarré.");
    }

    /* méthodes du Design */

    void chat(String m, String i) {
		Client.hi.affichageChat(m, i);
    }

    void enchere(int prix, String i) {
		setPrixCourant(prix);
		Objet o = Client.objectmanager.getObject(Client.humain.getVente().getFirst());
		Client.client.setPrixCourant(prix);
        Client.client.setNouveauPrix(
                Client.ventemanager.getVenteEnCours().newPrice());
		Client.client.setDernierEncherisseur(i);
		if (this.getMode() == Onglet.HotelDesVentes) {
			Client.hi.affichageEnchere(prix, i);
		}
    }

    void evenement(Evenement e) {
		Client.hi.affichage(e);
		Vente v = Client.humain.getVente();

        if (v != null && e == Evenement.Adjuge) {
            v.removeFirst();
            Client.ventemanager.getVenteEnCours().setPrices();
            // update de la vue vente !
            Client.hi.affichageVente(Client.ventemanager.getVenteEnCours());
        } else if (v != null && e == Evenement.VenteAutomatique) {
            v.setMode(Mode.Automatique);
        }
    }

    void notification(Notification n) {
		Client.hi.message(n);
		
		switch (n) {
            case FinVente:
                Client.humain.setVente(null);
                break;
            case DebutVente:
            case VenteEnCours:
                Vente v = Client.ventemanager.getVenteEnCours();
                System.out.println("v = "+v);
                
                if(v != null) {
                    Client.humain.setVente(v);
                    Client.ventemanager.getVenteEnCours().setPrices();
                    Client.hi.affichageVente(v);
                }
                break;
            default:
            	break;
        }
    }

    void resultatLogin(StatutLogin s) {
		switch(s) {
			case Connecte_Utilisateur:
				Logger.log("ClientEntry", 1, LogType.INF, "[login] reçu réponse : Connecté Utilisateur");
				Client.hi.mainWindow(false);
				Client.hi.voir(Onglet.HotelDesVentes);
				Client.session.setModerateur(false);
				Client.session.setConnecte(true);
				break;
			case Connecte_Moderateur:
				Logger.log("ClientEntry", 1, LogType.INF, "[login] reçu réponse : Connecté Modérateur");
				Client.hi.mainWindow(true);
				Client.hi.voir(Onglet.HotelDesVentes);
				Client.session.setModerateur(true);
				Client.session.setConnecte(true);
				break;
			case Banni:
				Logger.log("ClientEntry", 1, LogType.WRN, "[login] reçu réponse : Banni");
				Client.hi.messageErreur(Erreur.Banni);
				// mod p.r. design : pas de destroy() (impossible en Java),
				// mais on tient quand même à fermer la connexion!
				Client.session.fermer();
				Client.session = null;
				break;
			case Invalide:
				Logger.log("ClientEntry", 1, LogType.WRN, "[login] reçu réponse : Invalide");
				Client.hi.messageErreur(Erreur.Invalide);
				// mod p.r. design : pas de destroy() (impossible en Java),
				// mais on tient quand même à fermer la connexion!
				Client.session.fermer();
				Client.session = null;
				break;
			default:
				break;
		}
    }

    void superviseur(String i) {
		setSuperviseur(i);
		Vente v = Client.humain.getVente();
		v.setMode(Mode.Manuel);
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
 *
 * L'utilité d'une telle classe est très discutable. En effet, il est plus
 * facile de forcer l'ordre les opérations via l'interface !
 */
class ClientFSM {

    // les états possibles du Client
    enum Etat {HV1, HV2, HV3, HV4, HV5, HV6, HV7, HV8, HV9, HV10, HV11,
               HV12, HV13, GU1, GU2, GU3, GU4, A1, L1, L2, L3, V1, V2,
               V3, V4, V5, V6, VA1, VA2, VA3, VA4, VA5, VA6, TR1, TR2,
               PL1, PL2, PL3, PL4, PL5, PL6, PL7, PL8, PL9, PL10, A2};
                                         // A2 rajouté, voir listeObjets()

    private Etat etat; // l'état actuel du Client

    ClientFSM() {
        // démarrage en mode attente de Login...
        this.etat = Etat.L1;
    }

    /** Remet à zéro l'état de la FSM.
     * Utilisé dans les cas d'erreur graves tels l'erreur de déconnexion.
     */
    void reset() {
        Logger.log("ClientFSM", 1, "Remise à zéro FSM");
        this.etat = Etat.L1;
    }

    Etat getEtat() {
        return this.etat;
    }

    /* les fonctions de vérification des requêtes */

    /* venant de HI */

    boolean accepterProposition() {
        //return transition(Etat.VA4, Etat.VA5);
       return transition(Etat.VA3, Etat.VA5);
    }

    boolean ajouterObjetVente() {
       return transition(Etat.PL4, Etat.PL8);
    }

    boolean choisirObjet() {
        /* modification par rapport au Protocol Model :
         * dans la phase 'Vente', choisir un objet revient
         * toujours sur l'état V3, mais le proposer branche sur l'état V5,
         * directement. (je ne peux qu'adapter le non-déterminisme... - jr)
         */
        return transition(Etat.VA3, Etat.VA4)
            || transition(Etat.V3, Etat.V3)
            || transition(Etat.HV4, Etat.HV4);
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
        return transition(Etat.HV4, Etat.HV13);
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

    boolean executer(Action a) {
        // la transition dépend pas mal de si c'est une
        // déconnexion ou une enchère.
        switch(a) {
            case Deconnecter:
            //return changementPhase(Etat.TR2);
                // c'est mieux si c'est inconditionnel.
                // en effet, on peut vouloir se déconnecter
                // de force, même si l'on attend la réponse
                // à une requête (p.ex. gros lag, ou timeout)
                return true;
            case Encherir:
            return transition(Etat.HV4, Etat.HV8);
            default:
            return false;
        }
        /* FAUX ! transition(Etat.TR1, Etat.TR2) */
    }

    boolean executerModo() {
        return transition(Etat.HV4, Etat.HV11);
    }

    boolean kicker() {
        return transition(Etat.HV4, Etat.HV9)
            // modif p.r. Design : permettre le kick
            // lors d'un kick-ban dans GestionUtilisateurs
            || transition(Etat.GU4, Etat.GU4);
    }

    boolean proposerObjet() {
        return transition(Etat.V3, Etat.V5);
    }

    boolean refuserProposition() {
        //return transition(Etat.VA4, Etat.VA5);
        return transition(Etat.VA3, Etat.VA5);
    }
    
    boolean retirerObjetVente() {
        return transition(Etat.PL4, Etat.PL8);
    }

    // le prochain état va fortement dépendre de ce que
    // l'Utilisateur veut voir !
    boolean voir(Onglet quoi) {
        switch(quoi) {
            case Planification:
                return changementPhase(Etat.PL2);
            case Vente:
                return changementPhase(Etat.V2);
            case GestionUtilisateurs:
                return changementPhase(Etat.GU2);
            case Achat:
                return changementPhase(Etat.A1);
            case HotelDesVentes:
                return changementPhase(Etat.HV2);
            case Validation:
                return changementPhase(Etat.VA2);
            default:
                return false;
        }
    }

    /* fonctions de vérification de requête venant de ClientEntry */

    boolean resultatLogin() {
        //return transition(Etat.L2, Etat.L3);
        // modif p.r. Protocol Model : transition directement sur TR1
        // après que le login soit fait
        return transition(Etat.L2, Etat.TR1);
    }
    
    boolean notification() {
        return transition(Etat.TR2, Etat.L1)
            || transition(Etat.HV5, Etat.HV6)
            || transition(Etat.HV9, Etat.HV10)
            || transition(Etat.HV4, Etat.HV4)
            || transition(Etat.HV12, Etat.HV4)
            || true; // INCONDITIONNEL LOL
    }

    boolean evenement() {
        //return transition(Etat.HV11, Etat.HV12);
        //modif p.r. proto model : retour direct de HV11 sur HV4 lors d'événement,
        //sans attendre en HV12 de notification qui n'arrive qu'à adjudication
        //(donc on implémente l'epsilon-transition HV11->HV12...)
        return transition(Etat.HV11, Etat.HV4)
            || true; // INCONDITIONNEL LOL
    }

    boolean enchere() {
        return transition(Etat.HV8, Etat.HV4)
        // modif p.r. proto model : on veut aussi avoir des updates
        // pendant que la vente se passe, et après avoir rentré dans l'onglet.
        // ce sont des boucles : on revient simplement sur l'état qui est
        // en train d'attendre une réponse qui n'a rien à voir avec l'état
        // des participants (genre la réponse à une enchère)
            || transition(Etat.HV4, Etat.HV4)
            || transition(Etat.HV8, Etat.HV8)
            || transition(Etat.HV9, Etat.HV9)
            || transition(Etat.HV11, Etat.HV11)
            || transition(Etat.HV12, Etat.HV12)
            || transition(Etat.HV13, Etat.HV13)
            || true; // INCONDITIONNEL LOL (pour s'inscrire dans le log)
    }

    boolean chat() {
        return transition(Etat.HV13, Etat.HV4)
            || transition(Etat.HV4, Etat.HV4)
            || true; // INCONDITIONNEL LOL
    }

    boolean detailsVente() {
        return transition(Etat.PL5, Etat.PL4)
            || transition(Etat.PL9, Etat.PL10)
            || transition(Etat.PL7, Etat.PL4)
            // modif p.r. Protocol Model : toute la branche
            // HV3-HV5-HV6-HV7 est groupée sur HV4
            || transition(Etat.HV4, Etat.HV4);
    }

    boolean detailsUtilisateur() {
        // gné ? inconnu du proto model...
        return true;
    }

    boolean listeObjets() {
        return transition(Etat.PL3, Etat.PL4)
            || transition(Etat.PL10, Etat.PL4)
            || transition(Etat.VA2, Etat.VA3)
            || transition(Etat.VA5, Etat.VA6)
            || transition(Etat.V2, Etat.V3)
            || transition(Etat.V6, Etat.V3)
            || transition(Etat.A1, Etat.A2);
            // modif. p.r. Protocol Model : le diagramme de la phase
            // 'Achat' transitionne de A1 sur l'état de sortie directement...
            // pas bon...
    }

    boolean listeUtilisateurs() {
        // modif p.r. Design : voir commentaire dans resultatEdition()
        return transition(Etat.GU4, Etat.GU4)
            || transition(Etat.GU2, Etat.GU4);
    }

    boolean listeParticipants() {
        // modif p.r. Protocol Model : toute la branche
        // HV3-HV5-HV6-HV7 est groupée sur HV4
        return transition(Etat.HV2, Etat.HV4)
        // modif p.r. proto model : on veut aussi avoir des updates
        // pendant que la vente se passe, et après avoir rentré dans l'onglet.
        // ce sont des boucles : on revient simplement sur l'état qui est
        // en train d'attendre une réponse qui n'a rien à voir avec l'état
        // des participants (genre la réponse à une enchère)
            || transition(Etat.HV4, Etat.HV4)
            || transition(Etat.HV8, Etat.HV8)
            || transition(Etat.HV9, Etat.HV9)
            || transition(Etat.HV11, Etat.HV11)
            || transition(Etat.HV12, Etat.HV12)
            || transition(Etat.HV13, Etat.HV13);
    }

    boolean listeVentes() {
        return transition(Etat.PL2, Etat.PL3)
            // modif p.r. Design : omis dans l'Analyse...
            || transition(Etat.PL7, Etat.PL4);
    }

    boolean resultatEdition() {
        return transition(Etat.PL8, Etat.PL9)
            || transition(Etat.PL6, Etat.PL7)
            || transition(Etat.VA6, Etat.VA3)
            || transition(Etat.V5, Etat.V6)
            || transition(Etat.GU3, Etat.GU4);
            // modif p.r. Design : on ne peut pas faire un branchement
            // sur le bon état de GestionUtilisateurs sans savoir quelle
            // édition a été faite. Comme on ne peut pas savoir ici,
            // modification : résultatEdition revient toujours sur GU4,
            //                et listeUtilisateurs est permis dans
            //                une transition GU4 -> GU4 en plus de
            //                la précédente, GU2 -> GU4.
            //|| transition(Etat.GU3, Etat.GU2);
    }

    boolean etatParticipant() {
        return transition(Etat.HV10, Etat.HV4)
        // modif p.r. proto model : on veut aussi avoir des updates
        // pendant que la vente se passe, et après avoir rentré dans l'onglet.
        // ce sont des boucles : on revient simplement sur l'état qui est
        // en train d'attendre une réponse qui n'a rien à voir avec l'état
        // des participants (genre la réponse à une enchère)
            || transition(Etat.HV4, Etat.HV4)
            || transition(Etat.HV8, Etat.HV8)
            || transition(Etat.HV9, Etat.HV9)
            || transition(Etat.HV11, Etat.HV11)
            || transition(Etat.HV12, Etat.HV12)
            || transition(Etat.HV13, Etat.HV13);
    }

    boolean superviseur() {
        return transition(Etat.HV4, Etat.HV4);
//            || transition(Etat.HV4, Etat.HV7); retiré cette branche : ne mène
//            à rien ???
    }

    /** Teste si la FSM est dans l'état e1, et effectue e1 = e2 en retournant true
     * si c'est le cas - n'effectue rien et retourne false sinon.
     */

    private boolean transition(Etat e1, Etat e2) {
        if (etat == e1) {
            Logger.log("ClientFSM", 2, "Transition : "+e1+" -> "+e2);
            etat = e2;
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
            case A2:
                Logger.log("ClientFSM", 2, "Transition : "+etat+" -> "+e);
                etat = e; return true;
            default:
                return false;
        }
    }
}
