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

/** Adaptateur Serveur pour un Modérateur. Hérite des propriétés
 *  de UtilisateurServeur, et redéfinit uniquement les méthodes
 *  spécifiques au Modérateur : dans ce cas précis, doLogin(mdp)
 *  et disconnect(). L'objet encapsulé est restreint au type Modérateur,
 *  via le constructeur.
 *
 *  @author Julien Ruffin
 */

class ModerateurServeur extends UtilisateurServeur {

	// Constructeurs : START
    ModerateurServeur(Moderateur m) {
        super(m);
    }
    ModerateurServeur(Moderateur m, SessionServeur s) {
        super(m, s);
    }

    ModerateurServeur(String login, String nom, String prenom, String motdepasse) {
        super(login, nom, prenom, motdepasse);
    }
	// Constructeurs : END

	// Méthodes du design : START
    /* doLogin() a été enlevée. Elle s'occupe désormais des deux classes.
     * Il serait parfaitement possible de faire une version lourdement
     * paramétrisée de doLogin() avec en paramètres rajoutés les réponses à donner,
     * mais j'ai (jr) préféré faire en sorte qu'elle autodétecte le type et
     * adapte les réponses...
     */

	/**
	 * <p>Déconnecte le modérateur.</p>
	 */
    public void disconnect() {
		//ls :  Vive l'OO, ca évite le copier-coller de code...
		super.disconnect();
        Serveur.ventemanager.modoLeaving(this.login);
    }
	// Méthodes du design : END
}
