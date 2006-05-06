package trollhammer;

/**
 * Classe de gestion du chat.
 * Encore très basique, elle est un sous-système à elle seule car elle pourrait
 * se voir dotée de beaucoup plus de fonctionnalités. Pour l'instant, elle se
 * contente d'ordonner un broadcast des messages de chat qui lui sont donnés.
 * @author squelette : Julien Ruffin
 */
class ChatSystem {

    void envoyerChat(String message, String sender) {
        Logger.log("ChatSystem", 0, "[chat] "+sender+" dit : "+message);
        Serveur.broadcaster.chat(message, sender);
    }

}
