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
	private JPanel rightPanel = null;
	private CoolPanel leftPanel = null;
	private boolean modo = false;
	private String titre = null;
	private JTextPane titrePane = null;
	private JSplitPane splitPane = null;
	
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
		titrePane = new JTextPane();
		titrePane.setText(titre);
		
	}
	private JComponent buildVentePanel()
	{
		//Left Panel
		leftPanel = new CoolPanel("fill:pref:grow","pref, pref, pref, pref, pref, pref, pref, pref, pref, pref");
		leftPanel.addLabel("Proposer un\nobjet à vendre: ", new CellConstraints(1,1));
		
		//right Panel
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(titrePane);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		return splitPane;
	}
	public JComponent getComponent()
	{
		return buildVentePanel();
	}
	
	public void actionPerformed(ActionEvent event)
	{
		
		
	}
}