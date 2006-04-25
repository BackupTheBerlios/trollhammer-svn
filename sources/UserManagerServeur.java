package trollhammer;
import java.util.Set;

class UserManagerServeur {

    private Set<UtilisateurServeur> utilisateurs;

    UtilisateurServeur getUtilisateur(String login) {
        for(UtilisateurServeur u : utilisateurs) {
            if(login == u.getLogin()) {
                return u;
            }
        }
        return null;
    }

    void kickerUtilisateur(String i, String sender) {

    }

    void login(String i, String mdp) {

    }

    void logout(String sender) {

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
