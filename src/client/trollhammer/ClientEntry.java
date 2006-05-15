package trollhammer;
import java.util.Set;
import java.util.List;
import java.io.*;
import java.net.*;

class ClientEntry {

    void resultatLogin(StatutLogin s) {
        if(Client.fsm.resultatLogin()) {
            Client.client.resultatLogin(s);
        }
    }

    void notification(Notification n) {
		if(Client.fsm.notification()) {
            Client.client.notification(n);
        }
    }

    void evenement(Evenement e) {
        if(Client.fsm.evenement()) {
        	Client.client.evenement(e);
        }
    }

    void enchere(int prix, String i) {
        if (Client.fsm.enchere()) {
			Client.client.enchere(prix, i);
        }
    }

    void chat(String m, String i) {
        Logger.log("ClientEntry", 2, LogType.INF, "[chat] " + i + " dit : " + m);

        // inconditionnel, que ca s'affiche meme hors-onglet et que le log
        // l'aie note
// s'il y a un problème, c'est la fsm qu'il faut changer !!!
		if (Client.fsm.chat()) {
			Client.client.chat(m, i);
		}
	}

    void detailsVente(Vente v, List<Objet> liste) {
        if(Client.fsm.detailsVente()) {
            Client.ventemanager.detailsVente(v, liste);
            // jr : ajout p.r. Design : affichage de la vente
            // mis à jour ! *ding dong* !
            Client.hi.affichageVente(v);
        }
    }

    void detailsUtilisateur(Utilisateur u) {
        if(Client.fsm.detailsUtilisateur()) {
            Client.usermanager.detailsUtilisateur(u);
            // jr : modif p.r. Design : on aime actualiser la liste pour voir
            // les changements, aussi minimes soient-ils
            Client.hi.affichageListeUtilisateurs(Client.usermanager.getUtilisateurs());
        }
    }

    void listeObjets(Onglet t, Set<Objet> ol) {
        if(Client.fsm.listeObjets()) {
            Client.objectmanager.listeObjets(t, ol);
        }
    }

    void listeUtilisateurs(Set<Utilisateur> liste) {
        if(Client.fsm.listeUtilisateurs()) {
            Client.usermanager.listeUtilisateurs(liste);
        }
    }

    void listeParticipants(Set<Participant> liste) {
        if(Client.fsm.listeParticipants()) {
            Client.participantmanager.listeParticipants(liste);
            // modif P.R. Design : passage à HI pour que la liste
            // soit affichée dans l'Hôtel des Ventes.
            Client.hi.affichageListeParticipants(liste);
        }
    }

    void listeVentes(Set<Vente> liste) {
        if(Client.fsm.listeVentes()) {
            Client.ventemanager.listeVentes(liste);
        }
    }

    void resultatEdition(StatutEdition s) {
        if(Client.fsm.resultatEdition()) {
            Client.hi.resultatEdition(s);
        }
    }

    void etatParticipant(Participant p) {
// ??? inconditionnel pour raisons de cohérence
        Client.fsm.etatParticipant();
        Client.participantmanager.etatParticipant(p);
    }

    void superviseur(String u) {
// ??? inconditionnel pour raisons de cohérence
// s'il y a un problème, c'est la fsm qu'il faut corriger !!!
        if (Client.fsm.superviseur()) {
        	Client.client.superviseur(u);
        }
    }

}

/** Un thread qui répond aux commandes du Serveur.
 * Lancé à la connexion à ce dernier, fermé manu militari par déconnexion.
 */
class ClientEntryHandler extends Thread {

    Socket s;

    ClientEntryHandler(Socket socket) {
        this.s = socket;
    }

