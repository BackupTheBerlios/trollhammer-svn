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

    Utilisateur getUtilisateur() {
        return this.u;
    }

    void résultatLogin(StatutLogin s) {

    }

    void notification(Notification n) {

    }

    void événement(Evénement e) {

    }

    void enchère(int prix, String i) {

    }

    void détailsVente(Vente v, List<Objet> o) {

    }

    void détailsUtilisateur(Utilisateur u) {

    }

    void listeObjets(Set<Objet> lo) {

    }

    void listeUtilisateurs(Set<Utilisateur> ul) {

    }

    void listeParticipants(Set<Participant> pl) {

    }

    void listeVentes(Set<Vente> l) {

    }

    void résultatEdition(StatutEdition s) {

    }

    void étatParticipant(Participant p) {

    }

    void superviseur(String i) {

    }

    void doLogin(String mdp) {

    }

}
