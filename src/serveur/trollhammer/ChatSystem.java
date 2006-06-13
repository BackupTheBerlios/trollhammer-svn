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
 * <p>Classe de gestion du chat. Encore très basique, elle est un sous-système 
 * à elle seule car elle pourrait se voir dotée de beaucoup plus de
 * fonctionnalités. Pour l'instant, elle se contente d'ordonner un broadcast des
 * messages de chat qui lui sont donnés.</p>
 *
 * @author	jruffin
 */
class ChatSystem {
	// Méthodes du design : START
	/**
	 * <p>Utilise le broadcaster pour envoyer à tous les utilisateurs connectés
	 * le message provenant du sender.</p>
	 *
	 * @param	message	message
	 * @param	sender	identifiant utilisateur
	 */
    public void envoyerChat(String message, String sender) {
        Logger.log("ChatSystem", 2, LogType.DBG, "[chat] " + sender
        	+ " dit : \"" + message + "\"");
        Serveur.broadcaster.chat(message, sender);
    }
	// Méthodes du design : END
}
