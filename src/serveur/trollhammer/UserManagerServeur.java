package trollhammer;
import java.util.Set;
import java.util.HashSet;

import java.net.Socket; // nécessaire à login()...

/**
 * Classe Manager pour les Utilisateurs.
 * Regroupe et gère les Utilisateurs et Modérateurs du Serveur.
 * Pour des raisons d'adaptation, les classes stockées sont respectivement
 * UtilisateurServeur et ModérateurServeur.
 *
 * @author squelette : Julien Ruffin
 */
class UserManagerServeur {

    private Set<UtilisateurServeur> utilisateurs;

    UserManagerServeur() {
        utilisateurs = new HashSet<UtilisateurServeur>();
    }
    
    UtilisateurServeur getUtilisateur(String login) {
        for(UtilisateurServeur u : utilisateurs) {
// cfrey: thou shall not follow the null pointer!
// login peut être null (à cause du mode auto, de isModo et envoyerCoupdeMASSE)
// O4: on pourrait sortir le test null en dehors de la boucle
            if(login != null && login.equals(u.getLogin())) {
                return u;
            }
        }
        return null;
    }

    Set<UtilisateurServeur> getConnected() {
        Set<UtilisateurServeur> liste = new HashSet<UtilisateurServeur>();
        for(UtilisateurServeur u : utilisateurs) {
            if(u.getStatut() == StatutLogin.Connecte_Utilisateur
               || u.getStatut() == StatutLogin.Connecte_Moderateur) {
                liste.add(u);
               }
        }
        return liste;
    }

    /* hors-design : utilisé par les threads de ServeurEntry pour identifier
     * la provenance d'un message */
    UtilisateurServeur getUserForSocket(Socket s) {
        for(UtilisateurServeur u : utilisateurs) {
            if(u.getSession() != null && u.getSession().getSocket() == s) {
                return u;
            }
        }
        return null;
    }

    boolean isConnected(String login) {
        UtilisateurServeur u = getUtilisateur(login);
        if(u != null &&
                (u.getStatut() == StatutLogin.Connecte_Utilisateur
                || u.getStatut() == StatutLogin.Connecte_Moderateur)
          ) {
            return true;
        } else {
            return false;
        }
    }

    boolean isModo(String login) {
        UtilisateurServeur u = getUtilisateur(login);
        if(u != null && u instanceof ModerateurServeur &&
                (u.getStatut() == StatutLogin.Connecte_Moderateur)
          ) {
            return true;
        } else {
            return false;
        }
    }

//fonction a virer, qui ne date pas du design model, et qui ne font pas le boulot comme il faut
//corrigerais ca après mardi, LS
    boolean addUtilisateur(UtilisateurServeur u) {
		for (Utilisateur i : utilisateurs) {
			if (i.getLogin().equals(u.getLogin())) {
				return false;
			}
		}
        utilisateurs.add(u);
		return true;
    }

    void kickerUtilisateur(String i, String sender) {
		UtilisateurServeur victime = this.getUtilisateur(i);
		if (victime != null && (victime.getStatut() == StatutLogin.Connecte_Utilisateur || victime.getStatut() == StatutLogin.Connecte_Moderateur)) {
			victime.notification(Notification.Kicke);
			victime.disconnect();
			Serveur.broadcaster.etatParticipant(victime);
		}
    }

    // modif p.r. au design : passage du Socket en argument,
    // histoire de savoir à quoi la Session se connecte
    void login(Socket s, String i, String mdp) {
        Logger.log("UserManagerServeur", 1, LogType.INF, "[login] tentative de login : " + i);
        SessionServeur sess = new SessionServeur(s);
        UtilisateurServeur u = this.getUtilisateur(i);

        if(u != null) {
            Logger.log("UserManagerServeur", 2, LogType.DBG, "[login] Utilisateur " + i + " trouvé");
            u.doLogin(sess, mdp);
        } else {
            Logger.log("UserManagerServeur", 1, LogType.WRN, "[login] Utilisateur " + i + " non trouvé");
            sess.resultatLogin(StatutLogin.Invalide);
        }
    }

    void logout(String sender) {
        Logger.log("UserManagerServeur", 1, LogType.INF, "[logout] Utilisateur " + sender);
        UtilisateurServeur u = this.getUtilisateur(sender);
        u.notification(Notification.Deconnexion);
        u.disconnect();
        Serveur.broadcaster.etatParticipant(u);
    }

    void obtenirListeUtilisateurs(String sender) {
		UtilisateurServeur u = this.getUtilisateur(sender);
		Set<Utilisateur> liste = new HashSet<Utilisateur>();
		for (UtilisateurServeur t : utilisateurs) {
			liste.add(t);
		}
		u.listeUtilisateurs(liste);
    }

    void obtenirListeParticipants(String sender) {
		UtilisateurServeur u = this.getUtilisateur(sender);
		Set<Participant> liste = new HashSet<Participant>();
		for (UtilisateurServeur t : utilisateurs) {
			liste.add(t);
		}
		u.listeParticipants(liste);
    }

    void obtenirUtilisateur(String i, String sender) {
		UtilisateurServeur s = this.getUtilisateur(sender);
		UtilisateurServeur u = this.getUtilisateur(i);
		s.detailsUtilisateur(u);
    }

	//ls : plein de truck a checker en plus.. a revoir..
    void utilisateur(Edition e, Utilisateur u, String sender) {
		UtilisateurServeur t = Serveur.usermanager.getUtilisateur(u.getLogin());
		UtilisateurServeur s = Serveur.usermanager.getUtilisateur(sender);
		
		UtilisateurServeur n = null;
		if (u instanceof Moderateur) {
            if(t != null) {
                n = new ModerateurServeur((Moderateur) u, t.getSession());
            } else {
                n = new ModerateurServeur((Moderateur) u);
            }
		} else {
            if(t != null) {
                n = new UtilisateurServeur(u, t.getSession());
            } else {
                n = new UtilisateurServeur(u);
            }
		}
		n.setStatut(u.getStatut());
		
		switch (e) {
		case Creer:
			if (t == null) {
				utilisateurs.add(n);
				s.resultatEdition(StatutEdition.Reussi);
			} else {
				s.resultatEdition(StatutEdition.ExisteDeja);
			}
			break;
		case Modifier:
			if (t != null) {
				if(n.getStatut() == StatutLogin.Banni) {
					kickerUtilisateur(n.getLogin(), sender);
				}
				utilisateurs.remove(t);
				utilisateurs.add(n);
				s.resultatEdition(StatutEdition.Reussi);
			} else {
				s.resultatEdition(StatutEdition.NonTrouve);
			}
			break;
		case Supprimer:
			if (t != null) {
				utilisateurs.remove(t);
				s.resultatEdition(StatutEdition.Reussi);
			} else {
				s.resultatEdition(StatutEdition.NonTrouve);
			}
			break;
		default: 
		}
		
		//ls : question code, c'est plus propre, mais je calcul dans un cas pour
		// rien la liste... car je ne la renvoie pas.
		Set<Utilisateur> lu = new HashSet<Utilisateur>();
		for(UtilisateurServeur us : utilisateurs) {
			lu.add(us);
		}

		switch (e) {
		case Creer:
		case Supprimer:
			s.listeUtilisateurs(lu);
			break;
		case Modifier:
			if (t == null) {
				s.listeUtilisateurs(lu);
			} else {
				s.detailsUtilisateur(u);
			}
			break;
		default: 
		}
		
    }

    Set<UtilisateurServeur> getUtilisateurs() {
        return utilisateurs;
    }
}
