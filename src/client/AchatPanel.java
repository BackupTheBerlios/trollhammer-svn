package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

class AchatPanel
{
	private boolean modo = false;
	private JScrollPane jsp = null;
	private FreshPanel pan = null;
	private JLabel titreLabel = null;
	private String titre = null;
	public AchatPanel(boolean modo)
	{
		this.modo = modo;
		if(modo)
			titre = "vous ";
	}
	private JComponent buildAchatPanel()
	{
		pan = new FreshPanel('y',false);
		titreLabel = new JLabel("Liste des objets qui "+titre+"ont été adjugés: ");
		pan.add(titreLabel);
		jsp = new JScrollPane(pan, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return jsp;
	}
	public JComponent getComponent()
	{
		return buildAchatPanel();
	}
}