    /** Boucle de lecture des objets sérialisés reçus du socket.
    */
    public void run() {
        Logger.log("ClientEntryHandler", 1, LogType.INF, "[net] Ecoute des réponses Serveur lancée");
        Object o = null; // l'objet qui va être lu du Socket.
        ObjectInputStream ois = null; // le stream qui reçoit les objets.

        try {
            ois = new ObjectInputStream(s.getInputStream());
            do {
                o = ois.readObject(); // lire le message.

                if(o instanceof Message) {
                    Message m = (Message) o;
                    Logger.log("ClientEntryHandler", 2, LogType.INF, "[net] reçu message : " + m);
                    this.execute(m);
                } else {
                    Logger.log("ClientEntryHandler", 1, LogType.WRN, "[net] objet invalide du serveur " + s.getInetAddress() + " : ignoré");
                }
            } while ( !((o instanceof resultatLogin)
                        && (((resultatLogin) o).s == StatutLogin.Deconnecte)));

            // tant qu'on ne reçoit pas de StatutLogin(Déconnecté), on boucle.
            // sinon, on ferme le stream. La fermeture de Socket et autres est faite
            // par le Serveur.
            // en pratique, on finit toujours par être interrompu...


        } catch (IOException ioe) {
            // connexion fermée, ou interrompue d'une autre façon.
            Logger.log("ClientEntryHandler", 0, LogType.ERR, "[net] Listener déconnecté du serveur " + s.getInetAddress() + " : " + ioe.getMessage());
            Client.fsm.reset(); // on repart à zéro niveau FSM.
            Client.hi.forcerFermeture(); // et on revient au Login.
        } catch (Exception e) {
            Logger.log("ClientEntryHandler", 0, LogType.ERR, "[net] Exception : déconnexion du serveur " + s.getInetAddress());
            e.printStackTrace();
            Client.fsm.reset(); // on repart à zéro niveau FSM.
            Client.hi.forcerFermeture(); // et on revient au Login.
        } finally {
            // dans tous les cas, on ferme l'ObjectInputStream.
            // (s'il existe).
            if(ois != null)
                try {
                    ois.close();
                } catch (IOException ioe) {
                    // ne devrait pas arriver...
                    Logger.log("ClientEntryHandler", 1, LogType.ERR, "Erreur de fermeture" + " d'ObjectInputStream - NE DEVRAIT PAS ARRIVER");
                }
        }

    }

    /** Agit sur le système via ClientEntry en fonction du Message reçu. */
    private void execute(Message m) {
        if(m instanceof resultatLogin) {
            resultatLogin rl = (resultatLogin) m;
            Client.cliententry.resultatLogin(rl.s);
        } else if (m instanceof MessageNotification) {
            MessageNotification n = (MessageNotification) m;
            Client.cliententry.notification(n.n);
        } else if (m instanceof MessageEvenement) {
            MessageEvenement e = (MessageEvenement) m;
            Client.cliententry.evenement(e.e);
        } else if (m instanceof enchere) {
            enchere e = (enchere) m;
            Client.cliententry.enchere(e.prix, e.u);
        } else if (m instanceof chat) {
            chat c = (chat) m;
            Client.cliententry.chat(c.m, c.u);
        } else if (m instanceof detailsVente) {
            detailsVente dv = (detailsVente) m;
            Client.cliententry.detailsVente(dv.v, dv.o);
        } else if (m instanceof detailsUtilisateur) {
            detailsUtilisateur du = (detailsUtilisateur) m;
            Client.cliententry.detailsUtilisateur(du.u);
        } else if (m instanceof listeObjets) {
            listeObjets lo = (listeObjets) m;
            Client.cliententry.listeObjets(lo.type, lo.liste);
        } else if (m instanceof listeUtilisateurs) {
            listeUtilisateurs lu = (listeUtilisateurs) m;
            Client.cliententry.listeUtilisateurs(lu.liste);
        } else if (m instanceof listeParticipants) {
            listeParticipants lp = (listeParticipants) m;
            Client.cliententry.listeParticipants(lp.liste);
        } else if (m instanceof listeVentes) {
            listeVentes lv = (listeVentes) m;
            Client.cliententry.listeVentes(lv.liste);
        } else if (m instanceof resultatEdition) {
            resultatEdition re = (resultatEdition) m;
            Client.cliententry.resultatEdition(re.s);
        } else if (m instanceof etatParticipant) {
            etatParticipant ep = (etatParticipant) m;
            Client.cliententry.etatParticipant(ep.p);
        } else if (m instanceof superviseur) {
            superviseur s = (superviseur) m;
            Client.cliententry.superviseur(s.id);
        } else {
            Logger.log("ClientEntryHandler", 2, LogType.WRN, "[net] message de type non reconnu.");
        }
    }
}
