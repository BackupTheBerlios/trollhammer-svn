package trollhammer;
import java.util.Set;
import java.util.List;

/**
 * <p>Adaptateur Serveur pour un Utilisateur. Permet la mémorisation de sa 
 * Session associée ainsi que l'envoi de messages à l'Utilisateur, plus quelques
 * opérations spécifiques, notamment le login/logout.</p>
 *
 * @author Julien Ruffin
 */

class UtilisateurServeur extends Utilisateur{

    SessionServeur session;

	// Constructeurs : START
	
    UtilisateurServeur(Utilisateur u) {
        super(u.getLogin(), u.getNom(), u.getPrenom(), u.getMotDePasse());
    }

    UtilisateurServeur(Utilisateur u, SessionServeur s) {
        super(u.getLogin(), u.getNom(), u.getPrenom(), u.getMotDePasse());
        this.session = s;
    }

    UtilisateurServeur(String login, String nom, String prenom, String motdepasse) {
        super(login, nom, prenom, motdepasse);
    }
	// Constructeurs : END

	// Méthodes du design : START
    public void resultatLogin(StatutLogin s) {
        session.resultatLogin(s);
    }

    public void chat(String m, String i) {
        session.chat(m, i);
    }

    public void notification(Notification n) {
        session.notification(n);
    }

    public void evenement(Evenement e) {
        session.evenement(e);
    }

    public void enchere(int prix, String i) {
        session.enchere(prix, i);
    }

    public void detailsVente(Vente v, List<Objet> o) {
        session.detailsVente(v, o);
    }

    public void detailsUtilisateur(Utilisateur u) {
        session.detailsUtilisateur(u);
    }

    public void listeObjets(Onglet type, Set<Objet> lo) {
        session.listeObjets(type, lo);
    }

    public void listeUtilisateurs(Set<Utilisateur> ul) {
        session.listeUtilisateurs(ul);
    }

    public void listeParticipants(Set<Participant> pl) {
        session.listeParticipants(pl);
    }

    public void listeVentes(Set<Vente> l) {
        session.listeVentes(l);
    }

    public void resultatEdition(StatutEdition s) {
        session.resultatEdition(s);
    }

    public void etatParticipant(Participant p) {
        session.etatParticipant(p);
    }

    public void superviseur(String i) {
        session.superviseur(i);
    }

	//Ne serait-ce donc pas à implémenter au niveau de Utilisateur?
    /**
	 * <p>Effectue le login d'un utilisateur. Cette méthode a été généralisée.
	 * Elle gère aussi le cas du Modérateur. Au vu de la complexité relative des
	 * deux, et de leur similitude quasi-parfaite à part les réponses envoyées,
	 * c'est la meilleure manière de faire niveau compactitude.</p>
	 *
	 * @param	sess	SessionServeur, l'objet qui contient la session réseau.
	 * @param	mdp		Le mot de passe du client (sous forme de String).
	 */
    public void doLogin(SessionServeur sess, String mdp) {
        // modif p.r. au design : on rattache la Session
        // à l'Utilisateur, sinon ce dernier ne pourra
        // pas recevoir de messages du Serveur!
        // ici, on le fait uniquement si le login est valide.
        
        // tout ce qui est utile à la généralisation de la méthode est ici.
        StatutLogin reponse_login_correct = StatutLogin.Connecte_Utilisateur;
        String type = "Utilisateur";

        if(this instanceof ModerateurServeur) {
            Logger.log("UtilisateurServeur", 2, LogType.DBG, login + " est un Modérateur.");
            reponse_login_correct = StatutLogin.Connecte_Moderateur;
            type = "Modérateur";
        }

        Logger.log("UtilisateurServeur", 1, LogType.INF, "[login] vérification statut/pass pour " + login);

        if(mdp.equals(mot_de_passe) && statut != StatutLogin.Connecte_Utilisateur
                && statut != StatutLogin.Banni) {
            Logger.log("UtilisateurServeur", 1, LogType.INF, "[login] login " + type + " accepté : login " + login);

            sess.resultatLogin(reponse_login_correct);
            // la session est valide, on la fixe pour l'Utilisateur
            this.session = sess;
            this.setStatut(reponse_login_correct);
            Logger.log("UtilisateurServeur", 2, LogType.INF, "[login] envoi de la liste des Participants connectés");
			//ls : pourquoi refaire le travail, alors qu'il est fait par une opération?
            Serveur.participantmanager.obtenirListeParticipants(login);
            Logger.log("UtilisateurServeur", 2, LogType.INF, "[login] broadcast du login");
            Serveur.broadcaster.etatParticipant(this);
        } else if (!mdp.equals(mot_de_passe)) {
            Logger.log("UtilisateurServeur", 1, LogType.WRN, "[login] login " + type + " refusé, mauvais mot de passe : login " + login);
            sess.resultatLogin(StatutLogin.Invalide);
            this.setStatut(StatutLogin.Deconnecte);
            sess.kaboom();
        } else if (statut == StatutLogin.Banni) {
            Logger.log("UtilisateurServeur", 1, LogType.WRN, "[login] login " + type + " banni refusé : login " + login);
            sess.resultatLogin(StatutLogin.Banni);
            sess.kaboom();
        } else if (statut == reponse_login_correct) {
            // t'es déjà connecté gaillard, va voir ailleurs
            Logger.log("UtilisateurServeur", 1, LogType.WRN, "[login] login refusé pour " + login + " : déjà connecté !");
            sess.resultatLogin(StatutLogin.Deja_Connecte);
            sess.kaboom();
        } else {
            // pas sensé arriver. on ignore...
            Logger.log("UtilisateurServeur", 0, LogType.ERR, "[login] cas non-traité de login " + type + " : login " + login);
            sess.kaboom();
        }

    }

    public void disconnect() {
        Logger.log("UtilisateurServeur", 0, LogType.INF, "[logout] déconnexion : login " + login);
        this.setStatut(StatutLogin.Deconnecte);
        this.session.kaboom();
        this.session = null;
    }
	// Méthodes du design : END
	
	// Setters & Getters : START
    public SessionServeur getSession() {
        return this.session;
    }
	
	public void setSession(SessionServeur sess) {
        this.session = sess;
    }
	// Setters & Getters : END
}
