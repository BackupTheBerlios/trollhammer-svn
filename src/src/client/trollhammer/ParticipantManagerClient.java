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

        for(Participant pr : participants) {
            if(pr.getLogin().equals(p.getLogin())) {
                a_enlever.add(pr);
            }
        }

        for(Participant doublon : a_enlever) {
            participants.remove(doublon);
        }
		// Et pourquoi pas comme ca?? si ca te conviens, efface le commentaire.
        // -> jr : merci de la proposition, mais cette modification a ses raisons.
        //         enlever des Participants de la liste sur laquelle le programme itere
        //         renvoie une Exception, car l'iterateur - implicite avec le foreach -
        //         refuse de continuer a iterer sur une liste modifiee.
        /*for(Participant pr : participants) {
            if(pr.getLogin().equals(p.getLogin())) {
				participants.remove(pr);
             }
        }*/
		participants.add(p);
        // modif p.r. au Design : 'forwarder' le changement a l'interface !
        Client.hi.affichageListeParticipants(this.participants);
    }

    void listeParticipants(Set<Participant> pl) {
        participants = pl;
        if(Client.client.getMode() == Onglet.HotelDesVentes) {
            Client.hi.affichageListeParticipants(pl);
        }
		// modif P.R. Design : passage à HI pour que la liste
		// soit affichée dans l'Hôtel des Ventes.
		// ??? Client.hi.affichageListeParticipants(liste);
    }
}
