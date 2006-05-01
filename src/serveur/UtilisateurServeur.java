package trollhammer.serveur;
import trollhammer.commun;
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

    UtilisateurServeur(String login, String nom, String prenom, String motdepasse) {
        this(new Utilisateur(login, nom, prenom, motdepasse));
    }

    Utilisateur getUtilisateur() {
        return this.u;
    }

    void resultatLogin(StatutLogin s) {
        session.resultatLogin(s);
    }

    void chat(String m, String i) {
        session.chat(m, i);
    }

    void notification(Notification n) {
        session.notification(n);
    }

    void evenement(Evenement e) {
        session.evenement(e);
    }

    void enchere(int prix, String i) {
        session.enchere(prix, i);
    }

    void detailsVente(Vente v, List<Objet> o) {
        session.detailsVente(v, o);
    }

    void detailsUtilisateur(Utilisateur u) {
        session.detailsUtilisateur(u);
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

    void resultatEdition(StatutEdition s) {
        session.resultatEdition(s);
    }

    void etatParticipant(Participant p) {
        session.etatParticipant(p);
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

            session.resultatLogin(StatutLogin.Connecte_Utilisateur);
            u.setStatut(StatutLogin.Connecte_Utilisateur);
            Set<Participant> pl = Serveur.participantmanager.getParticipants();
            System.out.println("[login] envoi de la liste des Participants connectés");
            session.listeParticipants(pl);
            System.out.println("[login] broadcast du login");
            Serveur.broadcaster.etatParticipant((Participant) u);
        } else if (!mdp.equals(mot_de_passe)) {
            System.out.println("[login] login Utilisateur refusé, mauvais mot de passe : login "
                    +u.getLogin());
            session.resultatLogin(StatutLogin.Invalide);
            u.setStatut(StatutLogin.Deconnecte);
            this.session = null;
        } else if (statut == StatutLogin.Banni) {
            System.out.println("[login] login d'Utilisateur banni refusé : login "
                    +u.getLogin());
            session.resultatLogin(StatutLogin.Banni);
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
        u.setStatut(StatutLogin.Deconnecte);
        this.session = null;
    }

    /* getters/setters, relaient l'appel à l'Utilisateur */

    String getLogin() {
        return u.getLogin();
    }

    String getNom() {
        return u.getNom();
    }

    String getPrenom() {
        return u.getPrenom();
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

    void setPrenom(String prenom) {
        u.setPrenom(prenom);
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
