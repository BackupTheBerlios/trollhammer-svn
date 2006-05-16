package trollhammer;

/**
 * <p>Objet qui stocke les données d'un utilisateur.</p>
 *
 * @author Julien Ruffin
 */
class Utilisateur extends Participant {

    private String mot_de_passe;

	// Constructeurs : START
	/**
	 * <p>Crée un Utilisateur, avec les valeurs assignées.</p>
	 *
	 * @param	login			Nom d'utilisateur, doit être unique, car c'est 
	 *							l'identifiant système (un String).
	 * @param	nom				Nom de l'utilisateur (un String)
	 * @param	prenom			Prenom de l'utilisateur (un String)
	 * @param	mot_de_passe	Mot de passe de l'utilisateur (un String)
	 */
    Utilisateur(String login, String nom, String prenom, String mot_de_passe) {
        super(login, nom, prenom);
        this.mot_de_passe = mot_de_passe;
    }
	// Constructeurs : END
	
	// Méthodes du design : START
	// Méthodes du design : END
	
	// Setters & Getters : START
    public String getMotDePasse() {
        return this.mot_de_passe;
    }

    public void setMotDePasse(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }
   	// Setters & Getters : END
}

/**
 * <p>Objet qui stocke les données d'un modérateur.</p>
 *
 * @author Julien Ruffin
 */
class Moderateur extends Utilisateur {

	// Constructeurs : START
	/**
	 * <p>Crée un Modérateur, avec les valeurs assignées.</p>
	 *
	 * @param	login			Nom d'utilisateur, doit être unique, car c'est 
	 *							l'identifiant système (un String).
	 * @param	nom				Nom du modérateur (un String)
	 * @param	prenom			Prenom du modérateur (un String)
	 * @param	mot_de_passe	Mot de passe du modérateur (un String)
	 */
    Moderateur(String login, String nom, String prenom, String mot_de_passe) {
        super(login, nom, prenom, mot_de_passe);
    }
	// Constructeurs : END

	// Méthodes du design : START
	// Méthodes du design : END
	
	// Setters & Getters : START
   	// Setters & Getters : END
}
