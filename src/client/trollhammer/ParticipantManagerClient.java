package trollhammer;
import java.util.Set;
import java.util.HashSet;

class ParticipantManagerClient {

    private Set<Participant> participants;

    ParticipantManagerClient() {
        participants = new HashSet<Participant>();
    }

    void etatParticipant(Participant p) {
        Set<Participant> a_enlever = new HashSet<Participant>();
/*
        for(Participant pr : participants) {
            if(pr.getLogin().equals(p.getLogin())) {
                a_enlever.add(pr);
            }
        }

        for(Participant doublon : a_enlever) {
            participants.remove(doublon);
        }*/
		// Et pourquoi pas comme ca?? si ca te conviens, efface le commentaire.
        for(Participant pr : participants) {
            if(pr.getLogin().equals(p.getLogin())) {
				participants.remove(pr);
             }
        }
		participants.add(p);
    }

    void listeParticipants(Set<Participant> pl) {
        participants = pl;
        if(Client.client.getMode() == Onglet.HotelDesVentes) {
            Client.hi.affichageListeParticipants(pl);
        }
    }
}
