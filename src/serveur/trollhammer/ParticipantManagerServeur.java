package trollhammer;
import java.util.Set;
import java.util.HashSet;

/**
 * Classe Manager pour le Serveur.
 * Sert à regrouper et gérer les Participants du Serveur.
 * La liste des Participants est en réalité tirée de la liste d'Utilisateurs
 * de UserManagerServeur.
 *
 * @author Lionel Sambuc
 * @author squelette : Julien Ruffin
 */
class ParticipantManagerServeur {

	/**
	 * <p>Retourne la liste des participants (Utilisateur actuellement connecté).
	 * Réponds au message envoyer par le client pour remplir la liste affichée
	 * dans l'onglet HdV.
	 *
	 * @param sender	IDUtilisateur de l'émetteur de la requête.
	 */
    void obtenirListeParticipants(String sender) {
        UtilisateurServeur s = Serveur.usermanager.getUtilisateur(sender);
		Set<Participant> pl = new HashSet<Participant>();
        Set<UtilisateurServeur> ul = Serveur.usermanager.getUtilisateurs();
        for(UtilisateurServeur u : ul) {
            if(u.getStatut() == StatutLogin.Connecte_Utilisateur
            || u.getStatut() == StatutLogin.Connecte_Moderateur) {
                pl.add(u);
            }
        }
        s.listeParticipants(pl);
	}
	
	/**
	 * <p>Retourne la liste des utilisateurs (sous forme de participant) inscrits.
	 * Réponds au message envoyer par le client pour remplir la liste affichée
	 * dans l'onglet GestionUtilisateurs, on renvoye tous les utilisateurs défini
	 * dans le système, mais sous la forme de participants, afin de ne pas faire
	 * transiter sur le réseau des information sensible inutilement, tel que le MdP.</p>
	 *
	 * @param sender	IDUtilisateur de l'émetteur de la requête.
	 */
    void obtenirListeUtilisateurs(String sender) {
        UtilisateurServeur s = Serveur.usermanager.getUtilisateur(sender);
        Set<Participant> pl = new HashSet<Participant>();
        Set<UtilisateurServeur> ul = Serveur.usermanager.getUtilisateurs();
        for(UtilisateurServeur u : ul) {
			pl.add(u);
        }
        s.listeParticipants(pl);
	}
}
