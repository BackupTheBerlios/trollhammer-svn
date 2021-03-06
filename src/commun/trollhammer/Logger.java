/*
 * TrollHammer - Auctions at home over the Internet !
 * Copyright (C) 2006 ZOG Team, contact information on trollhammer.berlios.de
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. *  * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details. *  * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package trollhammer;

/**
 * <p>Log les messages que le système désire envoyer, selon la méthode choisie.
 * Les messages ne seront affiché que si le niveau de logging choisi est
 * supérieur ou égal à celui du message.</p>
 *
 * <p>Les niveaux actuellement défini, ainsi que leur utilisation : 
 * <ul>
 * <li>0 : Messages généraux, du type client démarre/stop ou erreurs critiques (ex. login refusé) [INF, ERR]</li>
 * <li>1 : Messages plus précis quand à l'éxecution par exemple session établie, etc [INF, WRN]</li>
 * <li>2 : Message de debug qui donne de l'information très précise et locale, par ex. messages reçu [DBG]</li>
 * </ul></p>
 *
 * <p>Les types de messages (LogType) et leur signification : 
 * <ul>
 * <li>INF : INFo, message d'information.</li>
 * <li>ERR : ERReur, erreur critique pour le bon déroulement de TrollHammer.</li>
 * <li>WRN : WaRNing, erreur d'exécution, ou message inattendu, qui est
 *           simplement ignoré, sans créer de problème au niveau de Trollhammer.</li>
 * <li>DBG : DeBuG, message utilisé pour le debogage de TrollHammer.</li>
 * <li>NSP : Not SPecified, utilisé uniquement pour la version "courte" de Logger.log</li>
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
	// C'est ici que l'on choisi le lvl de logging
	static final private int level = 2; 
	static final private boolean [] what = {
		true, // Afficher les message de type INF?
		true, // Afficher les message de type ERR?
		true, // Afficher les message de type WRN?
		false, // Afficher les message de type DBG?
		false  // Afficher les message de type NSP?
	};
	
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
	public static void log(String who, int lvl, String msg) {
		log(who, lvl, LogType.NSP, msg);
	}
	/**
	 * Log un message, avec en plus le type d'information que c'est.
	 * 
	 * <p>Log le String passé en paramètre, en indiquant l'expéditeur, si et 
	 * seulement si le niveau de logging et suffisant pour ce message. Un retour
	 * a la ligne est automatiquement ajouté.</p>
	 *
	 * @param who	String contenant le nom de la classe qui fait l'appel.
	 * @param lvl	Level minimum à partir duquel le message doit être loggé.
	 * @param lgt	Prend une valeur de l'énumeration LOGTYPE.
	 * @param msg	String qui est le message à logger.
	 */
	public static void log(String who, int lvl, LogType lgt, String msg) {
		logwln(who, lvl, lgt, msg + "\n");
	}

	/**
	 * Log un message, avec en plus le type d'information que c'est.
	 * 
	 * <p>Log le String passé en paramètre, en indiquant l'expéditeur, si et 
	 * seulement si le niveau de logging et suffisant pour ce message. Aucun
	 * retour a la ligne n'est ajouté.</p>
	 *
	 * @param who	String contenant le nom de la classe qui fait l'appel.
	 * @param lvl	Level minimum à partir duquel le message doit être loggé.
	 * @param lgt	Prend une valeur de l'énumeration LOGTYPE.
	 * @param msg	String qui est le message à logger.
	 */
	public static void logwln(String who, int lvl, LogType lgt, String msg) {
		if (lvl <= level) {
			// Indente le message suivant sa priorité
			String tmp = "";
			String slgt;
			boolean print = true;
			for (int i=lvl; i > 0; i--) {
				tmp += " ";
			}
			switch (lgt) {
				case INF : slgt = "INF"; print = what[0]; break; 
				case ERR : slgt = "ERR"; print = what[1]; break;
				case WRN : slgt = "WRN"; print = what[2]; break;
				case DBG : slgt = "DBG"; print = what[3]; break;
				case NSP : slgt = "NSP"; print = what[4]; break;
				default : slgt = "UKN";
			}
			if (print) {
				System.out.print("[" + slgt + "] "+ tmp + "[" + who + "] " + msg);
			}
		}	
	}
}

enum LogType { INF, ERR, WRN, DBG, NSP }
