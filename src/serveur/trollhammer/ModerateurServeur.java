package trollhammer;
import java.util.Set;

/** Adaptateur Serveur pour un Modérateur. Hérite des propriétés
 *  de UtilisateurServeur, et redéfinit uniquement les méthodes
 *  spécifiques au Modérateur : dans ce cas précis, doLogin(mdp)
 *  et disconnect(). L'objet encapsulé est restreint au type Modérateur,
 *  via le constructeur.
 *
 *  @author Julien Ruffin
 */

class ModerateurServeur extends UtilisateurServeur {

	// Constructeurs : START
    ModerateurServeur(Moderateur m) {
        super(m);
    }
    ModerateurServeur(Moderateur m, SessionServeur s) {
        super(m, s);
    }

    ModerateurServeur(String login, String nom, String prenom, String motdepasse) {
        super(login, nom, prenom, motdepasse);
    }
	// Constructeurs : END

	// Méthodes du design : START
    /* doLogin() a été enlevée. Elle s'occupe désormais des deux classes.
     * Il serait parfaitement possible de faire une version lourdement
     * paramétrisée de doLogin() avec en paramètres rajoutés les réponses à donner,
     * mais j'ai (jr) préféré faire en sorte qu'elle autodétecte le type et
     * adapte les réponses...
     */

	/**
	 * <p>Déconnecte le modérateur.</p>
	 */
    public void disconnect() {
		//ls :  Vive l'OO, ca évite le copier-coller de code...
		super.disconnect();
        Serveur.ventemanager.modoLeaving(this.login);
    }
	// Méthodes du design : END
}
