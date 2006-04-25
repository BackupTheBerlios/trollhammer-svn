package trollhammer;
import java.util.Set;
import java.util.List;
import java.io.*;
import java.net.*;

class ClientEntry {

    void résultatLogin(StatutLogin s) {

    }

    void notification(Notification n) {

    }

    void événement(Evénement e) {

    }

    void enchère(int prix, String i) {

    }

    void chat(String m, String i) {

    }

    void détailsVente(Vente v, List<Objet> liste) {

    }

    void détailsUtilisateur(Utilisateur u) {

    }

    void listeObjets(Onglet t, Set<Objet> ol) {

    }

    void listeUtilisateurs(Set<Utilisateur> liste) {

    }
    
    void listeParticipants(Set<Participant> liste) {

    }

    void listeVentes(Set<Vente> liste) {

    }

    void résultatEdition(StatutEdition s) {

    }

    void étatParticipant(Participant p) {

    }

    void superviseur(String u) {

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
        System.out.println("[net] Ecoute des réponses Serveur lancée");
        Object o; // l'objet qui va être lu du Socket.

        try {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            do {
                o = ois.readObject(); // lire le message.

                if(o instanceof Message) {
                    Message m = (Message) o;
                    this.execute(m);
                } else {
                    System.out.println("[net] objet invalide du serveur "+s.getInetAddress()+" : ignoré");
                }
            } while ( !((o instanceof résultatLogin)
                         && (((résultatLogin) o).s == StatutLogin.Déconnecté)) );

            // tant qu'on ne reçoit pas de StatutLogin(Déconnecté), on boucle.
            // sinon, on ferme le stream. La fermeture de Socket et autres est fait
            // lors du traitement par ClientEntry.

            ois.close();

        } catch (IOException ioe) {
            // connexion fermée, ou interrompue d'une autre façon.
            System.out.println("[net] Listener déconnecté du serveur "+s.getInetAddress()+" : "+ioe.getMessage());
        } catch (Exception e) {
            System.out.println("[net] EXCEPTION : déconnexion du serveur "+s.getInetAddress());
            e.printStackTrace();
        } finally {
            /*// de toute façon, on ferme le Socket.
            try {
                s.close();
            } catch (IOException ioeagain) {
                System.out.println("[net] EXCEPTION : socket déjà fermé : "+ioeagain.getMessage());
            }*/
        }

    }

    /** Agit sur le système via ClientEntry en fonction du Message reçu. */
    private void execute(Message m) {
        if(m instanceof résultatLogin) {
            résultatLogin rl = (résultatLogin) m;
            Client.cliententry.résultatLogin(rl.s);
        } else if (m instanceof notification) {
            notification n = (notification) m;
            Client.cliententry.notification(n.n);
        } else if (m instanceof événement) {
            événement e = (événement) m;
            Client.cliententry.événement(e.e);
        } else if (m instanceof enchère) {
            enchère e = (enchère) m;
            Client.cliententry.enchère(e.prix, e.u);
        } else if (m instanceof chat) {
            chat c = (chat) m;
            Client.cliententry.chat(c.m, c.u);
        } else if (m instanceof détailsVente) {
            détailsVente dv = (détailsVente) m;
            Client.cliententry.détailsVente(dv.v, dv.o);
        } else if (m instanceof détailsUtilisateur) {
            détailsUtilisateur du = (détailsUtilisateur) m;
            Client.cliententry.détailsUtilisateur(du.u);
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
        } else if (m instanceof résultatEdition) {
            résultatEdition re = (résultatEdition) m;
            Client.cliententry.résultatEdition(re.s);
        } else if (m instanceof étatParticipant) {
            étatParticipant ep = (étatParticipant) m;
            Client.cliententry.étatParticipant(ep.p);
        } else if (m instanceof superviseur) {
            superviseur s = (superviseur) m;
            Client.cliententry.superviseur(s.id);
        } else {
            System.out.println("[net] message de type non reconnu.");
        }
    }
}
