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

    ModerateurServeur(Moderateur m) {
        super(m);
    }

    ModerateurServeur(String login, String nom, String prenom, String motdepasse) {
        this(new Moderateur(login, nom, prenom, motdepasse));
    }

    /* doLogin() a été enlevée. Elle s'occupe désormais des deux classes.
     * il serait parfaitement possible de faire une version lourdement
     * paramétrisée de doLogin() avec en paramètres rajoutés les réponses à donner,
     * mais j'ai (jr) préféré faire en sorte qu'elle autodétecte le type et
     * adapte les réponses...
     */

    void disconnect() {
        Logger.log("ModérateurServeur", 0, "[logout] déconnexion : login "+u.getLogin());
        u.setStatut(StatutLogin.Deconnecte);
        this.session.kaboom();
        this.session = null;
        // jr : j'ai dû 'aplatir' l'appel et le splitter,
        // ce qui est différent du Design :
        // faire un setStatut() et ensuite un appel direct
        // à VenteManager.modoLeaving(), parce que faire
        // un setStatut(Déconnecté) qui va spécifiquement
        // prévenir le Manager est spécifique au Serveur.
        // setStatut() ne devrait pas l'être.
        // Oui, c'est une correction (minime?) à la volée.
        Serveur.ventemanager.modoLeaving(u.getLogin());
    }

}
