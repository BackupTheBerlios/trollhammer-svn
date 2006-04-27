package trollhammer;
import java.net.*;
import java.io.*;
import java.util.Set;
import java.util.List;

class SessionServeur {

    private Socket s;
    private ObjectOutputStream oos;
    private String login;

    SessionServeur(Socket socket) {
        this.s = socket;
        try {
            oos = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("[net] Ne peut pas créer d'ObjectOutputStream pour "+s.getInetAddress()+" : "+ioe.getMessage());
        }
    }

    private void envoyer(Message m) {
        try {
            System.out.println("[net] envoi de "+m);
            oos.writeObject(m);
        } catch (IOException ioe) {
            System.out.println("[net] Incapable d'envoyer le message : "+ioe.getMessage());
        }
    }

    /* méthodes du Design */

    void résultatLogin(StatutLogin s) {
        envoyer(new résultatLogin(s));
    }

    void chat(String m, String i) {
        envoyer(new chat(m, i));
    }

    void notification(Notification n) {
        envoyer(new notification(n));
    }

    void événement(Evénement e) {
        envoyer(new événement(e));
    }

    void enchère(int prix, String i) {
        envoyer(new enchère(prix, i));
    }

    void détailsVente(Vente v, List<Objet> o) {
        envoyer(new détailsVente(v, o));
    }

    void détailsUtilisateur(Utilisateur u) {
        envoyer(new détailsUtilisateur(u));
    }

    void listeObjets(Onglet type, Set<Objet> lo) {
        envoyer(new listeObjets(type, lo));
    }

    void listeUtilisateurs(Set<Utilisateur> ul) {
        envoyer(new listeUtilisateurs(ul));
    }

    void listeParticipants(Set<Participant> pl) {
        envoyer(new listeParticipants(pl));
    }

    void listeVentes(Set<Vente> l) {
        envoyer(new listeVentes(l));
    }

    void résultatEdition(StatutEdition s) {
        envoyer(new résultatEdition(s));
    }

    void étatParticipant(Participant p) {
        envoyer(new étatParticipant(p));
    }

    void superviseur(String i) {
        envoyer(new superviseur(i));
    }

    /* quelques getters/setters */

    Socket getSocket() {
        return this.s;
    }
}
