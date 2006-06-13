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

class FreshPanel extends JPanel
{
	public FreshPanel(char align, boolean border)
	{
		switch(align)
		{
			case 'x': this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); break;
			case 'y': this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); break;
		}
		if(border)
			this.setBorder(BorderFactory.createEtchedBorder());
	}
	/*public void add(Component c, CellConstraints cc)
	{
		builder.add(c);
	}*/

}