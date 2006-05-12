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
 * et par conséquent cette classe n'estz pas terminée...</p>
 *
 * @author Lionel Sambuc, inspired by previous work by Julien Ruffin
 */

class CLI {
	abstract class CMD {
		String nom;
		int nb = 0;
		String params[];

		abstract void apply(String parameters[]);
		
		void help() {
			helpStd("default message.", "none");
		}
		
		protected void helpStd(String msg, String msg2) {
			Logger.log("CLI", 1, LogType.INF, "[help] " + msg + "\nSyntax : " + msg2);
		}
		
		public CMD(String nom, int nb) {
			this.nb = nb;
			this.nom = nom;
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
							c.help();
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
			new CMD("help", 0) {
				void apply(String parameters[]){
						help();
				}
				void help() {
					String msg = "Liste des commandes disponible : \n";
					Logger.log("CLI", 1, LogType.INF, "[help] " + msg);
					for(CMD p : commandes) {
						if (p != this) {
							p.help();
						}
					}
					Logger.log("CLI", 1, LogType.INF, "[help] " + "q ou Q - Quitte le serveur.\n");
				}
			}
		);
		commandes.add(
			new CMD("nu", 5) {
				void apply(String parameters[]){
					Serveur.usermanager.addUtilisateur(new UtilisateurServeur(parameters[1], parameters[2], parameters[3], parameters[4]));
					Logger.log("CMD", 1, LogType.INF, "Utilisateur créé : " + parameters[1]);
				}
				void help(){
					this.helpStd("nu - Créé un nouvel Utilisateur.", "nu LOGIN NOM PRENOM MOTDEPASSE");
				}
			}
		);
		commandes.add(
			new CMD("nm", 5) {
				void apply(String parameters[]){
					Serveur.usermanager.addUtilisateur(new ModerateurServeur(parameters[1], parameters[2], parameters[3], parameters[4]));
					Logger.log("CMD", 1, LogType.INF, "Modérateur créé : " + parameters[1]);
				}
				void help(){
					this.helpStd("nm - Créé un nouveau modérateur.", "nm LOGIN NOM PRENOM MOTDEPASSE");
				}
			}
		);
/*		commandes.add(
			new CMD(, ) {
				void apply(String parameters[]){
					
				}
				void help(){
					
				}
			}
		);
*/	
	}

}