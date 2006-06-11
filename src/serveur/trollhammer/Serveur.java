package trollhammer;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

/**
 * Serveur Trollhammer.
 * Cette classe est un singleton.
 *
 * @author Lionel Sambuc & Charles François Rey
 * @author squelette : Julien Ruffin, implémentation : Julien Ruffin
 */
public class Serveur {
    
    /* champs du design */
    // private long date; // remplacé par getDate() !
    private int marteau;
    private int prix_courant;
    private String dernier_encherisseur;

    /* champ 'globaux'. Tous doivent se voir avoir une valeur
     * correcte attribuée au démarrage du Serveur. */

    static Serveur serveur = null;
    static ServeurEntry serveurentry = null;
    static Broadcaster broadcaster = null;
    static ObjectManagerServeur objectmanager = null;
    static UserManagerServeur usermanager = null;
    static ParticipantManagerServeur participantmanager = null;
    static VenteManagerServeur ventemanager = null;
    static ChatSystem chatsystem = null;

    /* la méthode main... */
    public static void main(String[] args) {
    	boolean consoleUI = true;
		boolean err = false;
		String defaultFile = "data.bin";
		// ls parse les arguements de la ligne de commande.
		int i = 0;
		while (i < args.length) {
			if(args[i].equals("-c")) {
				consoleUI = false;
			} else if (args[i].equals("-f")) { 
				if (args[++i].length() == 0) {
					err = true;
				} else {
					defaultFile = args[i];
				}
			}/*else if () { 
			// ajouter les arguments valables (pour extensibilité)
			} */else {
				err = true; // unknown argument
			}
			i++;
		}
		
		if (err) {
			Logger.log("Serveur", 0, LogType.ERR, "[sys] Paramètres invalide!\n" +
			"Syntaxe : java -jar serveur.jar [-c] [-f filename]\n" +
			"\t-c : utilise STDOUT au lieu de l'interface console utilisateur.\n"+
			"\t-f filename : le fichier à charger et dans lequel sauvegarder.\n");
		} else {
			if (consoleUI) {
				new Console();
			}
			Serveur.demarrer();
// mais à quoi ça sert de faire ces loadState/saveState avec defaultFile ???
// ... vu, Console va être modifié pour quitter autrement ce qui rendra son
// utilité à la chose ...
			Serveur.serveur.loadState(defaultFile);
			// ls : N'affiche pas le prompt si on est dans la console graphique.
			new CLIServeur(!consoleUI).interprete();
			Serveur.serveur.saveState(defaultFile);

		}
        /* attente finie => tout quitter, en forçant la main même au threads
         * qui attendent ad eternam (ie. le thread Listener)
         */
        Logger.log("Serveur", 0, LogType.INF, "[sys] Terminaison du serveur.");
        System.exit(0);
    }

    /* demarrage du serveur. Il ne peut en y avoir qu'une seule instance.*/
    static void demarrer() {
        if (Serveur.serveur == null) {
            Serveur.serveur = new Serveur();
        }
    }

    /* constructeur du Serveur, à son propre usage uniquement */
    private Serveur() {
        Logger.log("Serveur", 0, LogType.INF, "[sys] Démarrage serveur.");
        Serveur.serveurentry = new ServeurEntry();
        Serveur.broadcaster = new Broadcaster();
        Serveur.objectmanager = new ObjectManagerServeur();
        Serveur.usermanager = new UserManagerServeur();
        Serveur.participantmanager = new ParticipantManagerServeur();
        Serveur.ventemanager = new VenteManagerServeur();
        Serveur.chatsystem = new ChatSystem();

        /* le thread démarreur de ventes ! */
        new VenteStarter().start();

        Logger.log("Serveur", 0, LogType.INF, "[sys] Serveur démarré, passage en boucle d'attente.");
    }
    
    /* méthodes du design */

