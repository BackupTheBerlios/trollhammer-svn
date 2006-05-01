package trollhammer.serveur;
import trollhammer.commun;
import java.io.*;
import java.net.*;

class ServeurEntry {

    ServeurEntry() {
        // démarrage du thread listener, et c'est réglé.
        new ServeurEntryListener().start();
    }

    /* méthodes du design */

    // modif p.r. au design : passage du Socket en argument, pour
    // créer la Session qui permettra le renvoi de messages
    void login(Socket s, String u, String motDePasse, String sender) {
        Serveur.usermanager.login(s, u, motDePasse);
    }

    void logout(String sender) {
        Serveur.usermanager.logout(sender);
    }

    void envoyerChat(String msg, String sender) {
        System.out.println("[chat] "+sender+" dit : "+msg);
        Serveur.broadcaster.chat(msg, sender);
    }

    void envoyerCoupdeMASSE(String sender) {

    }

    void kickerUtilisateur(String u, String sender) {

    }

    void encherir(int prix, String sender) {

    }

    void envoyerProposition(Objet proposition, String sender) {

    }

    void validerProposition(int objet, String sender) {

    }

    void invaliderProposition(int objet, String sender) {

    }

    void insererObjetVente(int objet, int vente, int pos, String sender) {

    }

    void enleverObjetVente(int objet, int vente, String sender) {

    }

    void obtenirUtilisateur(String u, String sender) {

    }

    void utilisateur(Edition e, Utilisateur u, String sender) {

    }

    void obtenirListeObjets(Onglet type, String sender) {

    }

    void obtenirListeUtilisateurs(String sender) {

    }

    void obtenirListeVentes(String sender) {

    }

    void obtenirListeParticipants(String sender) {

    }

    void obtenirVente(int v, String sender) {

    }

    void obtenirProchaineVente(String sender) {

    }

    void vente(Edition e, Vente v, String sender) {

    }

    /* fin des méthodes du design */
}

/** Un thread en permanence en attente d'une nouvelle connexion,
 *  qui lance ensuite le thread qui lui est dédié.
 */

class ServeurEntryListener extends Thread {

    ServerSocket ss;

    public void run() {
        try {

            // jr : socket actif sur le port 4662. choix demi-arbitraire (ed2k)
            ss = new ServerSocket(4662); 
            System.out.println("[net] Serveur actif sur le port 4662");
            Socket s; // le socket d'une nouvelle connexion

            while(true) {
                s = ss.accept(); // attendre et obtenir le socket d'une
                                 // nouvelle connexion
                // et lancer le thread handler dessus.
                new ServeurEntryHandler(s).start();
            }
        } catch (Exception e) {
            System.out.println("[net] exception thread listener :");
            e.printStackTrace();
            try {
                // on balaie après soi, vé.
                ss.close();
            } catch (Exception ebis) {
                System.out.println("[net] exception sur la fermeture thread listener. La vie est rude.");
            }
        } 
    }
}

/** Un thread qui répond aux commandes d'une connexion particulière.
 * Lancé pour chaque nouvelle connexion établie.
 */
class ServeurEntryHandler extends Thread {

    Socket s;

    ServeurEntryHandler(Socket socket) {
        this.s = socket;
        System.out.println("[net] connexion de "+s.getInetAddress());
    }

    /** Boucle de lecture des objets sérialisés reçus du socket.
     */
    public void run() {
        Object o; // l'objet qui va être lu du Socket.

        try {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            do {
                o = ois.readObject(); // lire le message.

                if(o instanceof MessageClientServeur) {
                    MessageClientServeur m = (MessageClientServeur) o;
                    System.out.println("[net] reçu requête : "+m+" de "+m.sender);
                    this.execute(m);
                } else {
                    System.out.println("[net] objet invalide de "+s.getInetAddress()+" : ignoré");
                }
            } while (!(o instanceof logout));

            ois.close();

        } catch (IOException ioe) {
            // connexion fermée, ou interrompue d'une autre façon.
            System.out.println("[net] déconnexion de "+s.getInetAddress()+" : "+ioe.getMessage());

            /* on essaie de savoir si la connexion venait de la session d'un
             * Utilisateur, et si oui, on déconnecte ce dernier */
            UtilisateurServeur u = Serveur.usermanager.getUserForSocket(s);
            if(u != null) {
                u.disconnect();
            }
        } catch (Exception e) {
            System.out.println("[net] EXCEPTION : déconnexion de "+s.getInetAddress());
            e.printStackTrace();
        } finally {
            // de toute façon, on ferme le Socket.
            try {
                s.close();
            } catch (IOException ioeagain) {
                System.out.println("[net] EXCEPTION : fermeture du socket impossible : "+ioeagain.getMessage());
            }
        }

    }

