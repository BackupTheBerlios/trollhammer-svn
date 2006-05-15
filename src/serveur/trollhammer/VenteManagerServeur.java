package trollhammer;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Manager pour les Ventes.
 * Regroupe et gère les Ventes du Serveur.
 *
 * @author cfrey
 * @author sambuc
 * @author jruffin
 */
class VenteManagerServeur {

	int lastId = -1;
	private VenteServeur venteEnCours;
	private List<VenteServeur> ventes;
	long timerDerniereEnchere = -1;

    VenteManagerServeur() {
    	ventes = new ArrayList<VenteServeur>();
    	venteEnCours = null;
	}
	
// begin cfrey: ??????
	//ls : ajout de cette fonction Ô combien necessaire... car je vois mal un
	//     objet (java) ce supprimer lui-même... enfin ca me parait plus logique
	//     ce soit ici, utiliser un prototype // a enleverObjetVente(...)
	/**
	 * Supprime une vente. Ne propoage aucune erreur si la vente a supprimer
	 * n'existe pas, car au final c'est la même chose, ie la vente n'existe plus.
	 *
	 * @param	iv	Id de la vente à supprimer.
	 */
	void remove(int iv) {
		VenteServeur vs = null;
		for(VenteServeur vt : ventes) {
			if (vt.getId() == iv) {
				vs = vt;
			}
		}
		if (vs != null) {
			ventes.remove(vs);
		}
	}
// end cfrey: ??????

