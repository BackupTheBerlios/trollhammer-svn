package trollhammer;
import java.net.*;
import java.io.*;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

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

    private synchronized void envoyer(Message m) {
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

    void detailsVente(Vente v, List<Objet> ol) {
		List<Objet> olp = new ArrayList<Objet>();
		for (Objet o : ol) {
			olp.add(new Objet(o.getId(), o.getNom(), o.getDescription(), o.getModerateur(), o.getPrixDeBase(), o.getPrixDeVente(), o.getStatut(), o.getAcheteur(), o.getVendeur(), o.getImage()));
		}
        envoyer(new detailsVente(new Vente(v.getId(), v.getNom(), v.getDescription(), v.getDate(), v.getMode(), v.getSuperviseur(), v.getOIds()),
								 olp));
    }

    void detailsUtilisateur(Utilisateur u) {
		//ls : fixe pour ne pas modifier notre design foireux
		//On a bad trippé sur la maniere de géré les sessions...
        envoyer(new detailsUtilisateur(new Utilisateur(u.getLogin(), u.getNom(), u.getPrenom(), u.getStatut(), u.getMotDePasse())));
    }

    void listeObjets(Onglet type, Set<Objet> ol) {
		Set<Objet> olp = new HashSet<Objet>();
		for (Objet o : ol) {
			olp.add(new Objet(o.getId(), o.getNom(), o.getDescription(), o.getModerateur(), o.getPrixDeBase(), o.getPrixDeVente(), o.getStatut(), o.getAcheteur(), o.getVendeur(), o.getImage()));
		}
        envoyer(new listeObjets(type, olp));
    }

    void listeUtilisateurs(Set<Utilisateur> ul) {
		//ls : fixe pour ne pas modifier notre design foireux
		//On a bad trippé sur la maniere de géré les sessions...
		Set<Utilisateur> ulp = new HashSet<Utilisateur>();
		for (Utilisateur up : ul) {
			if (up instanceof ModerateurServeur) {
				ulp.add(new Moderateur(up.getLogin(), up.getNom(), up.getPrenom(), up.getStatut(), up.getMotDePasse()));
			} else if (up instanceof UtilisateurServeur) {
				ulp.add(new Utilisateur(up.getLogin(), up.getNom(), up.getPrenom(), up.getStatut(), up.getMotDePasse()));
			}
		}
        envoyer(new listeUtilisateurs(ulp));
    }

    void listeParticipants(Set<Participant> pl) {
		//ls : fixe pour ne pas modifier notre design foireux
		//On a bad trippé sur la maniere de géré les sessions...
		Set<Participant> plp = new HashSet<Participant>();
		for (Participant pp : pl) {
			plp.add(new Participant(pp.getLogin(), pp.getNom(), pp.getPrenom(), pp.getStatut()));
		}
        envoyer(new listeParticipants(plp));
    }

    void listeVentes(Set<Vente> l) {
		Set<Vente> vlp = new HashSet<Vente>();
		for (Vente vp : l) {
			vlp.add(new Vente(vp.getId(), vp.getNom(), vp.getDescription(), vp.getDate(), vp.getMode(), vp.getSuperviseur(), vp.getOIds()));
		}
        envoyer(new listeVentes(vlp));
    }

    void resultatEdition(StatutEdition s) {
        envoyer(new resultatEdition(s));
    }

    void etatParticipant(Participant p) {
		//ls : fixe pour ne pas modifier notre design foireux
		//On a bad trippé sur la maniere de géré les sessions...
        envoyer(new etatParticipant(new Participant(p.getLogin(), p.getNom(), p.getPrenom(), p.getStatut())));
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
            // barbarie : s'interrompre une demi-seconde pour laisser le temps
            // au trafic d'en finir. Ca peut paraître fou, mais c'est nécessaire.
            // l'option SO_LINGER des Sockets devrait se charger d'attendre
            // le temps nécessaire à la transmission avant de faire un reset
            // de la connexion, mais ne le fait _PAS_. Donc... à la main,
            // qu'on le fait. Gorito, gorito... 
            Thread.sleep(500);
            oos.flush();
            oos.close();
            s.close();
        } catch (IOException ioe) {
            Logger.log("SessionServeur", 1, LogType.WRN, "[net] Erreur de fermeture de Session : "+ioe.getMessage());
        } catch (InterruptedException ie) {
            Logger.log("SessionServeur", 1, LogType.ERR, "MAIS TANT MIEUX QUE J'ARRIVE PAS A DORMIR ! (trad. : erreur de sleep() dans la fermeture de Session");
        }
    }
}
