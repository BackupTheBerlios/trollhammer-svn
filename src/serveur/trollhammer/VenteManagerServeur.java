package trollhammer;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Classe Manager pour les Ventes.
 * Regroupe et gère les Ventes du Serveur.
 *
 * @author cfrey
 * @author sambuc
 * @author jruffin
 */
class VenteManagerServeur {
	private int lastId = -1; // l'identifiant de la dernière vente créée
	private VenteServeur venteEnCours = null; // vente en cours, null si aucune
	private List<VenteServeur> ventes; // liste des ventes, ordonnée selon t
	private long timerDerniereEnchere = -1; // utilisé pour gén auto coupdeMASSE

	// Constructeurs : START
    VenteManagerServeur() {
    	ventes = new ArrayList<VenteServeur>();
    	venteEnCours = null;
	}
	// Constructeurs : END

	// Méthodes du design : START
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
		
        Logger.log("VenteManagerServeur", 2, LogType.DBG,
        		   "Tentative d'insertion de" +" l'OID "+o+" à la position "
        		   +p+" de la vente "+v);
		
		if (vte != null) {
			// la vente existe
			if (vte.getDate() >= dateCourante) {
				// vente courante, prochaine ou à venir ?
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
						StatutObjet s = obj.getStatut();
						
						if (s == StatutObjet.Accepte) {
							// il est important de changer le statut d'abord,
							// puis de faire l'insertion, pour des raisons de
							// concurrence ...
							obj.setStatut(StatutObjet.EnVente);
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

					if (prochaineVente != null
						&& prochaineVente.getId() == vte.getId()) {
						// vte est la prochaine, changements -> broadcast
						Serveur.broadcaster.detailsVente(vte.copieVente(),
														 vte.getObjets());
					} else {
						// vte pas la prochaine, changements -> sender
						u.detailsVente(vte.copieVente(), vte.getObjets());
					}

                    // jr : oublié dans le Design, présent dans l'Analyse :
                    // renvoyer la liste d'Objets 'Planification' à la fin
                    // du mouvement pour mettre à jour sur le Client sender.
                    // cfrey: si tu le dis 
                    Serveur.objectmanager.obtenirListeObjets
                    	(Onglet.Planification, i);
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
        ObjetServeur obj = Serveur.objectmanager.getObjet(oid);
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(uid);
		
		if (vte != null && obj != null) {
			// la vente existe (jr : et l'objet aussi)
			if (vte != venteEnCours && (venteEnCours == null
				|| venteEnCours.getFirst() != oid)) {
				// l'objet n'est pas entrain d'être vendu
				vte.removeOId(oid);
                obj.setStatut(StatutObjet.Accepte);
				u.resultatEdition(StatutEdition.Reussi);
			} else {
				// l'objet est entrain d'être vendu
				u.resultatEdition(StatutEdition.NonTrouve);
                Logger.log("VenteManagerServeur", 2, LogType.WRN, "Objet "+oid
               	+" en train d'être vendu : refusé de retirer de la vente "+vid);
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

	/**
	 * Envoie les détails d'une vente au sender.
	 *
	 * @param	vid		identifiant d'une vente
	 * @param	sender	identifiant utilisateur
	 */
    void obtenirVente(int vid, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		VenteServeur vte = this.getVente(vid);
		if (u != null && vte != null) {
			u.detailsVente(vte.copieVente(), vte.getObjets());
		} else {
			Logger.log("VenteManagerServeur", 2, LogType.DBG,
				"obtenirVente: utilisateur nulle ou vente nulle");
		}
    }

	/**
	 * Envoie la liste des ventes au sender.
	 *
	 * @param	sender	identifiant utilisateur
	 */
    void obtenirListeVentes(String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		Set<Vente> liste = new HashSet<Vente>();
		
		for(VenteServeur v : ventes) {
			liste.add(v.copieVente());
		}
		u.listeVentes(liste);
    }

	/**
	 * Si il y a une vente en cours ou prévue, le sender reçoit les informations
	 * relatives.
	 *
	 * @param	sender	identifiant utilisateur
	 */
    void obtenirProchaineVente(String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		VenteServeur vs = this.getStarting();
		
        if(vs != null) {
        	// envoie les détails de la prochaine vente au sender
            this.obtenirVente(vs.getId(), sender);
            
            if(venteEnCours != null && vs.getId() == venteEnCours.getId()) {
            	Logger.log("VenteManagerServeur", 2, LogType.INF,
					"obtenirVente: la prochaine vente est la vente en cours");
                u.notification(Notification.VenteEnCours);
                if (Serveur.serveur.getDernierEncherisseur() != null) {
					u.enchere(Serveur.serveur.getPrixCourant(), Serveur.serveur.getDernierEncherisseur());
				}
				switch (Serveur.serveur.getMarteau()) {
					case 0: break;
					case 1: u.evenement(Evenement.CoupDeMassePAF1); break;
					case 2: u.evenement(Evenement.CoupDeMassePAF2); break;
					default:
				}
				if (vs.getMode() == Mode.Manuel) {
                    u.superviseur(vs.getSuperviseur());
                }
            }
        } else {
        	Logger.log("VenteManagerServeur", 2, LogType.INF,
				"obtenirProchaineVente: pas de prochaine vente");
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

	/**
	 * Retourne la liste des identifiants des ventes dans la liste (donc sans la 
	 * vente en cours s'il y en a une).
	 */
	List<Integer> getVIds() {
        List<Integer> r = new ArrayList<Integer>();
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
						u.resultatEdition(StatutEdition.Reussi);
					} else {
						u.resultatEdition(StatutEdition.NonTrouve);
					}
				} else {
					u.resultatEdition(StatutEdition.NonTrouve);
				}
				break;
				
			case Supprimer:
				// peut supprimer si vte pas en cours
				if (venteEnCours != null
					&& venteEnCours.getId() == vte.getId()) {
					u.resultatEdition(StatutEdition.NonTrouve);
				} else {
					// pas en cours ou en cours mais pas vte => suppression
					// bug ok: ne pas oublier de repasser les objets en statut
					//		"Accepté" ...
					for (Objet o : vte.getObjets()) {
						o.setStatut(StatutObjet.Accepte);
					}
					this.ventes.remove(this.getVente(vte.getId()));
					u.resultatEdition(StatutEdition.Reussi);
				}
				break;
				
			default:
				Logger.log("VenteManagerServeur", 2, LogType.DBG,
					"vente: édition invalide");
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
					} else {
                        // jr : rajout du renvoi des détails de la vente
                        // qui vient d'etre modifiée (faisait bugger, à raison,
                        // la FSM client lors de la modification
                        // de l'ordre des objets dans la liste
                        // d'une vente quand celle-ci n'était pas la prochaine)
                        u.detailsVente(vte.copieVente(), vte.getObjets());
                    }
					break;
				case Supprimer:
                    u.listeVentes(l);
					break;
				default:
			}
		}
    }

	/**
	 * Si un modérateur superviseur qui la vente en cours, certaines
	 * doivent être mises à jour, et le mode passe en automatique.
	 *
	 * @param	sender	identifiant utilisateur
	 */
    void modoLeaving(String sender) {
        /* si une vente est en cours et que le modo la supervise,
         * prévenir et passer en mode auto. */
        if(venteEnCours != null
           && sender.equals(venteEnCours.getSuperviseur())) {
            //venteEnCours.setSuperviseur(null);
            //venteEnCours.setMode(Mode.Automatique);
// on va plutôt faire appel à la modoLeaving du Vente non ?            
			venteEnCours.modoLeaving(sender);
// même question dans VenteServeur.java
            // à vérifier : est-ce que la Vente fait un broadcast de son
            // Mode aux participants présents ? Sinon, il faut penser à
            // le faire ici...
        } else {
        	Logger.log("VenteManagerServeur", 2, LogType.INF,
				"modoLeaving: il n'y a pas de vente en cours ou le modérateur"
				+ "n'en était pas le superviseur");
        }
    }

    /**
	 * Cherche une vente par son identifiant et la retourne,
     * ou null si non trouvée.
     *
     * @param	i	identifiant d'une vente
     */
    VenteServeur getVente(int i) {
		if (venteEnCours != null && venteEnCours.getId() == i) {
			return venteEnCours;
		} else {
			for(VenteServeur v : ventes) {
				if(v.getId() == i) {
					return v;
				}
			}
			return null;
		}
    }

    /**
	 * Retourne la prochaine vente: c'est celle qui est en cours si elle existe,
	 * autrement c'est la première de la liste.
     */
    VenteServeur getStarting() {
    
    	if (this.venteEnCours == null && ventes.size() > 0) {
    		return ventes.get(0);
    	} else {
    		// venteEnCours != null ou size == 0
    		return this.venteEnCours;
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
			// ls : On vérifie encore que la vente à démarrer possède des objets...
    		if (ventes.size() > 0
				&& ventes.get(0).getDate() <= Serveur.serveur.getDate()
				&& ventes.get(0).getOIds().size() > 0) {
				
    			this.venteEnCours = ventes.remove(0);
    			this.venteEnCours.setSuperviseur(null);
				Serveur.broadcaster.notification(Notification.DebutVente);
				Serveur.broadcaster.evenement(Evenement.VenteAutomatique);
				Logger.log("VenteManagerServeur", 2, LogType.INF, "Démarrage de vente!");
			}
			// aucune en retard, rien ne change
		}
		// s'il y en a une en cours, on fait exactement rien
	}
	
	/**
	 * Utilisé par envoyerCoupdeMASSE pour indiquer au manager des ventes que
	 * la vente courante est terminée.
	 */
	void terminateVenteEnCours() {
		this.venteEnCours = null;
	}

    // ajouté les paramètres génériques pour rendre le Comparator 'safe'
    // (pas de warnings à la compil)
	private class VentesComparator<T extends VenteServeur>
            implements Comparator<VenteServeur> {
		public int compare(VenteServeur v1, VenteServeur v2) {
			if (v1.getDate() < v2.getDate()) return -1;
			if (v1.getDate() > v2.getDate()) return +1; 
			return 0; // v1.getDate == v2.getDate ...
		}
		// always safe not to override "equals"
	}
	
	/**
	 * Ajoute une vente à la liste des ventes, à la bonne position en respectant
	 * l'ordre temporel croissant. NB: la liste est tout le temps triée.
	 *
	 * @param	v	vente serveur
	 */
	private void addVente(VenteServeur v) {
/*		int lastPos = this.ventes.size()-1;
		VenteServeur lastV = null;
		
		if (lastPos > -1) {
			Logger.log("VenteManagerServeur", 2, LogType.DBG, "addVente: lastPos="+lastPos);
			lastV = this.ventes.get(lastPos);
		}
		
		if (lastV == null
			|| v.getDate() >= lastV.getDate()) {
			this.ventes.add(v);
			if (lastV == null) Logger.log("VenteManagerServeur", 2, LogType.DBG, "addVente: lastV null");
			if (lastV != null && v.getDate() >= lastV.getDate()) Logger.log("VenteManagerServeur", 2, LogType.DBG, "addVente: v.getDate() >= lastV.getDate()");
		} else {
			if (lastV != null) Logger.log("VenteManagerServeur", 2, LogType.DBG, "addVente: lastV != null");
			if (v.getDate() < lastV.getDate()) Logger.log("VenteManagerServeur", 2, LogType.DBG, "addVente: v.getDate() < lastV.getDate()");
			
			for(int i = 0; i <= lastPos; i++) {
				if (v.getDate() < this.ventes.get(i).getDate()) {
					Logger.log("VenteManagerServeur", 2, LogType.DBG, "addVente: v.getDate() < ventes.get("+i+").getDate()");
					this.ventes.add(i, v);
					Logger.log("VenteManagerServeur", 2, LogType.DBG, "addVente:  added v at position "+i+"in ventes");
					return;
				}
				Logger.log("VenteManagerServeur", 2, LogType.DBG, "addVente: v.getDate() >= ventes.get("+i+").getDate()");
			}
*/
			
			this.ventes.add(v);
			Collections.sort(this.ventes, new VentesComparator<VenteServeur>());
//		}
	}
	
	// c'est un setter ....
	void setTimerDerniereEnchere(long date) {
		this.timerDerniereEnchere = date;
	}

	//getter pour sauvegarder le truc
	public int getLastId() {
		return lastId;
	}

    public List<VenteServeur> getVentes() {
		return ventes;
	}

	
	/**
	 * Envoi automatique du coup de masse, si nécessaire. Appelé dans
	 * VenteStarter (toutes les secondes). Si la dernière enchère date de plus
	 * de 30 secondes, en mode automatique, un coup de masse est généré.
	 */
	void donnerCoupdeMASSE() {
		
		if (venteEnCours != null
			&& venteEnCours.getMode() == Mode.Automatique
			&& Serveur.serveur.getDernierEncherisseur() != null) {
			
			if (Serveur.serveur.getDate() >
				this.timerDerniereEnchere + 30*1000) { 
				Serveur.serveur.envoyerCoupdeMASSE(null);
				this.setTimerDerniereEnchere(Serveur.serveur.getDate());
			}
		}
	}
}
