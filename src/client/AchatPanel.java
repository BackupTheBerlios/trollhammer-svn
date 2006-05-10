package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

class AchatPanel implements ActionListener
{
	private boolean modo = false;
	private JScrollPane jsp = null;
	private JPanel pan = null;
	private JLabel titreLabel = null;
	private String titre = null;
	public AchatPanel(boolean modo)
	{
		this.modo = modo;
		if(modo)
			titre = "Liste des objets qui ont été adjugés: ";
		else
			titre = "Liste des objets qui vous ont été adjugés: ";
	}
	private JComponent buildAchatPanel()
	{
		pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		titreLabel = new JLabel(titre);
		pan.add(titreLabel);
		jsp = new JScrollPane(pan, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return jsp;
	}
	public JComponent getComponent()
	{
		return buildAchatPanel();
	}
	
	public void actionPerformed(ActionEvent event)
	{
		
		
	}
}