	/**
	 * Le superviseur donne un coup de marteau. Les utilisateurs sont avertis de
	 * cet événement. Si c'est le troisième coup, vendre l'objet au dernier en-
	 * chérisseur. Si la vente est en mode automatique et qu'un coup de marteau
	 * est donné manuellement par un modérateur, la vente passe en mode manuel
	 * et ce modérateur devient superviseur. S'il ne reste plus d'objets dans la
	 * vente, terminer la vente et envoyer les informations sur la suivante.
	 *
	 * @param	sender	identifiant du modérateur qui donne le coup de marteau
	 * author	cfrey
	 */
    void envoyerCoupdeMASSE(String sender) {
		VenteServeur venteEnCours = Serveur.ventemanager.getVenteEnCours();
		if (venteEnCours != null) {
			switch (venteEnCours.isSuperviseur(sender) ? 1 : 0) {
				case 0:
					if (venteEnCours.getMode() == Mode.Automatique) {
						if (Serveur.usermanager.isModo(sender)) {
							// promotion du modérateur en superviseur
							venteEnCours.setSuperviseur(sender);
							Serveur.broadcaster.superviseur(sender);
							venteEnCours.setMode(Mode.Manuel);
							// continue avec le cas où sender == superviseur
						}
					} else {
						// mode Manuel => un superviseur existe déjà => rien
						break; // (sort du switch)
					}
				case 1:
					// sender est un superviseur ou null (mode automatique)
					// NB: les questions de superviseur/enchérisseur devraient avoir
					// été gérées par encherir(prix, user).
					
					switch (this.marteau) { // nombre de coups de marteau
						case 0 :
							this.marteau = 1;
							Serveur.broadcaster.evenement(Evenement.CoupDeMassePAF1);
							break;
						case 1 :
							this.marteau = 2;
							Serveur.broadcaster.evenement(Evenement.CoupDeMassePAF2);
							break;
						case 2 :
							this.marteau = 0;
							Serveur.broadcaster.evenement(Evenement.Adjuge);
							venteEnCours.sellObject(dernier_encherisseur, prix_courant);
							this.dernier_encherisseur = null;
							// actualiser vue client
							//Serveur.broadcaster.detailsVente(venteEnCours, venteEnCours.getObjets());
							
							if (venteEnCours.getOIds().size() == 0) {
								// c'est le dernier objet qu'on a adjugé
								Serveur.broadcaster.notification(Notification.FinVente);
								Serveur.ventemanager.terminateVenteEnCours();
								VenteServeur nv = Serveur.ventemanager.getStarting();
								if (nv != null) {
									Serveur.broadcaster.detailsVente(nv, nv.getObjets());
								}
							} else {
                                // il reste des objets à vendre, mettre prix_courant
                                // au prix de base du nouvel objet
                                this.prix_courant = 
                                    venteEnCours.getObjets().get(0).getPrixDeBase();
                            }
							break;
						default :
							Logger.log("Serveur", 1, LogType.WRN, "wtf?? On est pas près de la finir cette vente!! marteau == " + marteau);
					}
				default:
					break;
			}
		} else {
			Logger.log("Serveur", 1, LogType.WRN, "Coups de masse donné, alors qu'il n'y a aucune vente en cours. Message ignoré.");
		}
    }

    boolean checkEnchere(int prix, String i) {
        if (Serveur.ventemanager.checkEncherisseur(i) 
			&& !i.equals(dernier_encherisseur)
			&& prix > prix_courant) {
			Logger.log("Serveur", 2, LogType.DBG, "checkEnchere : Enchère valide");
			return true;
		}
		else {
			Logger.log("Serveur", 2, LogType.DBG, "checkEnchere : Enchère invalide");
			return false;
		}
    }

    void doEnchere(int prix, String i) {
		prix_courant = prix;
		dernier_encherisseur = i;
		marteau = 0; 
    }

	//ls : Modification par rapport au Design model : 
	// checkEnchere appelle directement checkEncherisseur, vu que l'on utilise
	// qu'à cet endroit le retour de celle-ci, donc il est inutile de le stocker
	// et de le transmettre... (variable "c" du design model)
    void encherir(int prix, String i) {
		Logger.log("Serveur", 1, LogType.INF, i + " tente d'enchérir à " + prix);
		if (checkEnchere(prix, i)) {
			Logger.log("Serveur", 2, LogType.INF, i + " : enchère valide à " + prix);
			doEnchere(prix, i);
			Serveur.ventemanager.setTimerDerniereEnchere(getDate());
			Serveur.broadcaster.enchere(prix, i);
		} else {
			Logger.log("Serveur", 2, LogType.WRN, i + " : enchère invalide à " + prix);
		}
    }

	// ls : on devrait vérifier la validité des champs de o, à effectuer plus
	// tard.
    void envoyerProposition(Objet o, String sender) {
		o.setStatut(StatutObjet.Propose);
		Serveur.objectmanager.add(new ObjetServeur(o), sender);
		Serveur.usermanager.getUtilisateur(sender).resultatEdition(StatutEdition.Reussi);
		Serveur.objectmanager.obtenirListeObjets(Onglet.Vente, sender);
    }

    /* fin méthodes du design */

    long getDate() {
        return System.currentTimeMillis();
    }

	int getPrixCourant() {
		return prix_courant;
	}
	
	int getMarteau() {
		return marteau;
	}

    public String getDernierEncherisseur() {
    	return this.dernier_encherisseur;
    }
    
