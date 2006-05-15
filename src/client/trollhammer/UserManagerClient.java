package trollhammer;
import java.util.Set;
import java.util.HashSet;

class UserManagerClient {

    private Set<Utilisateur> utilisateurs;

    UserManagerClient() {
        utilisateurs = new HashSet<Utilisateur>();
    }

    void detailsUtilisateur(Utilisateur u) {
        HashSet<Utilisateur> a_enlever = new HashSet<Utilisateur>();

        for(Utilisateur util : utilisateurs) {
            if(u.getLogin().equals(util.getLogin())) {
                a_enlever.add(util);
            }
        }

        for(Utilisateur util : a_enlever) {
            utilisateurs.remove(util);
        }

        utilisateurs.add(u);

		// jr : modif p.r. Design : on aime actualiser la liste pour voir
		// les changements, aussi minimes soient-ils
		Client.hi.affichageListeUtilisateurs(Client.usermanager.getUtilisateurs());
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
            if(u.getLogin().equals(i)) {
                return u;
            }
        }
        return null;
    }

    /** Retourne la liste des Utilisateurs du Manager.
     * Utilisé par HI pour force l'affichage de cette liste
     * lors d'une modification minime d'Utilisateur (qui ne
     * fait pas renvoyer une liste complète du Serveur
     * automatiquement affichée, elle)
     */
    Set<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }
}
