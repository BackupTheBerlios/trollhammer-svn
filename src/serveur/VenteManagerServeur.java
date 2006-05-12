package trollhammer;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Manager pour les Ventes.
 * Regroupe et gère les Ventes du Serveur.
 *
 * @author jruffin
 */
class VenteManagerServeur {

	int lastId = -1;
	
	private VenteServeur venteEnCours;
	
	private List<VenteServeur> ventes;

    //private Set<VenteServeur> ventes;

    VenteManagerServeur() {
    	ventes = new ArrayList<VenteServeur>();
    	venteEnCours = null;
        //ventes = new HashSet<VenteServeur>();
    }

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
	 * @author 	cfrey
	 */
	void insererObjetVente(int o, int v, int p, String i) {
		VenteServeur vte = getVente(v);
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(i);
		long dateCourante = Serveur.serveur.getDate();
		
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
						Serveur.broadcaster.detailsVente(vte, vte.getObjets());
					} else {
						// vte pas la prochaine, changements -> sender
						u.detailsVente(vte, vte.getObjets());
					}
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
	 * @author	cfrey
	 */
    void enleverObjetVente(int oid, int vid, String uid) {
		VenteServeur vte = this.getVente(vid);
// c'est pas la première fois que ça arrive, mais si u est null, et après je
// fais un u.resultatEdition ... problème. alors soit on modifie
// usermanager.getUtilisateur(uid) de telle sorte qu'il ne renvoie pas un null
// si l'uid ne correpond plus à qqch mais plutôt renvoie un truc non null bidon
// ou alors (mais ça requiert pas mal de changements), modifier resultatEdition
// pour qu'elle prenne en paramètre uid et qu'elle gère le cas null (donc ce
// serait plus une méthode de Utilisateur) ...
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(uid);
		
		if (vte != null) {
			// la vente existe
			if (vte.getFirst() == oid) {
				// l'objet n'est pas entrain d'être vendu
				vte.removeOId(oid);
				u.resultatEdition(StatutEdition.Reussi);
			} else {
				// l'objet est entrain d'être vendu
// c'est pas la première fois, mais des résultatsEdition plus variés seraient
// souhaitables ...
				u.resultatEdition(StatutEdition.NonTrouve);
			}
			
			if (venteEnCours != null && venteEnCours.getId() == vte.getId()) {
				// vte en cours, changements -> broadcast
				Serveur.broadcaster.detailsVente(vte, vte.getObjets());
			} else {
				// vte pas en cours, changements -> sender
				u.detailsVente(vte, vte.getObjets());
			}
		} else {
			// la vente n'existe pas
			u.resultatEdition(StatutEdition.NonTrouve);
		}
    }

