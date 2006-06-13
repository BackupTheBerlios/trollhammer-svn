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

public class HdVUser extends JRadioButton
{
	private String login;
	private boolean modo;
	private boolean sup;
	public HdVUser(String login, boolean modo, String selflogin)
	{
		super(login);
		this.modo = modo;
		this.login = login;
		if(modo)
		{
			try
			{
				this.setIcon(new ImageIcon(this.getClass().getResource("/ressources/img/modoIcon.gif")));
			}catch(NullPointerException e)
			{
				this.setIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/modoIcon.gif"));
			}
			this.setForeground(Color.BLUE);
			try
			{
				this.setSelectedIcon(new ImageIcon(this.getClass().getResource("/ressources/img/modoIconSelected.gif")));
			}catch(NullPointerException e)
			{
				this.setSelectedIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/modoIconSelected.gif"));
			}
		}
		else
		{
			try
			{
				this.setIcon(new ImageIcon(this.getClass().getResource("/ressources/img/userIcon.gif")));
			}catch(NullPointerException e)
			{
			this.setIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/userIcon.gif"));
			}
			this.setForeground(Color.BLACK);
			try
			{
				this.setSelectedIcon(new ImageIcon(this.getClass().getResource("/ressources/img/userIconSelected.gif")));
			}catch(NullPointerException e)
			{
			this.setSelectedIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/userIconSelected.gif"));
			}
		}

        // mettre son nom en rouge non en gras !
        if(login.equals(selflogin)) {
            //this.setForeground(new Color(33,94,33));
            this.setForeground(Color.RED);
			//this.setFont(new Font(this.getFont().getFontName(),Font.BOLD,this.getFont().getSize()+1));
        }
		
	}
    /*
	public HdVUser(String login, boolean superviseur, boolean modo)
	{
		super(login);
		this.modo=modo;
		this.login=login;
		sup = superviseur;
		this.setIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/modoIcon.gif"));
		this.setSelectedIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/modoIconSelected.gif"));
		this.setForeground(new Color(217,217,25));	//Gold
			
	}
    */

    String getLogin() {
        return this.login;
    }
}
