package trollhammer;
import java.util.Set;
import java.util.List;

class Broadcaster {

    /* méthodes du design */

    void étatParticipant(Participant p) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.étatParticipant(p);
        }
    }

    void chat(String m, String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.chat(m, i);
        }
    }

    void enchère(int prix, String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.enchère(prix, i);
        }
    }

    void événement(Evénement e) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.événement(e);
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

    void détailsVente(Vente v, List<Objet> vo) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.détailsVente(v, vo);
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
