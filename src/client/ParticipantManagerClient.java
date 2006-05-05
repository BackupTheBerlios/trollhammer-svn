package trollhammer;
import java.util.Set;
import java.util.HashSet;

class ParticipantManagerClient {

    private Set<Participant> participants;

    ParticipantManagerClient() {
        participants = new HashSet<Participant>();
    }

    void etatParticipant(Participant p) {
        for(Participant pr : participants) {
            if(pr.getLogin() == p.getLogin()) {
                participants.remove(pr);
            }
        }
        participants.add(p);
    }

    void listeParticipants(Set<Participant> pl) {
        participants = pl;
        Onglet m = Client.client.getMode();
        if(m == Onglet.HotelDesVentes) {
            Client.hi.affichageListeParticipants(pl);
        }
    }

}
