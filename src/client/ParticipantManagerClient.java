package trollhammer;
import java.util.Set;
import java.util.HashSet;
import java.util.Vector;

class ParticipantManagerClient {

    private Set<Participant> participants;

    ParticipantManagerClient() {
        participants = new HashSet<Participant>();
    }

    void etatParticipant(Participant p) {
        Vector<Participant> a_enlever = new Vector<Participant>();

        for(Participant pr : participants) {
            if(pr.getLogin().equals(p.getLogin())) {
                a_enlever.add(pr);
            }
        }

        for(Participant doublon : a_enlever) {
            participants.remove(doublon);
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
