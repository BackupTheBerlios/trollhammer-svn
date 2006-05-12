package trollhammer;
/**
* Log les messages que le système désire envoyer, selon la méthode choisie.
* <p>Les messages ne seront affiché que si le niveau de logging choisi est
* supérieur ou égal à celui du message.</p>
*
* <p>Les niveaux actuellement défini, ainsi que leur utilisation : 
* <ul>
* <li>0 : Messages généraux, du type client démarre/stop ou erreurs critiques (ex. login refusé) [INF, ERR]</li>
* <li>1 : Messages plus précis quand à l'éxecution par exemple session établie, etc [INF, WRN]</li>
* <li>2 : Message de debug qui donne de l'information très précise et locale, par ex. messages reçu [DBG]</li>
* </ul></p>
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
	static private int level = 2; 
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
/**
* Log un message, avec en plus le type d'information que c'est.
* 
* <p>Log le String passé en paramètre, en indiquant l'expéditeur, si et 
* seulement si le niveau de logging et suffisant pour ce message.</p>
*
* @param who	String contenant le nom de la classe qui fait l'appel.
* @param lvl	Level minimum à partir duquel le message doit être loggé.
* @param lgt	Prend une valeur de l'énumeration LOGTYPE.
* @param msg	String qui est le message à logger.
*/
	static void log(String who, int lvl, LogType lgt, String msg) {
		if (lvl <= level) {
			// Indente le message suivant sa priorité
			String tmp = "";
			String slgt; 
			for (int i=lvl; i > 0; i--) {
				tmp += " ";
			}
			switch (lgt) {
				case INF : slgt = "INF"; break; 
				case ERR : slgt = "ERR"; break;
				case WRN : slgt = "WRN"; break;
				case DBG : slgt = "DBG"; break;
				default : slgt = "UKN";
			}
			System.out.println("[" + slgt + "] "+ tmp + "[" + who + "] " + msg);
		}	
	}
}

enum LogType { INF, ERR, WRN, DBG }
