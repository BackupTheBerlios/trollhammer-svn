package trollhammer;
import java.util.Set;
import java.util.List;

/**
 * La classe se chargeant d'envoyer un message à tous les clients connectés
 * au serveur. Ses méthodes portent le nom des messages qui peuvent être envoyés 
 * en "broadcast".
 *
 * @author jruffin
 */
class Broadcaster {

	/**
	 * Envoie à tous les clients connectés l'état du participant p.
	 *
	 * @param	p	participant
	 */
    void etatParticipant(Participant p) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.etatParticipant(p);
        }
    }

	/**
	 * Envoie à tous les clients connectés le message de l'utilisateur i.
	 *
	 * @param	m	message
	 * @param	i	identifiant utilisateur
	 */
    void chat(String m, String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.chat(m, i);
        }
    }

	/**
	 * Envoie à tous les clients connectés l'enchère faite par l'utilisateur i.
	 *
	 * @param	prix	prix de l'enchère
	 * @param	i		identifiant utilisateur
	 */
    void enchere(int prix, String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.enchere(prix, i);
        }
    }

	/**
	 * Envoie à tous les clients connectés l'événement e.
	 *
	 * @param	e	événement
	 */
    void evenement(Evenement e) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.evenement(e);
        }
    }

	/**
	 * Envoie à tous les clients connectés la notification n.
	 *
	 * @param	n	notification
	 */
    void notification(Notification n) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.notification(n);
        }
    }

	/**
	 * Envoie à tous les clients connectés la liste des ventes l.
	 *
	 * @param	l	ensemble de ventes
	 */
    void listeVentes(Set<Vente> l) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.listeVentes(l);
        }
    }

	/**
	 * Envoie à tous les clients connectés les détails de la vente v.
	 * NB: la vente seule ne contient que la liste des identifiants de ses obj.
	 *
	 * @param	v	une vente
	 * @param	vo	liste d'objets de cette vente
	 */
    void detailsVente(Vente v, List<Objet> vo) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.detailsVente(v, vo);
        }
    }

	/**
	 * Envoie à tous les clients connectés le fait que l'utilisateur i est un
	 * superviseur.
	 *
	 * @param	i	identifiant utilisateur
	 */
    void superviseur(String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.superviseur(i);
        }
    }
}
