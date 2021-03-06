/*
 * TrollHammer - Auctions at home over the Internet !
 * Copyright (C) 2006 ZOG Team, contact information on trollhammer.berlios.de
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. *  * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details. *  * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package trollhammer;

import java.util.Set;
import java.util.List;

/**
 * <p>Déclaration des objets-messages qui sont transmis entre le client et le 
 * serveur.</p>
 *
 * <p>Ces objets sont envoyés tels quels commes messages entre Serveur et Client
 * et vice-versa. Cette manière de faire permet la création rapide
 * (syntaxiquement) des messages quand on les expédie, ainsi que la lecture tout
 * aussi rapide de l'autre côté. L'inspiration en vient fortement des 
 * 'case classes' de Scala.</p>
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

class encherir extends MessageClientServeur {

    int prix;

    encherir(String sender, int prix) {
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

class insererObjetVente extends MessageClientServeur {

    int objet;
    int vente;
    int pos;

    insererObjetVente(String sender, int objet, int vente, int pos) {
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

class MessageUtilisateur extends MessageClientServeur {

    Edition e;
    Utilisateur u;

    MessageUtilisateur(String sender, Edition e, Utilisateur u) {
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

class obtenirProchaineVente extends MessageClientServeur {

    obtenirProchaineVente(String sender) {
        super(sender);
    }

}

class MessageVente extends MessageClientServeur {

    Edition e;
    Vente v;

    MessageVente(String sender, Edition e, Vente v) {
        super(sender);
        this.e = e;
        this.v = v;
    }

}

/* messages Serveur -> Client */

class resultatLogin extends Message {

    StatutLogin s;

    resultatLogin(StatutLogin s) {
        this.s = s;
    }

}

class MessageNotification extends Message {

    Notification n;

    MessageNotification(Notification n) {
        this.n = n;
    }

}

class MessageEvenement extends Message {

    Evenement e;

    MessageEvenement(Evenement e) {
        this.e = e;
    }

}

class enchere extends Message {

    int prix;
    String u;

    enchere(int prix, String u) {
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

class detailsVente extends Message {

    Vente v;
    List<Objet> o;

    detailsVente(Vente v, List<Objet> o) {
        this.v = v;
        this.o = o;
    }

}

class detailsUtilisateur extends Message {

    Utilisateur u;

    detailsUtilisateur(Utilisateur u) {
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

class resultatEdition extends Message {

    StatutEdition s;

    resultatEdition(StatutEdition s) {
        this.s = s;
    }

}

class etatParticipant extends Message {

    Participant p;

    etatParticipant(Participant p) {
        this.p = p;
    }

}

class superviseur extends Message {

    String id;

    superviseur(String id) {
        this.id = id;
    }

}

