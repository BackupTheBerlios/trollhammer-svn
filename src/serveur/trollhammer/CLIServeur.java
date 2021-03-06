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
import java.util.Calendar;

/**
 * <p>Interface utilisateur du Serveur Trollhammer. C'est une interface en ligne
 * de commande uniquement (ie une CLI). Elle permet de forger n'importe quelle
 * message envoyé par le serveur, ainsi que ceux reçu par celui-ci.</p>
 *
 * <p>Pour ajouter une nouvelle commande, il suffit de l'ajouter au tableau, je 
 * vous laisser prendre exemple sur les deux déjà fournies...</p>
 *
 * <p>IMPORTANT : A l'heure actuelle n'implemente pas une commande par message,
 * et par conséquent cette classe n'est pas terminée...</p>
 *
 * @author Lionel Sambuc, inspired by previous work by Julien Ruffin
 */

class CLIServeur extends CLI {

	// Constructeurs : START
	CLIServeur(boolean b) {
		super(b);
		//En premier, quelques commandes déjà implémentées : 
		commandes.add(
			new CMD("nu", 5, "nu - Créé un nouvel Utilisateur.", "nu LOGIN NOM PRENOM MOTDEPASSE") {
				public void apply(String parameters[]){
					if (!Serveur.usermanager.addUtilisateur(new UtilisateurServeur(parameters[1], parameters[2], parameters[3], parameters[4]))) {
						Logger.log("CMD", 0, LogType.ERR, "Il existe déjà un utilisateur avec comme login : " + parameters[1]);
					} else {
						Logger.log("CMD", 0, LogType.INF, "Utilisateur créé : " + parameters[1]);
					}
				}
			}
		);
		commandes.add(
			new CMD("nm", 5, "nm - Créé un nouveau modérateur.", "nm LOGIN NOM PRENOM MOTDEPASSE") {
				public void apply(String parameters[]){
					if (!Serveur.usermanager.addUtilisateur(new ModerateurServeur(parameters[1], parameters[2], parameters[3], parameters[4]))) {
						Logger.log("CMD", 0, LogType.ERR, "Il existe déjà un modérateur avec comme login : " + parameters[1]);
					} else {
						Logger.log("CMD", 0, LogType.INF, "Modérateur créé : " + parameters[1]);
					}
				}
			}
		);
		commandes.add(
			new CMD("lv", 1, "lv - Liste la liste des ventes", "lv") {
				public void apply(String parameters[]) {
					String liste = "liste des ventes\n";
					Calendar cal = Calendar.getInstance();
					VenteServeur vte = Serveur.ventemanager.getVenteEnCours();
					if (vte != null) {
						cal.setTimeInMillis(vte.getDate());
						String maDate = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
						liste = liste + "\t" + maDate + " : " + vte.getId() + " - " + vte.getNom() + " <-- en cours !\n";
					}
					for (int vid : Serveur.ventemanager.getVIds()) {
						vte = Serveur.ventemanager.getVente(vid);
						cal.setTimeInMillis(vte.getDate());
						String maDate = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
						liste = liste + "\t" + maDate + " : " + vid + " - " + vte.getNom() + "\n";
					}
					Logger.log("CMD", 0, LogType.INF, liste);
				}
			}
		);
		commandes.add(
			new CMD("st", 2, "st - Store les données actuelles du serveur", "st FILENAME") {
				public void apply(String parameters[]){
					Serveur.serveur.saveState(parameters[1]);
				}
			}
		);
		commandes.add(
			new CMD("ld", 2, "ld - Charge les données depuis un fichier", "ld FILENAME") {
				public void apply(String parameters[]){
					Serveur.serveur.loadState(parameters[1]);
				}
			}
		);

		commandes.add(
			new CMD("set", 3 , "set - affecte certaines variables", "set var val") {
				public void apply(String parameters[]){
					if (parameters[1].equals("autoInterval")) {
						Serveur.ventemanager.setAutoInterval(new Integer(parameters[2]));
						Logger.log("CMD", 0, LogType.INF, "var "+parameters[1]+" set to val "+ Serveur.ventemanager.getAutoInterval());
					// d'autres variables ?
					} else {
						Logger.log("CMD", 0, LogType.ERR, "set: var invalide");
					}
				}
			}
		);

		commandes.add(
			new CMD("get", 2 , "get - récupère la valeur de certaines variables", "get var") {
				public void apply(String parameters[]){
					if (parameters[1].equals("autoInterval")) {
						Logger.log("CMD", 0, LogType.INF, "returned val "+ Serveur.ventemanager.getAutoInterval());
					// d'autres variables ?
					} else {
						Logger.log("CMD", 0, LogType.ERR, "get: var invalide");
					}
				}
			}
		);

/*		commandes.add(
			new CMD(, , , ) {
				public void apply(String parameters[]){
					
				}
			}
		);
*/	
	}
	// Constructeurs : END
}