package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Vector;

class VentePanel implements ActionListener
{
	private FormLayout layout = null;
	private boolean modo = false;
	public VentePanel(boolean modo)
	{
		this.modo = modo;
	}
	private JComponent buildVentePanel()
	{
		layout = new FormLayout("","");
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		
		return builder.getPanel();
	}
	public JComponent getComponent()
	{
		return buildVentePanel();
	}
	
	public void actionPerformed(ActionEvent event)
	{
		
		
	}
}