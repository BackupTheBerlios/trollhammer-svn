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

import javax.swing.*;
import java.awt.*;

/* jr : remplacé par GestionUtilisateur. (mes excuses, Alex)
class GestionUser extends JPanel
{
	private String login;
    private String nom;
    private String prenom;
    private StatutLogin statut;
	private String mot_de_passe;
	
	public GestionUser(Utilisateur u)
	{
		super();
		login = u.getLogin();
		mot_de_passe = u.getMotDePasse();
		nom = u.getNom();
		prenom = u.getPrenom();
		statut = u.getStatut();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //à modifier quand il y aura l'image
		this.setBorder(BorderFactory.createEtchedBorder());
		this.add(new JLabel(login));
		this.add(new JLabel("("+nom+", "+prenom+")"));
	}
}*/
