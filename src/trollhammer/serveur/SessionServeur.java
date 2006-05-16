package trollhammer.serveur;
import trollhammer.commun.*;
import java.net.*;
import java.io.*;
import java.util.Set;
import java.util.List;

/**
 * Classe d'envoi de messages réseau à un client.
 * Encapsule l'envoi de réponses aux clients.
 * Chaque client connecté, 'incarné' par un Utilisateur ou Modérateur,
 * se voit attribué une SessionServeur.
 *
 * @author Julien Ruffin
 */
class SessionServeur {

    private Socket s;
    private ObjectOutputStream oos;
    private String login;

    SessionServeur(Socket socket) {
        this.s = socket;
        try {
            oos = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException ioe) {
            Logger.log("SessionServeur", 0, LogType.ERR, "[net] Ne peut pas créer d'ObjectOutputStream pour " + s.getInetAddress() + " : " + ioe.getMessage());
        }
    }

    private void envoyer(Message m) {
        try {
            Logger.log("SessionServeur", 2, LogType.DBG, "[net] envoi de " + m);
            oos.writeObject(m);
            oos.reset(); // pour ne pas renvoyer deux fois la meme chose !!!
        } catch (IOException ioe) {
            Logger.log("SessionServeur", 1, LogType.WRN, "[net] Incapable d'envoyer le message : "+ioe.getMessage());
        }
    }

    /* méthodes du Design */

    void resultatLogin(StatutLogin s) {
        envoyer(new resultatLogin(s));
    }

    void chat(String m, String i) {
        envoyer(new chat(m, i));
    }

    void notification(Notification n) {
        envoyer(new MessageNotification(n));
    }

    void evenement(Evenement e) {
        envoyer(new MessageEvenement(e));
    }

    void enchere(int prix, String i) {
        envoyer(new enchere(prix, i));
    }

    void detailsVente(Vente v, List<Objet> o) {
        envoyer(new detailsVente(v, o));
    }

    void detailsUtilisateur(Utilisateur u) {
        envoyer(new detailsUtilisateur(u));
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

    void resultatEdition(StatutEdition s) {
        envoyer(new resultatEdition(s));
    }

    void etatParticipant(Participant p) {
        envoyer(new etatParticipant(p));
    }

    void superviseur(String i) {
        envoyer(new superviseur(i));
    }

    /* quelques getters/setters */

    Socket getSocket() {
        return this.s;
    }

    /* un destructeur pour les logins ratés et déconnexions */

    void kaboom() {
        try {
            oos.close();
            s.close();
        } catch (IOException ioe) {
            Logger.log("SessionServeur", 1, LogType.WRN, "[net] Erreur de fermeture de Session : "+ioe.getMessage());
        }
    }
}