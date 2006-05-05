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

    /* des différences avec UtilisateurServeur.doLogin() existent !
     * elles sont juste relativement subtiles (la réponse renvoyée p. ex.)
     */
    void doLogin(SessionServeur sess, String mdp) {
        String mot_de_passe = u.getMotDePasse();
        StatutLogin statut = u.getStatut();

        if(mdp == mot_de_passe && statut != StatutLogin.Banni) {
            Logger.log("ModerateurServeur", 0, "[login] login de Modérateur accepté : login "
                    +u.getLogin());
            sess.resultatLogin(StatutLogin.Connecte_Moderateur);
            // la session est valide, on la fixe pour le Modérateur
            this.session = sess;
            u.setStatut(StatutLogin.Connecte_Moderateur);
            Set<Participant> pl = Serveur.participantmanager.getParticipants();
            Logger.log("ModérateurServeur", 2, "[login] envoi de la liste des Participants connectés");
            sess.listeParticipants(pl);
            Logger.log("ModérateurServeur", 2, "[login] broadcast du login");
            Serveur.broadcaster.etatParticipant((Participant) u);
        } else if (mdp != mot_de_passe) {
            Logger.log("ModerateurServeur", 0, "[login] login Modérateur refusé, mauvais mot de passe : login "
                    +u.getLogin());
            sess.resultatLogin(StatutLogin.Invalide);
            u.setStatut(StatutLogin.Deconnecte);
            sess.kaboom();
        } else if (statut == StatutLogin.Banni) {
            Logger.log("ModerateurServeur", 0, "[login] login de Modérateur banni refusé : login "
                    +u.getLogin());
            sess.resultatLogin(StatutLogin.Banni);
            sess.kaboom();
        } else {
            // pas sensé arriver. on ignore...
            Logger.log("ModerateurServeur", 0, "[login] cas non-traité de login Modérateur : login "
                    +u.getLogin());
            sess.kaboom();
        }

    }

    void disconnect() {
        Logger.log("Modérateur", 0, "[logout] déconnexion : login "+u.getLogin());
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
