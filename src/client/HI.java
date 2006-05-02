package trollhammer;
import java.util.Set;
import java.util.List;

class HI {

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					new LoginWindow();
				}
			}); // pask on s'la pète!!!
		

	}

    void accepterProposition(int i) {
        Client.session.validerProposition(i);
    }

    void affichage(Evenement e) {

    }

    void affichageChat(String m, String i) {

    }

    void affichageEnchere(Integer prix, String i) {

    }

    void affichageObjet(Objet o) {

    }

    void affichageListeObjets(Set<Objet> ol) {

    }

    void affichageUtilisateur(Utilisateur i) {

    }

    void affichageListeUtilisateurs(Set<Utilisateur> ul) {

    }

    void affichageListeParticipants(Set<Participant> pl) {

    }

    /* modif p.r. au design : argument change de int i => Vente v, sinon,
     * je ne comprends pas à quoi ça sert ! (jr)
     */
    void affichageVente(Vente v) {

    }

    /* rajouté du design : oublié pendant la rédaction du DCD !!! (jr) */
    void affichageListeVentes(Set<Vente> vl) {

    }

    void ajouterObjetVente(int o, int v, int p) {
        Client.session.insererObjetVente(o, v, p);
    }

    void choisirObjet(int i) {
        Objet o = Client.objectmanager.getObjet(i);
        if(o != null) {
            this.affichageObjet(o);
        }
    }

    void choisirUtilisateur(String i) {
        Utilisateur u = Client.usermanager.getUser(i);
        this.affichageUtilisateur(u);
    }

    void choisirVente(int i) {
        Client.session.obtenirVente(i);
        Vente v = Client.ventemanager.getVente(i);
        // modifié du Design. Affichage de la vente par objet, pas par ID
        this.affichageVente(v);
    }

    void connecter(String i, String mdp, String s) {
        SessionClient sess = SessionClient.login(i, mdp, s);
        if(sess != null) {
            Client.session = sess;
            System.out.println("[net] Session établie et assignée.");
        } else {
            System.out.println("[net] Erreur d'ouverture de Session : non trouvé."); 
            this.messageErreur(Erreur.NonTrouve);
        }
    }

    void ecrireChat(String message) {
        Client.session.envoyerChat(message);
    }

    void editerUtilisateur(Edition e, Utilisateur u) {
        Client.session.utilisateur(e, u);
    }

    void editerVente(Edition e, Vente v) {
        Client.session.vente(e, v);
    }

    void executer(Action a) {
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

    void executerModo(ActionModo a) {
        switch(a) {
            case CoupDeMassePAF:
                Client.session.envoyerCoupdeMASSE(); break;
        }
    }

    void kicker(String i) {
        Client.session.kickerUtilisateur(i);
    }

    void message(Notification n) {

    }

    void messageErreur(Erreur e) {

    }

    void proposerObjet(Objet o) {
        Client.session.envoyerProposition(o);
    }

    void refuserProposition(int i) {
        Client.session.invaliderProposition(i);
    }

    void resultatEdition(StatutEdition s) {
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

    void retirerObjetVente(int o, int v) {
        Client.session.enleverObjetVente(o, v);
    }

    void voir(Onglet quoi) {
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
                Client.session.obtenirProchaineVente();
                Client.session.obtenirListeParticipants();
                break;
        }
    }

}