    /** Agit sur le système via ServeurEntry en fonction du Message reçu. */
    private void execute(MessageClientServeur m) {
        if(m instanceof login) {
            login l = (login) m;
            Serveur.serveurentry.login(s, l.u, l.motdepasse, l.sender);
        } else if (m instanceof logout) {
            logout l = (logout) m;
            Serveur.serveurentry.logout(l.sender);
        } else if (m instanceof envoyerChat) {
            envoyerChat ec = (envoyerChat) m;
            Serveur.serveurentry.envoyerChat(ec.msg, ec.sender);
        } else if (m instanceof envoyerCoupdeMASSE) {
            envoyerCoupdeMASSE ecdm = (envoyerCoupdeMASSE) m;
            Serveur.serveurentry.envoyerCoupdeMASSE(ecdm.sender);
        } else if (m instanceof kickerUtilisateur) {
            kickerUtilisateur ku = (kickerUtilisateur) m;
            Serveur.serveurentry.kickerUtilisateur(ku.u, ku.sender);
        } else if (m instanceof encherir) {
            encherir e = (encherir) m;
            Serveur.serveurentry.encherir(e.prix, e.sender);
        } else if (m instanceof envoyerProposition) {
            envoyerProposition ep = (envoyerProposition) m;
            Serveur.serveurentry.envoyerProposition(ep.proposition, ep.sender);
        } else if (m instanceof validerProposition) {
            validerProposition vp = (validerProposition) m;
            Serveur.serveurentry.validerProposition(vp.objet, vp.sender);
        } else if (m instanceof invaliderProposition) {
            invaliderProposition ip = (invaliderProposition) m;
            Serveur.serveurentry.invaliderProposition(ip.objet, ip.sender);
        } else if (m instanceof insererObjetVente) {
            insererObjetVente iov = (insererObjetVente) m;
            Serveur.serveurentry.insererObjetVente(iov.objet, iov.vente, iov.pos, iov.sender);
        } else if (m instanceof enleverObjetVente) {
            enleverObjetVente eov = (enleverObjetVente) m;
            Serveur.serveurentry.enleverObjetVente(eov.objet, eov.vente, eov.sender);
        } else if (m instanceof obtenirUtilisateur) {
            obtenirUtilisateur ou = (obtenirUtilisateur) m;
            Serveur.serveurentry.obtenirUtilisateur(ou.u, ou.sender);
        } else if (m instanceof utilisateur) {
            utilisateur u = (utilisateur) m;
            Serveur.serveurentry.utilisateur(u.e, u.u, u.sender);
        } else if (m instanceof obtenirListeObjets) {
            obtenirListeObjets olo = (obtenirListeObjets) m;
            Serveur.serveurentry.obtenirListeObjets(olo.type, olo.sender);
        } else if (m instanceof obtenirListeUtilisateurs) {
            obtenirListeUtilisateurs olu = (obtenirListeUtilisateurs) m;
            Serveur.serveurentry.obtenirListeUtilisateurs(olu.sender);
        } else if (m instanceof obtenirListeVentes) {
            obtenirListeVentes olv = (obtenirListeVentes) m;
            Serveur.serveurentry.obtenirListeVentes(olv.sender);
        } else if (m instanceof obtenirListeParticipants) {
            obtenirListeParticipants olp = (obtenirListeParticipants) m;
            Serveur.serveurentry.obtenirListeParticipants(olp.sender);
        } else if (m instanceof obtenirVente) {
            obtenirVente ov = (obtenirVente) m;
            Serveur.serveurentry.obtenirVente(ov.v, ov.sender);
        } else if (m instanceof obtenirProchaineVente) {
            obtenirProchaineVente ov = (obtenirProchaineVente) m;
            Serveur.serveurentry.obtenirProchaineVente(ov.sender);
        } else if (m instanceof vente) {
            vente v = (vente) m;
            Serveur.serveurentry.vente(v.e, v.v, v.sender);
        } else {
            System.out.println("[net] message de type non reconnu : "+m);
        }
    }
}