	// ls : A corriger?? : aucune gestion d'erreur...
    void obtenirVente(int v, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		VenteServeur vs = this.getVente(v);
		List<Objet> lo = vs.getObjets();
		u.detailsVente(vs, lo);
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
			liste.add(v);
		}
		u.listeVentes(liste);
    }

	//ls : modif, le teste concernant le mode et imbriqué dans le if sur la
	// date, plutôt qu'à côté avec le test a double (sur la date). 
	//ls : modif, pour envoyer le message detailsVente, fais appel a la méthode
	// qui le fait, plutot que copier son code...
    // jr : exception si aucune vente en cours : ne rien renvoyer.
    // (sans le fix : NullPointerException pour trouver la prochaine vente inexistante.)
    void obtenirProchaineVente(String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		VenteServeur vs = this.getStarting();
        if(vs != null) {
            this.obtenirVente(vs.getId(), sender);
            if(vs.getId() == venteEnCours.getId()) {
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
	 * @author	cfrey
	 */
    boolean checkEncherisseur(String uid) {
    	// venteEnCours ne devrait pas être nul à ce stade
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
	 * @author	cfrey
	 */
    void vente(Edition e, VenteServeur vte, String uid) {
    	UtilisateurServeur u = Serveur.usermanager.getUtilisateur(uid);
    	VenteServeur v = this.getVente(vte.getId());
    	
		switch (e) {
			case Creer:
				// vérifie si vte existe déjà ou pas
				if (v == null) {
					// peut créer si date pas dans le passé
					if (vte.getDate() > Serveur.serveur.getDate()) {
						ventes.add(vte);
						vte.setId(++this.lastId);
						u.resultatEdition(StatutEdition.Reussi);
					} else {
						// dans le passé
// il faudrait vraiment un StatutEdition "Impossible" + message non ?
						u.resultatEdition(StatutEdition.NonTrouve);
					}
				} else {
					u.resultatEdition(StatutEdition.ExisteDeja);
				}
				break;
				
			case Modifier:
				// peut modifier que les ventes existantes à venir
				
				if (v != null) {
					// il y a donc une v avec le même id que vte
					if (vte.getDate() > Serveur.serveur.getDate()) {
						// on remplace v par vte
						this.ventes.remove(v);
						this.ventes.add(vte);
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
			l.add(vi);
		}
		
// ----
		if (prochaineVente != null && prochaineVente.getId() == vte.getId()) {
			// vte est la prochaine, changements -> broadcast
			switch (e) {
				case Creer:
					if (v == null) {
						Serveur.broadcaster.listeVentes(l);
					}
					break;
				case Modifier:
					if (v != null) {
						Serveur.broadcaster.detailsVente(vte, vte.getObjets());
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
			// vte pas la prochaine, changements -> sender
			switch (e) {
				case Creer:
					break;
				case Modifier:
					if (v == null) {
						u.listeVentes(l);
					}
					break;
				case Supprimer:
					break;
				default:
			}
		}
    }

    void modoLeaving(String sender) {
        /* si une vente est en cours et que le modo la supervise,
         * prévenir et passer en mode auto. */
        if(venteEnCours != null && sender.equals(venteEnCours.getSuperviseur())) {
            venteEnCours.setSuperviseur(null);
            venteEnCours.setMode(Mode.Automatique);
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
    	if (this.venteEnCours == null) {
    		return ventes.get(0);
    	} else {
    		// != null, il y en a une en cours, on la retourne
    		// (même comportement qu'avant avec la comparaison des dates)
    		return this.venteEnCours;
    	}
    }
    	
//        long min = Long.MAX_VALUE;
//        long prevmin = 0;
//        VenteServeur starting = null;
//
//        /* on prend la vente la plus proche dans le temps */
//        for(VenteServeur v : ventes) {
//            prevmin = min;
//            min = Math.min(min, v.getDate());
//            if(min < prevmin) {
//                starting = v;
//            }
//        }
//
//        return starting;

    /**
	 * Retourne la vente en cours. S'il en existe déjà une elle ne change pas,
	 * et si on en a une en retard, on la balance, autrement reste null.
	 *
	 * @author	cfrey
     */
    VenteServeur getVenteEnCours() {
		return this.venteEnCours;
	}
//        // on commence par prendre la vente la plus proche dans le temps
//        VenteServeur encours = getStarting();
//
//        /* puis on vérifie si elle a démarré ! Si pas,
//         * alors on retourne null (ie. aucune vente n'est en cours)
//         */
//        if(encours != null && encours.getDate() < Serveur.serveur.getDate()) {
//            return encours;
//        } else {
//            return null;
//        }
    
	/**
	 * Exécuté périodiquement pour vérifier s'il n'y a pas une vente à démarrer,
	 * cf VenteStarter. S'il y en a une, envoi de notification et événement, set
	 * Superviseur à null et mode automatique, pour être sûr.
	 * 
	 * @author	cfrey
	 */
	void demarrerVente() {
		if (this.venteEnCours == null) {
    		// aucune actuellement, on vérifie s'il y en a pas une en retard.
    		// on va de toute façon pas en lancer une dans l'avenir par rapport
    		// à la date courante ...
    		if (ventes.get(0).getDate() <= Serveur.serveur.getDate()) {
    			this.venteEnCours = ventes.remove(0);
    			this.venteEnCours.setSuperviseur(null);
    			Serveur.broadcaster.notification(Notification.DebutVente);
    			Serveur.broadcaster.evenement(Evenement.VenteAutomatique);
			}
			// aucune en retard, rien ne change
		}
		// s'il y en a une en cours, on fait exactement rien
	}
	
	// devra être utilisé par coupDeMASSE, il faut bien que quelqu'un signale
	// à VenteManagerServeur que la vente est finie (liste objets vide après
	// dernière adjudication).
	void targetVenteEnCoursForTermination() {
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

}
