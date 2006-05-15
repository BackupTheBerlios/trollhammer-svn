package trollhammer;
import java.util.Set;
import java.util.HashSet;

/**
 * <p>Interface Utilisateur du Serveur Trollhammer. C'est une interface en ligne
 * de commande uniquement (ie une CLI). Elle permet de forger n'importe quelle
 * message envoyé par le serveur, ainsi que ceux recu par celui-ci.</p>
 *
 * <p>Pour ajouter une nouvelle commande, il suffit de l'ajouter au tableau, je 
 * vous laisser prendre exemplesur les deux déjà fournies...</p>
 *
 * <p>IMPORTANT : A l'heure actuelle n'implemente pas une commande par message,
 * et par conséquent cette classe n'est pas terminée...</p>
 *
 * @author Lionel Sambuc, inspired by previous work by Julien Ruffin
 */

class CLIServeur extends CLI {
	public CLIServeur() {
		//En premier, quelques commandes déjà implémentée : 
		commandes.add(
			new CMD("nu", 5, "nu - Créé un nouvel Utilisateur.", "nu LOGIN NOM PRENOM MOTDEPASSE") {
				void apply(String parameters[]){
					Serveur.usermanager.addUtilisateur(new UtilisateurServeur(parameters[1], parameters[2], parameters[3], parameters[4]));
					Logger.log("CMD", 1, LogType.INF, "Utilisateur créé : " + parameters[1]);
				}
			}
		);
		commandes.add(
			new CMD("nm", 5, "nm - Créé un nouveau modérateur.", "nm LOGIN NOM PRENOM MOTDEPASSE") {
				void apply(String parameters[]){
					Serveur.usermanager.addUtilisateur(new ModerateurServeur(parameters[1], parameters[2], parameters[3], parameters[4]));
					Logger.log("CMD", 1, LogType.INF, "Modérateur créé : " + parameters[1]);
				}
			}
		);
/*		commandes.add(
			new CMD(, , , ) {
				void apply(String parameters[]){
					
				}
			}
		);
*/	
	}

}