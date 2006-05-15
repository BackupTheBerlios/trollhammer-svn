package trollhammer;
import java.util.Set;
import java.util.HashSet;

/**
 * <p>Interface Utilisateur du Client Trollhammer. C'est une interface en ligne
 * de commande uniquement (ie une CLI). Elle permet de forger n'importe quelle
 * message envoyé par le client, ainsi que ceux recu par celui-ci.</p>
 *
 * <p>Pour ajouter une nouvelle commande, il suffit de l'ajouter au tableau, je 
 * vous laisser prendre exemplesur les deux déjà fournies...</p>
 *
 * <p>IMPORTANT : A l'heure actuelle n'implemente pas une commande par message,
 * et par conséquent cette classe n'est pas terminée...</p>
 *
 * @author Lionel Sambuc, inspired by previous work by Julien Ruffin
 */

class CLIClient extends CLI {
	public CLIClient() {
		commandes.add(
			new CMD("login", 4, "login : se connecter à un serveur.", "Syntaxe : login ADRESSE LOGIN MOTDEPASSE") {
				void apply(String parameters[]){
					Client.hi.connecter(parameters[2], parameters[3], parameters[1]);
				}
			}
		);
		commandes.add(
			new CMD("logout", 1, "logout : se déconnecter du serveur.", "Ne prend pas d'arguments") {
				void apply(String parameters[]){
					Client.hi.executer(Action.Deconnecter);
				}
			}
		);
		//En premier, quelques commandes déjà implémentée : 
/*		commandes.add(
			new CMD(, , , ) {
				void apply(String parameters[]){
					
				}
			}
		);
*/	
	}

}