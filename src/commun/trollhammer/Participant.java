package trollhammer;

/**
 * <p>Objet qui stocke les données de base d'un utilisateur. Ne contient que les
 * données nécessaires afin de l'afficher dans une vente. Permet aussi de ne 
 * transmettre les données sensibles d'un utilisateur que lorsque c'est vraiment
 * nécessaire.</p>
 *
 * @author Julien Ruffin
 */
class Participant implements java.io.Serializable {
    
    private String login;
    private String nom;
    private String prenom;
    private StatutLogin statut;

	// Constructeurs : START
	/**
	 * <p>Crée un participant, avec les valeurs assignées.</p>
	 *
	 * @param	login	Nom d'utilisateur, doit être unique, car c'est 
	 *					l'identifiant système (un String).
	 * @param	nom		Nom du particpant (un String)
	 * @param	prenom	Prenom du participant (un String)
	 */
    Participant(String login, String nom, String prenom) {
		this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.statut = StatutLogin.Deconnecte;
    }
	// Constructeurs : END
	
	// Méthodes du design : START
	// Méthodes du design : END
	
	// Setters & Getters : START
    public String getLogin() {
        return this.login;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public StatutLogin getStatut() {
        return this.statut;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setStatut(StatutLogin statut) {
        this.statut = statut;
    }
	// Setters & Getters : END
}
