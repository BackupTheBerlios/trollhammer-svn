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