	/**
	 * Attribution d'un objet à une vente. Si la position p vaut -1, cela signi-
	 * fie qu'il faut ajouter l'objet à la fin de la liste. Les changements sont
	 * répercutés chez le sender et chez tout le monde si la vente est la pro-
	 * aine. On ne peut pas assigner un objet à une vente en cours.
	 *
	 * @param 	o 	identifiant de l'objet
	 * @param 	v 	identifiant de la vente
	 * @param 	p 	position d'insertion de l'objet dans la liste
	 * @param 	i 	identifiant de l'utilisateur sender
	 */
	void insererObjetVente(int o, int v, int p, String i) {
		VenteServeur vte = getVente(v);
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(i);
		long dateCourante = Serveur.serveur.getDate();
		
        Logger.log("VenteManagerServeur", 2, LogType.DBG, "Tentative d'insertion de"
                +" l'OID "+o+" à la position "+p+" de la vente "+v);
		if (vte != null) {
			// la vente existe
			if (vte.getDate() >= dateCourante) {
				// vente courante, prochaine ou à venir ?
				//VenteServeur venteEnCours = getVenteEnCours();
				
				if (venteEnCours != null
					&& venteEnCours.getId() == vte.getId()) {
					// vte est la vente en cours, pas de modification possible !
					u.resultatEdition(StatutEdition.NonTrouve);
				} else {
					// pas de vente en cours ou vente en cours mais != vte
					ObjetServeur obj = Serveur.objectmanager.getObjet(o);
					VenteServeur prochaineVente = getStarting(); // != null ...
					
					if (obj != null) {
						// obj existe
						StatutObjet s = obj.getObjet().getStatut();
						
						if (s == StatutObjet.Accepte) {
							// il est important de changer le statut d'abord,
							// puis de faire l'insertion, pour des raisons de
							// concurrence ...
							obj.getObjet().setStatut(StatutObjet.EnVente);
							// /!\ je propose que ce soit Vente.insertObject
							// qui s'occupe des histoires de position ...
							vte.insertObject(o, p, i, dateCourante);
							u.resultatEdition(StatutEdition.Reussi);
						} else {
							// obj est Vendu, Proposé, Refusé ou EnVente
							u.resultatEdition(StatutEdition.NonTrouve);
						}
					} else {
						// obj n'existe pas
						u.resultatEdition(StatutEdition.NonTrouve);
					}

					if (prochaineVente != null && prochaineVente.getId() == vte.getId()) {
						// vte est la prochaine, changements -> broadcast
						Serveur.broadcaster.detailsVente(vte.copieVente(), vte.getObjets());
					} else {
						// vte pas la prochaine, changements -> sender
						u.detailsVente(vte.copieVente(), vte.getObjets());
					}

                    // jr : oublié dans le Design, présent dans l'Analyse :
                    // renvoyer la liste d'Objets 'Planification' à la fin
                    // du mouvement pour mettre à jour sur le Client sender.
                    Serveur.objectmanager.obtenirListeObjets(Onglet.Planification, i);
				}
			} else {
				// vente passée, pas de modification possible !
				u.resultatEdition(StatutEdition.NonTrouve);
// ??? pas dans la spéc: renvoyer la liste des ventes ???
			}
		} else {
			// la vente n'existe pas !
			u.resultatEdition(StatutEdition.NonTrouve);
// ??? pas dans la spéc: renvoyer la liste des ventes ???
		}
	}

// spéc étrange: la vente est en cours, on insère un objet en position 0 ...
// donc ça veut dire qu'on vend le 2ème. problème quand l'objet est adjugé, on
// supprime la tête de la liste des objets d'une vente ... donc est-ce qu'on
// ferait pas mieux de bloquer l'ajout/suppression d'objets à une vente en
// cours, tout simplement ? NB: pour l'instant c'est fait comme dans la spéc.
	/**
	 * Suppression d'un objet d'une vente. Les changements sont répercutés chez
	 * le client qui effectue la modification et chez tout le monde si la vente
	 * est en cours. On ne peut pas enlever un objet qui est entrain d'être
	 * vendu. NB: les pre sont checkées au niveau de l'Entry.
	 *
	 * @param	oid		identifiant de l'objet
	 * @param	vid		identifiant de la vente
	 * @param	uid		identifiant utilisateur
	 */
    void enleverObjetVente(int oid, int vid, String uid) {
		VenteServeur vte = this.getVente(vid);
        // jr : rajouté ceci, pour changer le statut de l'objet...
        ObjetServeur obj = Serveur.objectmanager.getObjet(oid);
// c'est pas la première fois que ça arrive, mais si u est null, et après je
// fais un u.resultatEdition ... problème. alors soit on modifie
// usermanager.getUtilisateur(uid) de telle sorte qu'il ne renvoie pas un null
// si l'uid ne correpond plus à qqch mais plutôt renvoie un truc non null bidon
// ou alors (mais ça requiert pas mal de changements), modifier resultatEdition
// pour qu'elle prenne en paramètre uid et qu'elle gère le cas null (donc ce
// serait plus une méthode de Utilisateur) ...
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(uid);
		
		if (vte != null && obj != null) {
			// la vente existe (jr : et l'objet aussi)

			if (vte != venteEnCours && (venteEnCours == null
                    || venteEnCours.getFirst() != oid)) {
                // jr : l'objet n'est PAS en train d'être vendu.
                // donc ce n'est PAS le premier de la liste de la vente,
                // de la vente EN COURS.
                // le test précédent était (vte.getFirst() == oid) !

				// l'objet n'est pas entrain d'être vendu
				vte.removeOId(oid);

                // jr : et on n'oublie pas de remettre son statut à "Accepté",
                // oui ?
                obj.getObjet().setStatut(StatutObjet.Accepte);

				u.resultatEdition(StatutEdition.Reussi);
			} else {
				// l'objet est entrain d'être vendu
// c'est pas la première fois, mais des résultatsEdition plus variés seraient
// souhaitables ...
				u.resultatEdition(StatutEdition.NonTrouve);
                Logger.log("VenteManagerServeur", 2, LogType.WRN, "Objet "+oid+
                        " en train d'être vendu : refusé de retirer de la vente "
                        +vid);
			}
			
			if (venteEnCours != null && venteEnCours.getId() == vte.getId()) {
				// vte en cours, changements -> broadcast
				Serveur.broadcaster.detailsVente(vte.copieVente(), vte.getObjets());
			} else {
				// vte pas en cours, changements -> sender
				u.detailsVente(vte.copieVente(), vte.getObjets());
			}

            // jr : oublié dans le Design, présent dans l'Analyse :
            // renvoyer la liste d'Objets 'Planification' à la fin
            // du mouvement pour mettre à jour sur le Client sender.
            Serveur.objectmanager.obtenirListeObjets(Onglet.Planification, uid);
		} else {
			// la vente n'existe pas (jr : ou l'objet)
			u.resultatEdition(StatutEdition.NonTrouve);
		}
    }

