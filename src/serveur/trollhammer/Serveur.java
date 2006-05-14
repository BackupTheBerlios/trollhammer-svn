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
        
		new CLI().interprete();
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

    void envoyerCoupdeMASSE(String sender) {
		UtilisateurServeur s = Serveur.usermanager.getUtilisateur(sender);
		//1 m := isModérateur(s)
		if (Serveur.usermanager.isModo(sender)) {
			//2 [m]: vc :=getVenteEnCours()
			VenteServeur vc = Serveur.ventemanager.getVenteEnCours();
			//3 [m && vc != null]: sup := vc.isSuperviseur(s)
			if (vc != null && vc.isSuperviseur(sender)) {
				//4 [m && sup && vc != null]: ok := checkPAF(s)
				//ls : Modif : CheckPAF retirée en tant que fonction a part, vu qu'elle
				//     n'est utilisée qu'ici...
				if (!vc.getObjets().isEmpty()) {
					if (vc.getMode() == Mode.Automatique) {
						vc.setMode(Mode.Manuel);
						vc.setSuperviseur(sender);
						Serveur.broadcaster.superviseur(sender);
					}
				}
				//ls : modif étape "6 [marteau == X]" vers 
				//     "6 [m && sup && vc != null && marteau == X]"
				//	   afin de n'augmenter la velauer de marteau que lorsque 
				//     l'on envoye l'événement aux clients risque de désynch 
				//     autrement...
				switch (marteau) {
				case 0:
					// 5a [m && sup && vc != null && marteau == 0]: événement(CoupDeMassePAF1)
					Serveur.broadcaster.evenement(Evenement.CoupDeMassePAF1);
					// 6a [m && sup && vc != null && marteau == 0]: marteau = 1
					marteau = 1;
					break;
				case 1:
					// 5b [m && sup && vc != null && marteau == 1]: événement(CoupDeMassePAF2)
					Serveur.broadcaster.evenement(Evenement.CoupDeMassePAF2);
					// 6b [m && sup && vc != null && marteau == 0]: marteau = 2
					marteau = 2;
					break;
				case 2:
					// 5c [m && sup && vc != null && marteau == 2]: événement(Adjugé)
					Serveur.broadcaster.evenement(Evenement.Adjuge);
					// 5c [m && sup && vc != null && marteau == 2]: sellObject(dernier_enchérisseur, prix_courant)
					vc.sellObject(dernier_encherisseur, prix_courant);
					if (vc.getObjets().isEmpty()) {
						VenteServeur nv = Serveur.ventemanager.getStarting();
						if (nv != null) {
							Serveur.broadcaster.detailsVente(nv, nv.getObjets());
						}
						Serveur.ventemanager.remove(vc.getId());
					}

					// 6c [m && sup && vc != null && marteau == 0]: marteau = 0
					marteau = 0;
					break;
				default:
					Logger.log("Serveur", 1, LogType.WRN, "wtf?? On est pas près de la finir cette vente!! marteau == " + marteau);
				}
			}
		}
    }

    boolean checkEnchere(int prix, String i) {
        if (Serveur.ventemanager.checkEncherisseur(i) 
			&& i != dernier_encherisseur 
			&& prix > prix_courant) {
			return true;
		}
		else {
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
		if (checkEnchere(prix, i)) {
			doEnchere(prix, i);
			Serveur.broadcaster.enchere(prix, i);
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

}
