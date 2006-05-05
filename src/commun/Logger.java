package trollhammer;
/**
* Log les messages que le système désire envoyer, selon la méthode choisie.
* <p>Les messages ne seront affiché que si le niveau de logging choisi est
* supérieur ou égal à celui du message.</p>
*
* <p>Exemple : <br>
* Si la classe <code>Test</code> essaie dans ca méthode <code>echo</code> 
* de faire l'appel suivant :<br>
* <code>Logger.log("Test", 1, "Test de logging...")</code></p>
*
* <p>Le message n'apparaîtera que si le niveau d'affichage choisi pour le
* système est supérieur ou égal à 1.</p>
*/
class Logger {
	/* C'est ici que l'on choisi le lvl de logging */
	static private int level = 2; /**
/**
* Log un message, avec l'expéditeur de celui-ci, suivant le level de logging.
* 
* <p>Log le String passé en paramètre, en indiquant l'expéditeur, si et 
* seulement si le niveau de logging et suffisant pour ce message.</p>
*
* @param who	String contenant le nom de la classe qui fait l'appel.
* @param lvl	Level minimum à partir duquel le message doit être loggé.
* @param msg	String qui est le message à logger.
*/
	static void log(String who, int lvl, String msg) {
		if (lvl <= level) {
			// Indente le message suivant sa priorité
			String tmp = "";
			for (int i=lvl; i > 0; i--) {
				tmp += " ";
			}
			System.out.println(tmp + "[" + who + "] " + msg);
		}	
	}
}
