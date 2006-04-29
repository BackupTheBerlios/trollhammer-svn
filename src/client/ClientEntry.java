package trollhammer;
import java.util.Set;
import java.util.List;
import java.io.*;
import java.net.*;

class ClientEntry {

    void resultatLogin(StatutLogin s) {
        switch(s) {
            case Connecte_Utilisateur:
                System.out.println("[login] reçu réponse : Connecté Utilisateur");
                Client.hi.voir(Onglet.HôtelDesVentes);
                Client.session.setModerateur(false);
                Client.session.setConnecte(true);
                break;
            case Connecte_Moderateur:
                System.out.println("[login] reçu réponse : Connecté Modérateur");
                Client.hi.voir(Onglet.HôtelDesVentes);
                Client.session.setModerateur(true);
                Client.session.setConnecte(true);
                break;
            case Banni:
                System.out.println("[login] reçu réponse : Banni");
                Client.hi.messageErreur(Erreur.Banni);
                // mod p.r. design : pas de destroy() (impossible en Java),
                // mais on tient quand même à fermer la connexion!
                Client.session.fermer();
                Client.session = null;
                break;
            case Invalide:
                System.out.println("[login] reçu réponse : Invalide");
                Client.hi.messageErreur(Erreur.Invalide);
                // mod p.r. design : pas de destroy() (impossible en Java),
                // mais on tient quand même à fermer la connexion!
                Client.session.fermer();
                Client.session = null;
                break;
        }
    }

    void notification(Notification n) {
        Client.hi.message(n);

        switch (n) {
            case FinVente:
                Client.humain.setVente(null);
                break;
            case DebutVente:
            case VenteEnCours:
                Vente v = Client.ventemanager.getVenteEnCours();
                if(v != null) {
                    Client.humain.setVente(v);
                }
        }
    }

    void evenement(Evenement e) {
        Client.hi.affichage(e);
        Vente v = Client.humain.getVente();

        if (v != null && e == Evenement.Adjuge) {
            v.removeFirst();
            VenteClientAdapter.setPrices(v);
        } else if (v != null && e == Evenement.VenteAutomatique) {
            v.setMode(Mode.Automatique);
        }
    }

    void enchere(int prix, String i) {
        Client.client.setPrixCourant(prix);

        // incrément de 10% sur le prix initial
        // dans le design, on avait fait l'erreur de
        // faire 10% sur le prix _courant_.
        Objet o = Client.objectmanager.getObject(
                Client.humain.getVente().getFirst());
        Client.client.setPrixCourant((int) 1.1*o.getPrixDeBase());
        
        Client.client.setDernierEncherisseur(i);

        if (Client.client.getMode() == Onglet.HôtelDesVentes) {
            Client.hi.affichageEnchere(prix, i);
        }
    }

    void chat(String m, String i) {
        System.out.println("[chat] "+i+" dit : "+m);
        Client.hi.affichageChat(m, i);
    }

    void detailsVente(Vente v, List<Objet> liste) {
        Client.ventemanager.detailsVente(v, liste);
    }

    void detailsUtilisateur(Utilisateur u) {
        Client.usermanager.detailsUtilisateur(u);
    }

    void listeObjets(Onglet t, Set<Objet> ol) {
        Client.objectmanager.listeObjets(t, ol);
    }

    void listeUtilisateurs(Set<Utilisateur> liste) {
        Client.usermanager.listeUtilisateurs(liste);
    }
    
    void listeParticipants(Set<Participant> liste) {
        Client.participantmanager.listeParticipants(liste);
    }

    void listeVentes(Set<Vente> liste) {
        Client.ventemanager.listeVentes(liste);
    }

    void resultatEdition(StatutEdition s) {
        Client.hi.resultatEdition(s);
    }

    void etatParticipant(Participant p) {
        Client.participantmanager.etatParticipant(p);
    }

    void superviseur(String u) {
        Client.client.setSuperviseur(u);
        Vente v = Client.humain.getVente();
        v.setMode(Mode.Manuel);
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
                    System.out.println("[net] reçu message : "+m);
                    this.execute(m);
                } else {
                    System.out.println("[net] objet invalide du serveur "+s.getInetAddress()+" : ignoré");
                }
            } while ( !((o instanceof resultatLogin)
                         && (((resultatLogin) o).s == StatutLogin.Deconnecte)) );

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
        if(m instanceof resultatLogin) {
            resultatLogin rl = (resultatLogin) m;
            Client.cliententry.resultatLogin(rl.s);
        } else if (m instanceof notification) {
            notification n = (notification) m;
            Client.cliententry.notification(n.n);
        } else if (m instanceof evenement) {
            evenement e = (evenement) m;
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
            System.out.println("[net] message de type non reconnu.");
        }
    }
}
