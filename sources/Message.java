package trollhammer;
import java.util.Set;
import java.util.List;

/* Projet Trollhammer
 * Déclaration des objets-messages
 *
 * Ces objets sont envoyés tels quels commes messages
 * entre Serveur et Client et vice-versa.
 * Cette manière de faire permet la création rapide
 * (syntaxiquement) des messages quand on les expédie,
 * ainsi que la lecture tout aussi rapide de l'autre côté.
 * L'inspiration en vient fortement des 'case classes' de Scala.
 */

abstract class Message implements java.io.Serializable {
}

/* Messages Client -> Serveur */

class MessageClientServeur extends Message {

    String sender;

    MessageClientServeur(String sender) {
        this.sender = sender;
    }

}

class login extends MessageClientServeur {

    String u;
    String motdepasse;

    login(String sender, String u, String motdepasse) {
        super(sender);
        this.u = u;
        this.motdepasse = motdepasse;
    }

}

class logout extends MessageClientServeur {

    logout(String sender) {
        super(sender);
    }

}

class envoyerChat extends MessageClientServeur {
    
    String msg;

    envoyerChat(String sender, String msg) {
        super(sender);
        this.msg = msg;
    }

}

class envoyerCoupdeMASSE extends MessageClientServeur {

    envoyerCoupdeMASSE(String sender) {
        super(sender);
    }

}

class kickerUtilisateur extends MessageClientServeur {

    String u;

    kickerUtilisateur(String sender, String u) {
        super(sender);
        this.u = u;
    }

}

class enchérir extends MessageClientServeur {

    int prix;

    enchérir(String sender, int prix) {
        super(sender);
        this.prix = prix;
    }

}

class envoyerProposition extends MessageClientServeur {

    Objet proposition;

    envoyerProposition(String sender, Objet proposition) {
        super(sender);
        this.proposition = proposition;
    }

}

class validerProposition extends MessageClientServeur {

    int objet;

    validerProposition(String sender, int objet) {
        super(sender);
        this.objet = objet;
    }

}

class invaliderProposition extends MessageClientServeur {

    int objet;

    invaliderProposition(String sender, int objet) {
        super(sender);
        this.objet = objet;
    }

}

class insérerObjetVente extends MessageClientServeur {

    int objet;
    int vente;
    int pos;

    insérerObjetVente(String sender, int objet, int vente, int pos) {
        super(sender);
        this.objet = objet;
        this.vente = vente;
        this.pos = pos;
    }

}

class enleverObjetVente extends MessageClientServeur {

    int objet;
    int vente;

    enleverObjetVente(String sender, int objet, int vente) {
        super(sender);
        this.objet = objet;
        this.vente = vente;
    }

}

class obtenirUtilisateur extends MessageClientServeur {

    String u;
    
    obtenirUtilisateur(String sender, String u) {
        super(sender);
        this.u = u;
    }

}

class utilisateur extends MessageClientServeur {

    Edition e;
    Utilisateur u;

    utilisateur(String sender, Edition e, Utilisateur u) {
        super(sender);
        this.e = e;
        this.u = u;
    }

}

class obtenirListeObjets extends MessageClientServeur {

    Onglet type;

    obtenirListeObjets(String sender, Onglet type) {
        super(sender);
        this.type = type;
    }

}

class obtenirListeUtilisateurs extends MessageClientServeur {

    obtenirListeUtilisateurs(String sender) {
        super(sender);
    }

}

class obtenirListeParticipants extends MessageClientServeur {

    obtenirListeParticipants(String sender) {
        super(sender);
    }

}

class obtenirListeVentes extends MessageClientServeur {

    obtenirListeVentes(String sender) {
        super(sender);
    }

}

class obtenirVente extends MessageClientServeur {
    
    int v;

    obtenirVente(String sender, int v) {
        super(sender);
        this.v = v;
    }

}

class vente extends MessageClientServeur {

    Edition e;
    Vente v;

    vente(String sender, Edition e, Vente v) {
        super(sender);
        this.e = e;
        this.v = v;
    }

}

/* messages Serveur -> Client */

class résultatLogin extends Message {

    StatutLogin s;

    résultatLogin(StatutLogin s) {
        this.s = s;
    }

}

class notification extends Message {

    Notification n;

    notification(Notification n) {
        this.n = n;
    }

}

class événement extends Message {

    Evénement e;

    événement(Evénement e) {
        this.e = e;
    }

}

class enchère extends Message {

    int prix;
    String u;

    enchère(int prix, String u) {
        this.prix = prix;
        this.u = u;
    }

}

class chat extends Message {

    String m;
    String u;

    chat(String m, String u) {
        this.m = m;
        this.u = u;
    }

}

class détailsVente extends Message {

    Vente v;
    List<Objet> o;

    détailsVente(Vente v, List<Objet> o) {
        this.v = v;
        this.o = o;
    }

}

class détailsUtilisateur extends Message {

    Utilisateur u;

    détailsUtilisateur(Utilisateur u) {
        this.u = u;
    }

}

class listeObjets extends Message {

    Onglet type;
    Set<Objet> liste;

    listeObjets(Onglet type, Set<Objet> liste) {
        this.type = type;
        this.liste = liste;
    }

}

class listeUtilisateurs extends Message {

    Set<Utilisateur> liste;

    listeUtilisateurs(Set<Utilisateur> liste) {
        this.liste = liste;
    }

}

class listeParticipants extends Message {

    Set<Participant> liste;

    listeParticipants(Set<Participant> liste) {
        this.liste = liste;
    }

}

class listeVentes extends Message {

    Set<Vente> liste;

    listeVentes(Set<Vente> liste) {
        this.liste = liste;
    }

}

class résultatEdition extends Message {

    StatutEdition s;

    résultatEdition(StatutEdition s) {
        this.s = s;
    }

}

class étatParticipant extends Message {

    Participant p;

    étatParticipant(Participant p) {
        this.p = p;
    }

}

class superviseur extends Message {

    String id;

    superviseur(String id) {
        this.id = id;
    }

}

