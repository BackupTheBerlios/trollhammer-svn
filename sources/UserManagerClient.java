package trollhammer;
import java.util.Set;

class UserManagerClient {

    private Set<Utilisateur> utilisateurs;

    void détailsUtilisateur(Utilisateur u) {
        for(Utilisateur util : utilisateurs) {
            if(u.getLogin() == util.getLogin()) {
                utilisateurs.remove(util);
            }
        }
        utilisateurs.add(u);
    }

    void listeUtilisateurs(Set<Utilisateur> ul) {
        utilisateurs = ul;
        Onglet m = Client.client.getMode();
        if(m == Onglet.GestionUtilisateurs) {
            Client.hi.affichageListeUtilisateurs(ul);
        }
    }

    /** Cherche un utilisateur par son identifiant et le retourne,
     * ou null si non trouvé.
     */
    Utilisateur getUser(String i) {
        for(Utilisateur u : utilisateurs) {
            if(u.getLogin() == i) {
                return u;
            }
        }
        return null;
    }
}
