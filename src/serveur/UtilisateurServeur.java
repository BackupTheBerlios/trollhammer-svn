package trollhammer;
import java.util.Set;
import java.util.List;

/** Adaptateur Serveur pour un Utilisateur. Permet la mémorisation de sa Session
 * associée, ainsi que l'envoi de messages à l'Utilisateur,
 * plus quelques opérations spécifiques.
 */

class UtilisateurServeur {

    Utilisateur u;
    SessionServeur session;

    UtilisateurServeur(Utilisateur u) {
        this.u = u;
    }

    UtilisateurServeur(String login, String nom, String prénom, String motdepasse) {
        this(new Utilisateur(login, nom, prénom, motdepasse));
    }

    Utilisateur getUtilisateur() {
        return this.u;
    }

    void résultatLogin(StatutLogin s) {
        session.résultatLogin(s);
    }

    void chat(String m, String i) {
        session.chat(m, i);
    }

    void notification(Notification n) {
        session.notification(n);
    }

    void événement(Evénement e) {
        session.événement(e);
    }

    void enchère(int prix, String i) {
        session.enchère(prix, i);
    }

    void détailsVente(Vente v, List<Objet> o) {
        session.détailsVente(v, o);
    }

    void détailsUtilisateur(Utilisateur u) {
        session.détailsUtilisateur(u);
    }

    void listeObjets(Onglet type, Set<Objet> lo) {
        session.listeObjets(type, lo);
    }

    void listeUtilisateurs(Set<Utilisateur> ul) {
        session.listeUtilisateurs(ul);
    }

    void listeParticipants(Set<Participant> pl) {
        session.listeParticipants(pl);
    }

    void listeVentes(Set<Vente> l) {
        session.listeVentes(l);
    }

    void résultatEdition(StatutEdition s) {
        session.résultatEdition(s);
    }

    void étatParticipant(Participant p) {
        session.étatParticipant(p);
    }

    void superviseur(String i) {
        session.superviseur(i);
    }

    void doLogin(String mdp) {
        System.out.println("[login] vérification statut/pass pour "+u.getLogin());
        String mot_de_passe = u.getMotDePasse();
        StatutLogin statut = u.getStatut();

        /* bug monstrueux : si le même utilisateur essaie de se connecter
         * deux fois, ça va passer! */
        if(mdp.equals(mot_de_passe) && statut != StatutLogin.Banni) {
            System.out.println("[login] login d'Utilisateur accepté : login "
                    +u.getLogin());

            session.résultatLogin(StatutLogin.Connecté_Utilisateur);
            u.setStatut(StatutLogin.Connecté_Utilisateur);
            Set<Participant> pl = Serveur.participantmanager.getParticipants();
            System.out.println("[login] envoi de la liste des Participants connectés");
            session.listeParticipants(pl);
            System.out.println("[login] broadcast du login");
            Serveur.broadcaster.étatParticipant((Participant) u);
        } else if (!mdp.equals(mot_de_passe)) {
            System.out.println("[login] login Utilisateur refusé, mauvais mot de passe : login "
                    +u.getLogin());
            session.résultatLogin(StatutLogin.Invalide);
            u.setStatut(StatutLogin.Déconnecté);
            this.session = null;
        } else if (statut == StatutLogin.Banni) {
            System.out.println("[login] login d'Utilisateur banni refusé : login "
                    +u.getLogin());
            session.résultatLogin(StatutLogin.Banni);
            this.session = null;
        } else {
            // pas sensé arriver. on ignore...
            System.out.println("[login] cas non-traité de login Utilisateur : login "
                    +u.getLogin());
            this.session = null;
        }

    }

    void disconnect() {
        System.out.println("[logout] déconnexion : login "+u.getLogin());
        u.setStatut(StatutLogin.Déconnecté);
        this.session = null;
    }

    /* getters/setters, relaient l'appel à l'Utilisateur */

    String getLogin() {
        return u.getLogin();
    }

    String getNom() {
        return u.getNom();
    }

    String getPrénom() {
        return u.getPrénom();
    }

    StatutLogin getStatut() {
        return u.getStatut();
    }

    SessionServeur getSession() {
        return this.session;
    }

    void setLogin(String login) {
        u.setLogin(login);
    }

    void setNom(String nom) {
        u.setNom(nom);
    }

    void setPrénom(String prénom) {
        u.setPrénom(prénom);
    }

    void setStatut(StatutLogin statut) {
        u.setStatut(statut);
    }

    void setSession(SessionServeur sess) {
        this.session = sess;
    }

    String getMotDePasse() {
        return u.getMotDePasse();
    }

    void setMotDePasse(String mot_de_passe) {
        u.setMotDePasse(mot_de_passe);
    }


}