	// ls : A corriger?? : aucune gestion d'erreur...
	// cfrey: ajouté 1-2 petits checks
    void obtenirVente(int v, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		VenteServeur vs = this.getVente(v);
		if (u != null && vs != null) {
			u.detailsVente(vs.copieVente(), vs.getObjets());
		} // else message Log ?
    }

    void obtenirListeVentes(String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		//ls : Oui, je dois quand même le faire, il semble que java ne gère pas 
		// aussi bien les règle de sous-typage sur des classes paramétrées que 
		// Scala.
		// NB : c'est quand meme plus simple avec le casting implicite, non?
		// Et surtout ca évite un appel de fonction par itération... 
		Set<Vente> liste = new HashSet<Vente>();
		for(VenteServeur v : ventes) {
			liste.add(v.copieVente());
		}
		u.listeVentes(liste);
    }

	//ls : modif, le teste concernant le mode et imbriqué dans le if sur la
	// date, plutôt qu'à côté avec le test a double (sur la date). 
	//ls : modif, pour envoyer le message detailsVente, fais appel a la méthode
	// qui le fait, plutot que copier son code...
    // jr : exception si aucune vente en cours : ne rien renvoyer.
    // (sans le fix : NullPointerException pour trouver la prochaine vente inexistante.)
// cfrey: commentaires out-of-date
    void obtenirProchaineVente(String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		VenteServeur vs = this.getStarting();
        if(vs != null) {
            this.obtenirVente(vs.getId(), sender);
            if(venteEnCours != null && vs.getId() == venteEnCours.getId()) {
                u.notification(Notification.VenteEnCours);
                if (vs.getMode() == Mode.Manuel) {
                    u.superviseur(vs.getSuperviseur());
                }
            }
        }
	}

	/**
	 * Teste si un utiliseur peut enchérir ou pas. Un superviseur ne peut pas 
	 * enchérir.
	 *
	 * @param	uid		identifiant utilisateur
	 */
    boolean checkEncherisseur(String uid) {
    	// venteEnCours ne devrait pas être nul à ce stade, uid non plus
        return !uid.equals(venteEnCours.getSuperviseur());
    }

	Set<Integer> getVIds() {
		Set<Integer> r = new HashSet<Integer>();
		for(VenteServeur v : ventes) {
			r.add(v.getId());
		}
		return r;
	}