    public void saveState(String filename) {
    	try {
    		int un = 0, on = 0, vn = 0;
    		
    		FileOutputStream fos = new FileOutputStream(filename);
    		ObjectOutputStream oos = new ObjectOutputStream(fos);
    	
    		// les utilisateurs
    		Set<UtilisateurServeur> ul = Serveur.serveur.usermanager.getUtilisateurs();
    		Set<Utilisateur> ulp = new HashSet<Utilisateur>();
			for (Utilisateur up : ul) {
				un += 1;
				if (up instanceof ModerateurServeur) {
					ulp.add(new Moderateur(up.getLogin(), up.getNom(), up.getPrenom(), up.getStatut(), up.getMotDePasse()));
				} else if (up instanceof UtilisateurServeur) {
					ulp.add(new Utilisateur(up.getLogin(), up.getNom(), up.getPrenom(), up.getStatut(), up.getMotDePasse()));
				}
			}
			oos.writeObject(ulp);
			Logger.log("Serveur", 0, LogType.INF, "saveState: "+un+"#UtilisateurServeur");
			
			// les objets
			Set<ObjetServeur> ol = Serveur.serveur.objectmanager.getObjets();
			Set<Objet> olp = new HashSet<Objet>();
			for (Objet o : ol) {
				on += 1;
				olp.add(new Objet(o.getId(), o.getNom(), o.getDescription(), o.getModerateur(), o.getPrixDeBase(), o.getPrixDeVente(), o.getStatut(), o.getAcheteur(), o.getVendeur(), o.getImage()));
			}
			oos.writeInt(Serveur.serveur.objectmanager.getLastId());
			oos.writeObject(olp);
			Logger.log("Serveur", 0, LogType.INF, "saveState: "+on+"#ObjetServeur");
			
			// les ventes
			List<VenteServeur> vl = Serveur.serveur.ventemanager.getVentes();
			List<Vente> vlp = new ArrayList<Vente>();
			for (Vente v : vl) {
				vn += 1;
				vlp.add(new Vente(v.getId(), v.getNom(), v.getDescription(), v.getDate(), v.getMode(), v.getSuperviseur(), v.getOIds()));
			}
			oos.writeInt(Serveur.serveur.ventemanager.getLastId());
			oos.writeObject(vlp);
			Logger.log("Serveur", 0, LogType.INF, "saveState: "+vn+"#VenteServeur");
			
    	} catch (Exception e) {
			Logger.log("Serveur", 0, LogType.ERR, "saveState: "+e.toString());
    	}
    }
    
    public void loadState(String filename) {
    	try {
    		int un = 0, on = 0, vn = 0;
    		
    		FileInputStream fis = new FileInputStream(filename);
    		ObjectInputStream ois = new ObjectInputStream(fis);
    		
    		// les utilisateurs
    		Set<Utilisateur> ul = (HashSet<Utilisateur>) ois.readObject();
    		Set<UtilisateurServeur> ulp = new HashSet<UtilisateurServeur>();
    		for (Utilisateur u : ul) {
    			un += 1;
				if (u instanceof Moderateur) {
					ulp.add(new ModerateurServeur((Moderateur) u));
				} else {
					ulp.add(new UtilisateurServeur(u));
				}
    		}
    		Serveur.serveur.usermanager.setUtilisateurs(ulp);
    		Logger.log("Serveur", 0, LogType.INF, "loadState: "+un+"#UtilisateurServeur");
    		
    		// les objets
    		Serveur.serveur.objectmanager.setLastId(ois.readInt());
    		Set<Objet> ol = (HashSet<Objet>) ois.readObject();
    		Set<ObjetServeur> olp = new HashSet<ObjetServeur>();
    		for (Objet o : ol) {
    			on += 1;
    			olp.add(new ObjetServeur(o));
    		}
    		Serveur.serveur.objectmanager.setObjets(olp);
    		Logger.log("Serveur", 0, LogType.INF, "loadState: "+on+"#ObjetServeur");
    		
    		// les ventes
    		Serveur.serveur.ventemanager.setLastId(ois.readInt());
    		List<Vente> vl = (ArrayList<Vente>) ois.readObject();
    		List<VenteServeur> vlp = new ArrayList<VenteServeur>();
    		for (Vente v : vl) {
    			vn += 1;
    			vlp.add(new VenteServeur(v.getId(), v.getNom(), v.getDescription(), v.getDate(), v.getMode(), v.getSuperviseur(), v.getOIds()));
    		}
    		Serveur.serveur.ventemanager.setVentes(vlp);
    		Logger.log("Serveur", 0, LogType.INF, "loadState: "+vn+"#VenteServeur");
    		
    	} catch (Exception e) {
    		Logger.log("Serveur", 0, LogType.ERR, "loadState: "+e.toString());
    	}
    }
}
