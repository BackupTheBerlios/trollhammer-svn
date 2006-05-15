package trollhammer;

/**
 * Serveur Trollhammer.
 * Cette classe est un singleton.
 *
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
    static ServeurEntry serveurentry;
    static Broadcaster broadcaster;
    static ObjectManagerServeur objectmanager;
    static UserManagerServeur usermanager;
    static ParticipantManagerServeur participantmanager;
    static VenteManagerServeur ventemanager;
    static ChatSystem chatsystem;

    /* la méthode main... */
    public static void main(String[] args) {
        Serveur.demarrer();

        // CECI EST UN TEST
        
        Serveur.usermanager.addUtilisateur(new ModerateurServeur("tefal", "", "", "tefal"));
        Serveur.usermanager.addUtilisateur(new ModerateurServeur("deneo", "", "", "deneo"));
        Serveur.usermanager.addUtilisateur(new ModerateurServeur("mithrandir", "", "", "mithrandir"));
        Serveur.usermanager.addUtilisateur(new ModerateurServeur("dolarcles", "", "", "dolarcles"));
        Serveur.usermanager.addUtilisateur(new ModerateurServeur("spitfire", "", "", "spitfire"));
        Serveur.usermanager.addUtilisateur(new UtilisateurServeur("jruffin", "", "", "jruffin"));
        Serveur.usermanager.addUtilisateur(new UtilisateurServeur("becholey", "", "", "becholey"));
        Serveur.usermanager.addUtilisateur(new UtilisateurServeur("sambuc", "", "", "sambuc"));
        Serveur.usermanager.addUtilisateur(new UtilisateurServeur("cfrey", "", "", "cfrey"));
        Serveur.usermanager.addUtilisateur(new UtilisateurServeur("richon", "", "", "richon"));
        
        // FIN DU TEST
        
		new CLIServeur().interprete();
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
// mode automatique: problème ... qui envoie envoyerCoupdeMASSE ? il devrait y
// avoir un timeout côté serveur qui génère des coups de marteau ...
    void envoyerCoupdeMASSE(String sender) {
		VenteServeur venteEnCours = Serveur.ventemanager.getVenteEnCours();
		
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
						}
						break;
					default :
						Logger.log("Serveur", 1, LogType.WRN, "wtf?? On est pas près de la finir cette vente!! marteau == " + marteau);
				}
			default:
				break;
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
			Logger.log("Serveur", 2, LogType.INF, i+" : enchère valide à "+prix);
			doEnchere(prix, i);
			Serveur.ventemanager.setTimerDerniereEnchere(getDate());
			Serveur.broadcaster.enchere(prix, i);
		} else {
			Logger.log("Serveur", 2, LogType.WRN, i+" : enchère invalide à "+prix);
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

}
