package trollhammer;
import java.util.Set;
import java.util.List;
import javax.swing.SwingUtilities;

public class HI {  //doit être public si on veut un main... donc a gicler en temps voulu

    private Window mw;
    private LoginWindow lw;

    /* le constructeur pour démarrer la GUI comme il faut, vé */
    public HI()
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()
        {
            lw = new LoginWindow();
        }
        }); // pask on s'la pète!!!

    }

    /** Afficher la fenêtre principale et cacher la fenêtre de login.
     * En fonction du résultat du login (Utilisateur est Modérateur, ou non)
     */
    void mainWindow(final boolean moderateur) {

			/* jr : ceci est la cause des NullPointerExceptions lors de affichageListeObjets() au Login.
            En effet, l'affichage essaye d'acceder a une variable mw qui ne pointe pas encore sur la bonne Window,
            mais sur null, parce que mettre ca dans un invokeLater() renvoie l'operation a trop tard
            pour que affichageListeObjets() l'utilise.
            
            SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{*/
					mw = new Window(moderateur, lw);
			/*	}
			});*/
        lw.setVisible(false);

    }

    /** Fermer la fenêtre principale et revenir à la fenêtre de Login.
     * Utilisé lors des déconnexions forcées et autres kicks.
     */
    void forcerFermeture() {
        if(mw != null && lw != null) {
            mw.setVisible(false);
            lw.setVisible(true);
        }
    }
			
    /* rajout p.r. Design : afficher quand un utilisateur devient
     * superviseur de vente, pour dire que la vente passe en manuel !
     */

    void superviseur(String i) {
        mw.superviseur(i);
    }

    /* méthodes du Design */

    void accepterProposition(int i) {
        if(Client.fsm.accepterProposition()) {
            Client.session.validerProposition(i);
        }
    }
	//ls : Jule a toi de travailler sur ce truc... j'ai pas la moindre idée de
	// ce que je dois faire pour que cela s'affiche..
    void affichage(Evenement e) {
        // jr : on délègue. ^^
        mw.affichage(e);
    }

    void affichageChat(String m, String i) {
        mw.affichageChat(m, i);
    }

    void affichageEnchere(Integer prix, String i) {
        mw.affichageEnchere(prix, i);
    }

    void affichageObjet(Objet o) {
        this.mw.affichageObjet(o);
    }

    void affichageListeObjets(Set<Objet> ol) {
        this.mw.affichageListeObjets(ol);
    }

    void affichageUtilisateur(Utilisateur i) {
        this.mw.affichageUtilisateur(i);
    }

    void affichageListeUtilisateurs(Set<Utilisateur> ul) {
        this.mw.affichageListeUtilisateurs(ul);
    }

    void affichageListeParticipants(Set<Participant> pl) {
        mw.affichageListeParticipants(pl);
    }

    /* modif p.r. au design : argument change de int i => Vente v, sinon,
     * je ne comprends pas à quoi ça sert ! (jr)
     */
    void affichageVente(Vente v) {
        mw.affichageVente(v);
    }

    /* rajouté du design : oublié pendant la rédaction du DCD !!! (jr) */
    void affichageListeVentes(Set<Vente> vl) {
        mw.affichageListeVentes(vl);
    }

    void ajouterObjetVente(int o, int v, int p) {
        if(Client.fsm.ajouterObjetVente()) {
            Client.session.insererObjetVente(o, v, p);
        }
    }

    void choisirObjet(int i) {
        if(Client.fsm.choisirObjet()) {
            Objet o = Client.objectmanager.getObjet(i);
            if(o != null) {
                this.affichageObjet(o);
            }
        }
    }

    void choisirUtilisateur(String i) {
        if(Client.fsm.choisirUtilisateur()) {
            Utilisateur u = Client.usermanager.getUser(i);
            this.affichageUtilisateur(u);
        }
    }

    void choisirVente(int i) {
        if(Client.fsm.choisirVente()) {
            Client.session.obtenirVente(i);
            Vente v = Client.ventemanager.getVente(i);
            // modifié du Design. Affichage de la vente par objet, pas par ID
            this.affichageVente(v);
        }
    }

    void connecter(String i, String mdp, String s) {
        if(Client.fsm.connecter()) {
            SessionClient sess = SessionClient.login(i, mdp, s);
            if(sess != null) {
                Client.session = sess;
                Logger.log("HI", 1, "[net] Session établie et assignée.");
            } else {
                Logger.log("HI", 0, "[net] Erreur d'ouverture de Session : non trouvé."); 
                Client.fsm.reset(); // on repart à zéro avec la FSM
                this.messageErreur(Erreur.NonTrouve);
            }
        }
    }

    void ecrireChat(String message) {
        if(Client.fsm.ecrireChat()) {
            Client.session.envoyerChat(message);
        }
    }

    void editerUtilisateur(Edition e, Utilisateur u) {
        if(Client.fsm.editerUtilisateur()) {
            Client.session.utilisateur(e, u);
        }
    }

    void editerVente(Edition e, Vente v) {
        if(Client.fsm.editerVente()) {
            Client.session.vente(e, v);
        }
    }

    void executer(Action a) {
        if(Client.fsm.executer(a)) {
            if(a == Action.Deconnecter) {
                Client.session.logout();
            } else if(a == Action.Encherir) {
                boolean en_cours = Client.ventemanager.isInVenteEnCours(Client.client.getDate());
                int np = Client.client.getNouveauPrix();
                if(en_cours) {
                    Client.session.encherir(np);
                }
            }
        }
    }

    void executerModo(ActionModo a) {
        if(Client.fsm.executerModo()) {
            switch(a) {
                case CoupDeMassePAF:
                    Client.session.envoyerCoupdeMASSE(); break;
            }
        }
    }

    void kicker(String i) {
        if(Client.fsm.kicker()) {
            Client.session.kickerUtilisateur(i);
        }
    }

    void message(Notification n) {
        mw.message(n);
    }

    void messageErreur(Erreur e) {
        if(mw != null) {
            mw.messageErreur(e);
        } else {
            lw.messageErreur(e);
        }
    }

    void proposerObjet(Objet o) {
        if(Client.fsm.proposerObjet()) {
            Client.session.envoyerProposition(o);
        }
    }

    void refuserProposition(int i) {
        if(Client.fsm.refuserProposition()) {
            Client.session.invaliderProposition(i);
        }
    }

    void resultatEdition(StatutEdition s) {
        if(Client.fsm.resultatEdition()) {
            switch(s) {
                case NonTrouve:
                    this.messageErreur(Erreur.NonTrouve);
                    break;
                case ExisteDeja:
                    this.messageErreur(Erreur.ExisteDeja);
                    break;
                case DejaEffectue:
                    this.messageErreur(Erreur.DejaEffectue);
                    break;
            }
        }
    }

    void retirerObjetVente(int o, int v) {
        if(Client.fsm.retirerObjetVente()) {
            Client.session.enleverObjetVente(o, v);
        }
    }

    void voir(Onglet quoi) {
		try
		{
        if(Client.fsm.voir(quoi)) {
            // oublié dans le Design, mais si on passe d'un onglet à l'autre,
            // on tient à garder l'état - le champ 'mode' du Client - à jour.
            Client.client.setMode(quoi);
            switch(quoi) {
                case Planification:
                case Validation:
                case GestionUtilisateurs:
                    boolean modo = Client.session.getModerateur();
                    if(modo) {
                        switch(quoi) {
                            case Planification:
                                Client.session.obtenirListeVentes(); // fall-through!!!
                            case Validation:
                                Client.session.obtenirListeObjets(quoi); break;
                            case GestionUtilisateurs:
                                Client.session.obtenirListeUtilisateurs(); break;
                        }
                    }
                    break;
                case Achat:
                case Vente:
                    Client.session.obtenirListeObjets(quoi); break;
                case HotelDesVentes:
                    Client.session.obtenirListeParticipants();
                    Client.session.obtenirProchaineVente();
                    break;
            }
        }
		} catch(Exception e) {Logger.log("HI",0,"ben ça merde.... "+e.getMessage()); e.printStackTrace();}
    }

}
