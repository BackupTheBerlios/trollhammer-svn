package trollhammer.serveur;
import java.util.Set;
import java.util.List;

class Broadcaster {

    /* méthodes du design */

    void etatParticipant(Participant p) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.etatParticipant(p);
        }
    }

    void chat(String m, String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.chat(m, i);
        }
    }

    void enchere(int prix, String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.enchere(prix, i);
        }
    }

    void evenement(Evenement e) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.evenement(e);
        }
    }

    void notification(Notification n) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.notification(n);
        }
    }

    void listeVentes(Set<Vente> l) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.listeVentes(l);
        }
    }

    void detailsVente(Vente v, List<Objet> vo) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.detailsVente(v, vo);
        }
    }

    void superviseur(String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.superviseur(i);
        }
    }

    /* fin méthodes du design */

}
