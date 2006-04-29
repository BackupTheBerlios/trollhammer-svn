package trollhammer;
import java.util.Set;
import java.util.HashSet;

class ParticipantManagerServeur {

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
