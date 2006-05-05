package trollhammer;
import java.util.Set;
import java.util.HashSet;

/**
 * Classe Manager pour le Serveur.
 * Sert à regrouper et gérer les Participants du Serveur.
 * La liste des Participants est en réalité tirée de la liste d'Utilisateurs
 * de UserManagerServeur.
 *
 * @author squelette : Julien Ruffin
 */
class ParticipantManagerServeur {

    /* il y a peut-être confusion entre getParticipants() et getConnected() ici
     * getParticipants() renvoie-t-il la liste des Participants connectés,
     * ou tous ?
     */
    Set<Participant> getParticipants() {
        Set<Participant> pl = new HashSet<Participant>();
        Set<UtilisateurServeur> ul = Serveur.usermanager.getUtilisateurs();
        for(UtilisateurServeur u : ul) {
            if(u.getStatut() == StatutLogin.Connecte_Utilisateur
            || u.getStatut() == StatutLogin.Connecte_Moderateur) {
                pl.add((Participant) u.getUtilisateur());
            }
        }
        return pl;
    }

    void obtenirListeParticipants(String sender) {

    }

    Set<Participant> getConnected() {
        return null;
    }

}
