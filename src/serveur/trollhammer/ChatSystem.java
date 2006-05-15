package trollhammer;

/**
 * Classe de gestion du chat.
 * Encore très basique, elle est un sous-système à elle seule car elle pourrait
 * se voir dotée de beaucoup plus de fonctionnalités. Pour l'instant, elle se
 * contente d'ordonner un broadcast des messages de chat qui lui sont donnés.
 *
 * @author	jruffin
 */
class ChatSystem {

	/**
	 * Utilise le broadcaster pour envoyer à tous les utilisateurs connectés le
	 * message provenant du sender.
	 *
	 * @param	message	message
	 * @param	sender	identifiant utilisateur
	 */
    void envoyerChat(String message, String sender) {
        Logger.log("ChatSystem", 2, LogType.DBG, "[chat] " + sender
        	+ " dit : \"" + message + "\"");
        Serveur.broadcaster.chat(message, sender);
    }

}
