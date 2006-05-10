package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Vector;

class ValiderPanel implements ActionListener
{
	private boolean modo = false;
	private CoolPanel pan = null;
	//Panel du haut
	private JPanel hautPanel = null;
	private JScrollPane jsp = null;
	//Panel du bas
	private CoolPanel basPanel = null;
	private JButton accepter = null;
	private JButton refuser = null;
	
	public ValiderPanel(boolean modo)
	{
		this.modo = modo;
	}
	private JComponent buildValiderPanel()
	{
		//Panel du haut
		hautPanel = new JPanel();
		hautPanel.setLayout(new BoxLayout(hautPanel, BoxLayout.Y_AXIS));
		jsp = new JScrollPane(hautPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBorder(BorderFactory.createEtchedBorder());
		//Panel du bas
		basPanel = new CoolPanel();
		basPanel.setLayout(new BoxLayout(basPanel, BoxLayout.X_AXIS));
		accepter = new JButton("Accepter");
        accepter.setActionCommand("accepter");
        accepter.addActionListener(this);
		basPanel.add(accepter);
		refuser = new JButton("Refuser");
        refuser.setActionCommand("refuser");
        refuser.addActionListener(this);
		basPanel.add(refuser);
		
		
		pan = new CoolPanel("pref:grow","fill:pref:grow, pref");
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS)); //ne me demandez pas pourquoi...
		pan.add(jsp, new CellConstraints(1,1));
		pan.add(basPanel, new CellConstraints(1,2));
		return pan;
	}
	public JComponent getComponent()
	{
		return buildValiderPanel();
	}
	public void actionPerformed(ActionEvent event)
	{
        String commande = event.getActionCommand();
        Logger.log("ValiderPanel", 2, commande);

        if(commande.equals("accepter")) {
            Objet o = null; // TODO : prendre l'objet sélectionné dans la liste
            Client.hi.accepterProposition(o.getId());
        } else if (commande.equals("refuser")) {
            Objet o = null; // TODO : prendre l'objet sélectionné dans la liste
            Client.hi.refuserProposition(o.getId());
        }
	}
}
