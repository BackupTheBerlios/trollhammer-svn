package trollhammer;
import java.util.Set;
import java.util.HashSet;

import java.net.Socket; // nécessaire à login()...

/**
 * Classe Manager pour les Utilisateurs.
 * Regroupe et gère les Utilisateurs et Modérateurs du Serveur.
 * Pour des raisons d'adaptation, les classes stockées sont respectivement
 * UtilisateurServeur et ModérateurServeur.
 *
 * @author squelette : Julien Ruffin
 */
class UserManagerServeur {

    private Set<UtilisateurServeur> utilisateurs;

    UserManagerServeur() {
        utilisateurs = new HashSet<UtilisateurServeur>();
    }
    
    UtilisateurServeur getUtilisateur(String login) {
        for(UtilisateurServeur u : utilisateurs) {
            if(login.equals(u.getLogin())) {
                return u;
            }
        }
        return null;
    }

    Set<UtilisateurServeur> getConnected() {
        Set<UtilisateurServeur> liste = new HashSet<UtilisateurServeur>();
        for(UtilisateurServeur u : utilisateurs) {
            if(u.getStatut() == StatutLogin.Connecte_Utilisateur
               || u.getStatut() == StatutLogin.Connecte_Moderateur) {
                liste.add(u);
               }
        }
        return liste;
    }

    /* hors-design : utilisé par les threads de ServeurEntry pour identifier
     * la provenance d'un message */
    UtilisateurServeur getUserForSocket(Socket s) {
        for(UtilisateurServeur u : utilisateurs) {
            if(u.getSession() != null && u.getSession().getSocket() == s) {
                return u;
            }
        }
        return null;
    }

    boolean isConnected(String login) {
        UtilisateurServeur u = getUtilisateur(login);
        if(u != null &&
                (u.getStatut() == StatutLogin.Connecte_Utilisateur
                || u.getStatut() == StatutLogin.Connecte_Moderateur)
          ) {
            return true;
        } else {
            return false;
        }
    }

    boolean isModo(String login) {
        UtilisateurServeur u = getUtilisateur(login);
        if(u != null && u instanceof ModerateurServeur &&
                (u.getStatut() == StatutLogin.Connecte_Moderateur)
          ) {
            return true;
        } else {
            return false;
        }
    }

    void addUtilisateur(UtilisateurServeur u) {
        utilisateurs.add(u);
    }

    void removeUtilisateur(UtilisateurServeur u) {
        utilisateurs.remove(u);
    }

    void kickerUtilisateur(String i, String sender) {
		UtilisateurServeur victime = this.getUtilisateur(i);
		victime.notification(Notification.Kicke);
		victime.disconnect();
		Serveur.broadcaster.etatParticipant((Participant) victime.getUtilisateur());
    }

    // modif p.r. au design : passage du Socket en argument,
    // histoire de savoir à quoi la Session se connecte
    void login(Socket s, String i, String mdp) {
        Logger.log("UserManagerServeur", 1, "[login] tentative de login : " + i);
        SessionServeur sess = new SessionServeur(s);
        UtilisateurServeur u = this.getUtilisateur(i);

        if(u != null) {
            Logger.log("UserManagerServeur", 2, "[login] Utilisateur " + i + " trouvé");
            u.doLogin(sess, mdp);
        } else {
            Logger.log("UserManagerServeur", 0, "[login] Utilisateur " + i + " non trouvé");
            sess.resultatLogin(StatutLogin.Invalide);
            // jr : et on suppose que le Garbage Collector
            // passe après que la variable locale sess
            // n'existe plus à la sortie de login()...
			// ls : C'est une évidence, ici tu n'as pas encore quitté
			// le corps de la fonction, par conséquent la variable est toujours
			// en utilisation (référencée) par la fonction, et donc le GC ne 
			// peut virer la variable... (commentaires à effacer au premier 
			// prétexte trouvé (valable ou non))
        }
    }

    void logout(String sender) {
        Logger.log("UserManagerServeur", 1, "[logout] Utilisateur " + sender);
        UtilisateurServeur u = this.getUtilisateur(sender);
        u.notification(Notification.Deconnexion);
        u.disconnect();
        Serveur.broadcaster.etatParticipant((Participant) u.getUtilisateur());
    }

    void obtenirListeUtilisateurs(String sender) {
		UtilisateurServeur u = this.getUtilisateur(sender);
		//ls : C'est une horreur CA, si on avait du sous-typage, je n'aurais pas à le faire...
		Set<Utilisateur> liste = new HashSet<Utilisateur>();
		for (UtilisateurServeur t : utilisateurs) {
			liste.add(t.getUtilisateur());
		}
		u.listeUtilisateurs(liste);
    }

    void obtenirListeParticipants(String sender) {
		UtilisateurServeur u = this.getUtilisateur(sender);
		//ls : C'est une horreur CA, si on avait du sous-typage, je n'aurais pas à le faire...
		// a noter que les relations objet me permettent de passer des Utilisateurs qui se 
		// voient castés en leur sur class Participant...
		Set<Participant> liste = new HashSet<Participant>();
		for (UtilisateurServeur t : utilisateurs) {
			liste.add(t.getUtilisateur());
		}
		u.listeParticipants(liste);
    }

    void obtenirUtilisateur(String i, String sender) {
		UtilisateurServeur s = this.getUtilisateur(sender);
		UtilisateurServeur u = this.getUtilisateur(i);
		s.detailsUtilisateur(u.getUtilisateur());
    }

    void utilisateur(Edition e, Utilisateur u, String sender) {

    }

    Set<UtilisateurServeur> getUtilisateurs() {
        return utilisateurs;
    }
}
