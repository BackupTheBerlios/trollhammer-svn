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

    }

    // modif p.r. au design : passage du Socket en argument,
    // histoire de savoir à quoi la Session se connecte
    void login(Socket s, String i, String mdp) {
        Logger.log("Serveur", 0, "[login] tentative de login : "+i);
        SessionServeur sess = new SessionServeur(s);
        UtilisateurServeur u = this.getUtilisateur(i);

        if(u != null) {
            Logger.log("Serveur", 0, "[login] Utilisateur "+i+" trouvé");
            u.doLogin(sess, mdp);
        } else {
            Logger.log("Serveur", 0, "[login] Utilisateur "+i+" non trouvé");
            sess.resultatLogin(StatutLogin.Invalide);
            // jr : et on suppose que le Garbage Collector
            // passe après que la variable locale sess
            // n'existe plus à la sortie de login()...
        }
    }

    void logout(String sender) {
        Logger.log("UserManagerServeur", 0, "Logout de "+sender);
        UtilisateurServeur u = this.getUtilisateur(sender);
        u.notification(Notification.Deconnexion);
        u.disconnect();
        Serveur.broadcaster.etatParticipant((Participant) u.getUtilisateur());
    }

    void obtenirListeUtilisateurs(String sender) {

    }

    void obtenirUtilisateur(String i, String sender) {

    }

    void utilisateur(Edition e, Utilisateur u, String sender) {

    }

    Set<UtilisateurServeur> getUtilisateurs() {
        return utilisateurs;
    }
}
