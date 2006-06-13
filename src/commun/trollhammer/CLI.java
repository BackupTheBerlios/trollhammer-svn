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

import java.util.Set;
import java.util.HashSet;

/**
 * <p>Interface Utilisateur de Trollhammer. C'est une interface en ligne
 * de commande uniquement (ie une CLI). Elle permet de forger n'importe quel
 * message.</p>
 *
 * <p>Pour ajouter une nouvelle commande, il suffit de l'ajouter au tableau, je 
 * vous laisser prendre exemple sur les deux déjà fournies...</p>
 *
 * <p>IMPORTANT : A l'heure actuelle n'implemente pas une commande par message,
 * et par conséquent cette classe n'est pas terminée...</p>
 *
 * @author Lionel Sambuc, inspired by previous work by Julien Ruffin
 */

class CLI {
	/**
	 * <p>Modélise une commande pour les composant de TrollHammer.</p>
	 *
	 * @author Lionel Sambuc
	 */
	abstract class CMD {
		private String nom;
		private int nb = 0;
		private String helpSum = "default message.";
		private String helpSyntax = "none";

		/**
		 * <p>Fonction à exécuter lors de l'appel de la commande.</p>
		 */
		public abstract void apply(String parameters[]);

		/**
		 * <p>Renvoie la chaine d'aide, au lieu de l'afficher.</p>
		 */
		public String helpStr() {
			return this.helpSum + "\n\t\tSyntax : " + this.helpSyntax;
		}
		
		/**
		 * <p>Affiche l'aide de la commande à l'aide de Logger.log.</p>
		 *
		 * @see Logger
		 */
		public void helpPrint() {
			Logger.log("CLI", 0, LogType.INF, "[help] " + this.helpSum + "\n\t\tSyntax : " + this.helpSyntax);
		}
		
		/**
		 * <p>Constructeur de CMD.</p>
		 *
		 * @param nom	Nom de la commande, ie la chaine de caractère à taper
		 *				pour l'invoquer.
		 * @param nb	Nombre d'argument de la commande (minimum 1, ie le nom
		 *				de celle-ci).
		 * @param hsum	Texte de l'aide (expliquation)
		 * @param hsyn	Texte de l'aide, syntax de la commande, avec description
		 *				des paramètres si nécessaire.
		 */
		public CMD(String nom, int nb, String hsum, String hsyn) {
			this.nb = nb;
			this.nom = nom;
			this.helpSum = hsum;
			this.helpSyntax = hsyn;
		} 
	}
	
	
	protected Set<CMD> commandes = new HashSet<CMD>();
	protected boolean printPrompt = true;
	
	/**
	 * <p>Démarre l'interprétation des commandes tapées sur STDIN.</p>
	 */
	public void interprete() {
		String commande;
		String tokens[];
		boolean known;
		if (printPrompt) {
			Logger.logwln("CLI", 0, LogType.INF, "Trollhammer> ");
		}
		try {
			java.io.BufferedReader lr = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
			do {
				known = false;
				commande = lr.readLine();
				tokens = commande.split("\\s");
				for(CMD c : commandes) {
					if (c.nom.equals(tokens[0])) {
						known = true;
						if (tokens.length == c.nb) {
							c.apply(tokens);
						} else {
							c.helpPrint();
						}
					}
				}
				if (!known && tokens[0].length() != 0 && !(commande.equals("q") || commande.equals("Q"))) {
					Logger.log("CLI", 0, LogType.INF, "[help] Unknown command \"" + tokens[0] + "\". Enter \'help\' to see available commands.");
				}
				if (printPrompt && !(commande.equals("q") || commande.equals("Q"))) {
					Logger.logwln("CLI", 0, LogType.INF, "Trollhammer> ");
				}
			} while(!(commande.equals("q") || commande.equals("Q")));
            lr.close();
        } catch (Exception e) {
            Logger.log("CLI", 0, LogType.ERR, "[sys] Exception sur stdin : " + e.getMessage());
			this.interprete(); // On relance l'interpretation CLI
        }
	}
	
	public CLI() {
		this(true);
	}
	
	public CLI(boolean b) {
		//En premier, quelques commandes déjà implémentée :
		printPrompt = b; 
		commandes.add(
			new CMD("help", 1, "help - affiche l'aide ainsi que toutes les commandes disponibles.", "help") {
				public void apply(String parameters[]) {
					String msg = "Liste des commandes disponible : \n";
					for(CMD p : commandes) {
						if (p != this) {
							msg = msg + "\n\t" + p.helpStr();
						}
					}
					Logger.log("CLI", 0, LogType.INF, "[help] " + msg + "\n\tq ou Q - Quitte le serveur.\n\n[help] END.");
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