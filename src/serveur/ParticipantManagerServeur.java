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

    /* jr : il y a peut-être confusion entre getParticipants() et getConnected() ici
     * getParticipants() renvoie-t-il la liste des Participants connectés,
     * ou tous ? Celui-ci renvoie tous, getConnected() seulement les connectés,
     * mais c'est mon interprétation.
     */
    Set<Participant> getParticipants() {
        return utilisateurToParticipant(Serveur.usermanager.getUtilisateurs());
    }

    void obtenirListeParticipants(String sender) {
        Set<Participant> liste = this.getConnected();
        Serveur.usermanager.getUtilisateur(sender).listeParticipants(liste);
    }

    Set<Participant> getConnected() {
        return utilisateurToParticipant(Serveur.usermanager.getConnected());
    }

    // jr : généralisation de la conversion de Set faite par les deux méthodes,
    // getParticipants() et getConnected() (rétrospectivement, oui, c'est moche.)
    Set<Participant> utilisateurToParticipant(Set<UtilisateurServeur> s) {
        Set<Participant> pl = new HashSet<Participant>();
        for(UtilisateurServeur u : s) {
            pl.add((Participant) u.getUtilisateur());
        }
        return pl;
    }
}
