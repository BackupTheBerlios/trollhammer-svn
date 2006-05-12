package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Vector;

class GestionPanel implements ActionListener
{
	private FormLayout layout = null;
	private boolean modo = false;
	//panel de gauche
	private CoolPanel gauchePanel = null;
	private JTextField loginField = null;
	private JTextField passwdField = null;
	private JTextField nomField = null;
	private JTextField prenomField = null;
	private JButton ajouter = null;
	private JButton supprimer = null;
	private JButton bannir = null;
	//panel de droite
	private JScrollPane droitePane = null;
	private FreshPanel droitePanel = null;
	
	public GestionPanel(boolean modo)
	{
		this.modo = modo;
	}
	private void initComponents()
	{
		//GAUCHE
		gauchePanel = new CoolPanel("pref,pref,pref","pref, 10dlu, pref, pref, pref, pref, 10dlu, pref");
		loginField = new JTextField();
		passwdField = new JTextField();
		nomField = new JTextField();
		prenomField = new JTextField();
		ajouter = new JButton("Ajouter");
		ajouter.setActionCommand("add");
		ajouter.addActionListener(this);
		supprimer = new JButton("Supprimer");
		ajouter.setActionCommand("del");
		ajouter.addActionListener(this);
		bannir = new JButton("Bannir");
		ajouter.setActionCommand("bann");
		ajouter.addActionListener(this);
		
		gauchePanel.addLabel("Propriétés: ", new CellConstraints(1,1));
		gauchePanel.addLabel("Nom d'utilisateur: ", new CellConstraints(1,3));
		gauchePanel.add(loginField, new CellConstraints(2,3,2,1));
		gauchePanel.addLabel("Mot de passe: ", new CellConstraints(1,4));
		gauchePanel.add(passwdField, new CellConstraints(2,4,2,1));
		gauchePanel.addLabel("Nom: ", new CellConstraints(1,5));
		gauchePanel.add(nomField, new CellConstraints(2,5,2,1));
		gauchePanel.addLabel("Prénom; ", new CellConstraints(1,6));
		gauchePanel.add(prenomField, new CellConstraints(2,6,2,1));
		gauchePanel.add(ajouter, new CellConstraints(1,8));
		gauchePanel.add(supprimer, new CellConstraints(2,8));
		gauchePanel.add(bannir, new CellConstraints(3,8));
		
		//DROITE
		droitePanel = new FreshPanel('y',false);
		
		droitePane = new JScrollPane(droitePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		droitePane.setBorder(BorderFactory.createEtchedBorder());
	}
	private JComponent buildGestionPanel()
	{
		initComponents();
		layout = new FormLayout("pref,pref:grow","fill:pref:grow");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.add(gauchePanel, cc.xy(1,1));
		builder.add(droitePane, cc.xy(2,1));
		
		return builder.getPanel();
	}
	public JComponent getComponent()
	{
		return buildGestionPanel();
	}
	public void actionPerformed(ActionEvent event)
	{
		if(event.getActionCommand().equals("add"))
		{
			
		}
		else if(event.getActionCommand().equals("del"))
		{
			
		}
		else if(event.getActionCommand().equals("bann"))
		{
			
		}
		
	}
}