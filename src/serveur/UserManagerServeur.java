package trollhammer.serveur;
import java.util.Set;
import java.util.HashSet;

import java.net.Socket; // nécessaire à login()...

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
        System.out.println("[login] tentative de login : "+i);
        SessionServeur sess = new SessionServeur(s);
        UtilisateurServeur u = this.getUtilisateur(i);

        if(u != null) {
            System.out.println("[login] Utilisateur "+i+" trouvé");
            // modif p.r. au design : on rattache la Session
            // à l'Utilisateur, sinon ce dernier ne pourra
            // pas s'en servir. doLogin() se charge
            // de détruire la session si le login est incorrect
            u.setSession(sess);
            u.doLogin(mdp);
        } else {
            System.out.println("[login] Utilisateur "+i+" non trouvé");
            sess.resultatLogin(StatutLogin.Invalide);
            // jr : et on suppose que le Garbage Collector
            // passe après que la variable locale sess
            // n'existe plus à la sortie de login()...
        }
    }

    void logout(String sender) {
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