	/**
	 * Edition d'une vente. Création, modification ou suppression. Les
	 * changements sont transmis à l'éditeur, et à tout le monde si la vente
	 * est la prochaine. Il n'est pas possible de modifier une vente en cours
	 * ou de modifier une vente de telle sorte qu'elle ait lieu dans le passé.
	 * (NB: permis de scheduler une vente alors qu'une vente en cours)
	 *
	 * @param	e		opération d'édition
	 * @param	vte		Objet vente
	 * @param	uid		identifiant du sender
	 */
    void vente(Edition e, VenteServeur vte, String uid) {
    	UtilisateurServeur u = Serveur.usermanager.getUtilisateur(uid);
    	VenteServeur v = this.getVente(vte.getId());

		switch (e) {
			case Creer:
                Logger.log("VenteManagerServeur", 2, LogType.DBG, "Tentative de création de vente");
				// vérifie si vte existe déjà ou pas
				/* jr : N'EXISTE PAS EN PRATIQUE.
                 * En fin de compte, toutes les ventes que l'on crée
                 * sont au début dotées d'une ID par défaut par le Client.
                 * Si l'on vérifie l'unicité de l'ID avant de créer
                 * la vente et de lui associer son ID correcte,
                 * on peut à tout péter en créer une - celle dotée de l'ID
                 * par défaut - et c'est tout ! La création est, au final,
                 * inconditionnelle, ssi l'attribution des IDs se fait
                 * par le Serveur et non pas par le Client.
                 *
                 * La distinction création/modification, pour éviter de créer
                 * 2625 fois la même vente, se fait au niveau du Client.
                 *
                  if (v == null) {*/
					// peut créer si date pas dans le passé
					if (vte.getDate() > Serveur.serveur.getDate()) {
						addVente(vte);
						//ventes.add(vte);
						vte.setId(++this.lastId);
						u.resultatEdition(StatutEdition.Reussi);
                        Logger.log("VenteManagerServeur", 2, LogType.DBG, "Création de vente OK");
					} else {
						// dans le passé
// il faudrait vraiment un StatutEdition "Impossible" + message non ?
						u.resultatEdition(StatutEdition.NonTrouve);
                        Logger.log("VenteManagerServeur", 2, LogType.DBG, "Création de vente ratée : vente dans le passé");
					}
				/*} else {
					u.resultatEdition(StatutEdition.ExisteDeja);
                        Logger.log("VenteManagerServeur", 2, LogType.DBG, "Création de vente ratée : vente existe déjà");
				}*/
				break;
				
			case Modifier:
				// peut modifier que les ventes existantes à venir
				
				if (v != null) {
					// il y a donc une v avec le même id que vte
					if (vte.getDate() > Serveur.serveur.getDate()) {
						// on remplace v par vte
						this.ventes.remove(v);
						addVente(vte);
						//this.ventes.add(vte);
					} else {
						u.resultatEdition(StatutEdition.NonTrouve);
					}
				} else {
					u.resultatEdition(StatutEdition.NonTrouve);
				}
				break;
				
			case Supprimer:
				// peut supprimer si vte pas en cours
				if (venteEnCours != null && venteEnCours.getId() == vte.getId()) {
					u.resultatEdition(StatutEdition.NonTrouve);
				} else {
					// pas en cours ou en cours mais pas vte => suppression
					this.ventes.remove(this.getVente(vte.getId()));
					u.resultatEdition(StatutEdition.Reussi);
				}
				break;
				
			default:
		}
		
		// important que ça soit là et pas au début du bloc
		VenteServeur prochaineVente = getStarting();
		
		Set<Vente> l = new HashSet<Vente>();
		for(VenteServeur vi : this.ventes) {
			l.add(vi.copieVente()) ;
		}

// ---- pas encore conforme à la spec ...
		if (prochaineVente != null && prochaineVente.getId() == vte.getId()) {
            Logger.log("VenteManagerServeur", 2, LogType.DBG, "la vente est la prochaine, changements -> broadcast");
			// vte est la prochaine, changements -> broadcast
			switch (e) {
				case Creer:
					//if (v == null) {
						Serveur.broadcaster.listeVentes(l);
					//}
					break;
				case Modifier:
					if (v != null) {
						Serveur.broadcaster.detailsVente(vte.copieVente(), vte.getObjets());
					}
					break;
				case Supprimer:
					if (v != null) {
						Serveur.broadcaster.listeVentes(l);
					}
					break;
				default:
			}
		} else {
            Logger.log("VenteManagerServeur", 2, LogType.DBG, "la vente n'est"
                    +" pas la prochaine, changements -> sender");
			// vte pas la prochaine, changements -> sender
			//ls : ne faut-il pas renvoyerr la liste lors de la réussite de 
			//     l'opération (Créer|Supprimer)? a revoir
			switch (e) {
				case Creer:
                    u.listeVentes(l);
					break;
				case Modifier:
					if (v == null) {
						u.listeVentes(l);
					}
					break;
				case Supprimer:
                    u.listeVentes(l);
					break;
				default:
			}
		}
    }

    void modoLeaving(String sender) {
        /* si une vente est en cours et que le modo la supervise,
         * prévenir et passer en mode auto. */
        if(venteEnCours != null && sender.equals(venteEnCours.getSuperviseur())) {
            //venteEnCours.setSuperviseur(null);
            //venteEnCours.setMode(Mode.Automatique);
// on va plutôt faire appel à la modoLeaving du Vente non ?            
			venteEnCours.modoLeaving(sender);
// même question dans VenteServeur.java
            // à vérifier : est-ce que la Vente fait un broadcast de son
            // Mode aux participants présents ? Sinon, il faut penser à
            // le faire ici...
        }
    }

