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
		super(true);
		commandes.add(
			new CMD("login", 4, "login : se connecter à un serveur.", "Syntaxe : login ADRESSE LOGIN MOTDEPASSE") {
				public void apply(String parameters[]){
					Client.hi.connecter(parameters[2], parameters[3], parameters[1]);
				}
			}
		);
		commandes.add(
			new CMD("logout", 1, "logout : se déconnecter du serveur.", "Ne prend pas d'arguments") {
				public void apply(String parameters[]){
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