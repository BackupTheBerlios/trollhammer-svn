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
 * <p>Thread de démarrage de Vente. Surveille les dates de démarrage des ventes
 * et démarre celle qui doit démarrer, c'est à dire la vente dont le temps de
 * début est dans le passé, et qui se trouve en tête de la liste des ventes.</p>
 *
 * @author Lionel Sambuc
 * @author squelette : Julien Ruffin
 */
class VenteStarter extends Thread {

    public void run() {
		while (true) {
			Logger.log("VenteStarter", 3, LogType.DBG, "Démarrage de vente!");
			Serveur.ventemanager.demarrerVente();
			Serveur.ventemanager.donnerCoupdeMASSE();
			try {
				Thread.sleep(1000); //dodo pendant une seconde.
			} catch (InterruptedException ie) {
				Logger.log("VenteStarter", 0, LogType.ERR, "[sys] Je peux pas dormir!!! car : " + ie);
			}
		}
    }
}