    /**
	 * Cherche une vente par son identifiant et la retourne,
     * ou null si non trouvée.
     */
    VenteServeur getVente(int i) {
        for(VenteServeur v : ventes) {
            if(v.getId() == i) {
                return v;
            }
        }
        return null;
    }

    /**
	 * Retourne la prochaine vente: c'est celle qui est en cours si elle existe,
	 * autrement c'est la première de la liste.
     */
    VenteServeur getStarting() {
		// jr : ne pas essayer de renvoyer la première
		// vente disponible s'il n'existe absolument
		// aucune vente dans le manager. Source
		// de IndexArrayOutOfBoundsException quand
		// le premier modo qui va créer les ventes
		// veut se connecter.
		// cfrey: ok :-)
    	if (this.venteEnCours == null && ventes.size() > 0) {
    		return ventes.get(0);
    	} else if(ventes.size() > 0) {
    		// != null, il y en a une en cours, on la retourne
    		// (même comportement qu'avant avec la comparaison des dates)
    		return this.venteEnCours;
    	} else { // jr : si zéro vente dans le manager, retourner... null
            return null;
        }
    }

    /**
	 * Retourne la vente en cours.
     */
    VenteServeur getVenteEnCours() {
		return this.venteEnCours;
	}
    
	/**
	 * Exécuté périodiquement pour vérifier s'il n'y a pas une vente à démarrer,
	 * cf VenteStarter. S'il y en a une, envoi de notification et événement, set
	 * Superviseur à null et mode automatique, pour être sûr.
	 * 
	 */
	void demarrerVente() {
		if (this.venteEnCours == null) {
    		// aucune actuellement, on vérifie s'il y en a pas une en retard.
    		// on va de toute façon pas en lancer une dans l'avenir par rapport
    		// à la date courante ...
    		if (ventes.size() > 0
                    && ventes.get(0).getDate() <= Serveur.serveur.getDate()) {
    			this.venteEnCours = ventes.remove(0);
    			this.venteEnCours.setSuperviseur(null);
    			Serveur.broadcaster.notification(Notification.DebutVente);
    			Serveur.broadcaster.evenement(Evenement.VenteAutomatique);
				Logger.log("VenteManagerServeur", 2, LogType.DBG, "Démarrage de vente!");
			}
			// aucune en retard, rien ne change
		}
		// s'il y en a une en cours, on fait exactement rien
	}
	
	// devra être utilisé par coupDeMASSE, il faut bien que quelqu'un signale
	// à VenteManagerServeur que la vente est finie (liste objets vide après
	// dernière adjudication). et oui, terminate ça veut dire ce que ça veut
	// dire.
	void terminateVenteEnCours() {
		this.venteEnCours = null;
	}
	
	// ajoute une vente à la liste de ventes, à la bonne position suivant la
	// date dans l'ordre croissant (ventes est tout le temps triée).
	private void addVente(VenteServeur v) {
		int lastId = this.ventes.size()-1;
		VenteServeur lastV = null;
		
		if (lastId > -1) lastV = this.ventes.get(lastId);
		
		if (lastV == null
			|| (lastV != null && v.getDate() >= lastV.getDate())) {
			this.ventes.add(v);
		} else {
			for(int i = 0; i <= lastId; i++) {
				if (v.getDate() < this.ventes.get(i).getDate()) {
					this.ventes.add(i, v);
					return;
				}
			}
		}
	}
	
	void setTimerDerniereEnchere(long date) {
		this.timerDerniereEnchere = date;
	}

	
	/**
	 * Envoi automatique du coup de masse, si nécessaire. Appelé dans
	 * VenteStarter (toutes les secondes). Si la dernière enchère date de plus
	 * d'une minute, en mode automatique, un coup de masse est généré.
	 */
	void donnerCoupdeMASSE() {
		
		if (venteEnCours != null && venteEnCours.getMode() == Mode.Automatique) {
			if (Serveur.serveur.getDate() > this.timerDerniereEnchere + 60*1000) {
				Serveur.serveur.envoyerCoupdeMASSE(null);
				this.setTimerDerniereEnchere(Serveur.serveur.getDate());
			}
		}
	}

}
