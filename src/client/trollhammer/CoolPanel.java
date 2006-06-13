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
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

class CoolPanel extends JPanel
{
	private FormLayout layout = null;
	//private PanelBuilder builder = null;
	private CellConstraints cc = null;
	
	public CoolPanel()
	{
		//this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	public CoolPanel(String col, String row)
	{
		super();
		layout = new FormLayout(col,row);
		this.setLayout(layout);
		//builder = new PanelBuilder(layout);
		//builder.setDefaultDialogBorder();
		cc = new CellConstraints();
		//this.add(builder.getPanel());
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	public CoolPanel(String col, String row, Color bg)
	{
		super();
		layout = new FormLayout(col,row);
		this.setLayout(layout);
		//builder = new PanelBuilder(layout);
		//builder.setDefaultDialogBorder();
		cc = new CellConstraints();
		//this.add(builder.getPanel());
		this.setBackground(bg);
		this.setBorder(BorderFactory.createEtchedBorder());
	}
	public void addC(Component c, CellConstraints cc)
	{
		//builder.add(c,cc);
		//Logger.log("CoolPanel",2,"Kikoooooooo LooooooOOoOOoOOl :)))))))))");
		try
		{
			this.add(c,cc);
		} catch(Exception e) {Logger.log("ADD",2,"heu: "+e.getMessage());}
	}
	public void addLabel(String s, CellConstraints cc)
	{
		//builder.addLabel(s, cc);
		try
		{
			this.add(new JLabel(s),cc);
		} catch(Exception e) {Logger.log("ADDLABEL",2,"heu: "+e.getMessage());}
	}
	public void setRowGroups(int[][] g)
	{
		layout.setRowGroups(g);
	}
	public void setColumnGroups(int [][] g)
	{
		layout.setColumnGroups(g);
	}
}