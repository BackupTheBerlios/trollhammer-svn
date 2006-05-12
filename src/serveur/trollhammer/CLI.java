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

class CLI {
	abstract class CMD {
		String nom;
		int nb = 0;
		String params[];
		String helpSum = "default message.";
		String helpSyntax = "none";

		abstract void apply(String parameters[]);
		
		String helpStr() {
			return this.helpSum + "\n\t\tSyntax : " + this.helpSyntax;
		}
		
		void helpPrint() {
			Logger.log("CLI", 1, LogType.INF, "[help] " + this.helpSum + "\n\t\tSyntax : " + this.helpSyntax);
		}
		
		public CMD(String nom, int nb, String hsum, String hsyn) {
			this.nb = nb;
			this.nom = nom;
			this.helpSum = hsum;
			this.helpSyntax = hsyn;
		} 
	}
	
	
	Set<CMD> commandes = new HashSet<CMD>();
	
	public void interprete() {
		String commande;
		String tokens[];
		try {
			java.io.BufferedReader lr = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
			do {
				commande = lr.readLine();
				tokens = commande.split("\\s");
				for(CMD c : commandes) {
					if (c.nom.equals(tokens[0])) {
						if (tokens.length == c.nb) {
							c.apply(tokens);
						} else {
							c.helpPrint();
						}
					}
				}
			} while(!(commande.equals("q") || commande.equals("Q")));
            lr.close();
        } catch (Exception e) {
            Logger.log("CLI", 0, LogType.ERR, "[sys] Exception sur stdin : " + e.getMessage());
        }
	}
	
	public CLI() {
		//En premier, quelques commandes déjà implémentée : 
		commandes.add(
			new CMD("help", 1, "help - affiche l'aide ainsi que toutes les commandes disponibles.", "help") {
				void apply(String parameters[]) {
					String msg = "Liste des commandes disponible : \n";
					for(CMD p : commandes) {
						if (p != this) {
							msg = msg + "\n\t" + p.helpStr();
						}
					}
					Logger.log("CLI", 1, LogType.INF, "[help] " + msg + "\n\tq ou Q - Quitte le serveur.\n\n[help] END.");
				}
			}
		);
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
			new CMD(, ) {
				String helpSum = ;
				String helpSyntax = ;
				void apply(String parameters[]){
					
				}
			}
		);
*/	
	}

}