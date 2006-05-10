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
	private CoolPanel rightPanel = null;
	private CoolPanel leftPanel = null;
	private boolean modo = false;
	private String titre = null;
	private JSplitFrame splitFrame = null;
	
	public VentePanel(boolean modo)
	{
		this.modo = modo;
		if(modo)
			titre = "Status des objets proposés: ";
		else
			titre = "Status de vos objets proposés: ";
	}
	private void initComponents()
	{
		
		
	}
	private JComponent buildVentePanel()
	{
		//Left Panel
		leftPanel = new CoolPanel("fill:pref:grow","pref, pref, pref, pref, pref, pref, pref, pref, pref, pref");
		leftPanel.addLabel("Proposer un\nobjet à vendre: ", new CellConstraints(1,1));
		
		//right Panel
		rightPanel = new CoolPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLAyout.Y_AXIS));
		rightPanel.addLabel(titre);
		splitFrame = new JSplitFrame(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		return splitFrame;
	}
	public JComponent getComponent()
	{
		return buildVentePanel();
	}
	
	public void actionPerformed(ActionEvent event)
	{
		
		
	}
}