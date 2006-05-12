package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Vector;
import java.text.SimpleDateFormat;

class PlanifierPanel implements ActionListener
{
	private FormLayout layout = null;
	private boolean modo = false;
	private boolean modeAuto = true;
	//pan1 champs d'une vente
	private String titrePan1 = "";
	private CoolPanel pan1 = null;
	private JComboBox nomBox = null;
	private JRadioButton manuel = null;
	private JRadioButton auto = null;
	private ButtonGroup butGroup = null;
	private JTextField ouvDate = null;
	private JTextField ouvHeure = null;
	private JTextField lanceDate = null;
	private JTextField lanceHeure = null;
	private SimpleDateFormat dateFormat = null;
	private JFormattedTextField ouvertureFTF = null;
	private JScrollPane descrPane = null;
	private JTextArea descrArea = null;
	private JButton nouveau = null;
	private JButton valider = null;
	private JButton supprimer = null;
	
	//pan2 objets accepte
	private JScrollPane jspPan2 = null;
	private FreshPanel pan2 = null;
	
	//pan3 boutons ajouter enlever des objets
	private FreshPanel pan3 = null;
	private CoolPanel internPan3 = null;
	private JButton add = null;
	private JButton remove = null;
	
	//pan4 objets en vente
	private JScrollPane jspPan4 = null;
	private FreshPanel pan4 = null;
	
	//pan5 boutons de gestions du placement des objets en vente
	private FreshPanel pan5 = null;
	private CoolPanel internPan5 = null;
	private JButton up = null;		//déplace un Objet vers le haut
	private JButton down= null;		//déplace un Objet vers le bas
	private JButton top= null;	//déplace un Objet en tête de liste
	private JButton bottom= null;	//déplace un Objet en fin de liste
	
	
	public PlanifierPanel(boolean modo)
	{
		this.modo = modo;
	}
	private void initComponents()
	{
		//pan1 
		titrePan1 = "Planifier une nouveau vente: ";
		nomBox = new JComboBox();
		nomBox.setEditable(true);
		modeAuto = true;
		auto = new JRadioButton("Automatique");
		auto.setSelected(true);
		auto.setActionCommand("auto");
		auto.addActionListener(this);
		manuel = new JRadioButton("Manuel");
		manuel.setActionCommand("manuel");
		manuel.addActionListener(this);
		butGroup = new ButtonGroup();
		butGroup.add(auto);
		butGroup.add(manuel);
		dateFormat = new SimpleDateFormat("dd.MM.yyyy' 'HH:mm");
		ouvertureFTF = new JFormattedTextField(dateFormat);
		ouvertureFTF.setColumns(16);
		ouvertureFTF.setInputVerifier(new InputVerifier() {
			public boolean verify(JComponent input) {
				if (!(input instanceof JFormattedTextField))
					return true; // give up focus
				return ((JFormattedTextField) input).isEditValid();
			}
		});
		ouvDate = new JTextField();
		ouvHeure = new JTextField();
		lanceDate = new JTextField();
		lanceHeure = new JTextField();
		descrArea = new JTextArea();
		descrArea.setRows(10);
		descrArea.setColumns(16);
		descrArea.setLineWrap(true);
		descrArea.setWrapStyleWord(true);
		descrPane = new JScrollPane(descrArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		nouveau = new JButton("Nouveau"); //va falloir faire qu'on puisse le modifier si on séléctionne une vente déjà existante
		nouveau.setActionCommand("new");
		nouveau.addActionListener(this);
		valider = new JButton("Valider");
		valider.setActionCommand("ok");
		valider.addActionListener(this);
		supprimer = new JButton("Supprimer");
		supprimer.setActionCommand("del");
		supprimer.addActionListener(this);
		
		pan1 = new CoolPanel("pref:grow, pref:grow, pref:grow","pref, 3dlu, pref, pref, pref, pref, pref, pref, pref, 5dlu, pref");
		pan1.addLabel(titrePan1, new CellConstraints(1,1,3,1));
		pan1.addLabel("Nom: ", new CellConstraints(1,3));
		pan1.add(nomBox, new CellConstraints(2,3,2,1));
		pan1.addLabel("Mode: ", new CellConstraints(1,4));
		pan1.add(auto, new CellConstraints(2,4,2,1));
		pan1.add(manuel, new CellConstraints(2,5,2,1));
		pan1.addLabel("Ouverture: ", new CellConstraints(1,6));
		//pan1.add(ouvDate, new CellConstraints(3,6));
		//pan1.add(ouvHeure, new CellConstraints(5,6));
		pan1.add(ouvertureFTF, new CellConstraints(2,6,2,1));
		pan1.addLabel("Lancement: ", new CellConstraints(1,7));
		pan1.add(lanceDate, new CellConstraints(2,7));
		pan1.add(lanceHeure, new CellConstraints(3,7));
		pan1.addLabel("Description: ", new CellConstraints(1,8,3,1));
		pan1.add(descrPane, new CellConstraints(1,9,3,1));
		pan1.add(nouveau, new CellConstraints(1,11));
		pan1.add(valider, new CellConstraints(2,11));
		pan1.add(supprimer, new CellConstraints(3,11));
		
		
		
		//pan2
		jspPan2 = new JScrollPane(pan2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//pan3
		add = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/add.png"));
		add.setActionCommand("add");
		add.addActionListener(this);
		remove = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/remove.png"));
		remove.setActionCommand("remove");
		remove.addActionListener(this);
		pan3 = new FreshPanel('y',false);
		internPan3 = new CoolPanel("pref:grow,pref,pref:grow","pref:grow,pref,pref:grow");
		pan3.setLayout(new BorderLayout());
		internPan3.add(add, new CellConstraints(2,2));
		internPan3.add(remove, new CellConstraints(2,3));
		pan3.add(internPan3, BorderLayout.CENTER);
		
		//pan4
		jspPan4 = new JScrollPane(pan4, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//pan5
		up = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/up.png"));
		down = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/down.png"));
		top = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/top.png"));
		bottom = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/bottom.png"));
		internPan5 = new CoolPanel("pref:grow,pref,pref:grow","pref:grow,pref,pref,pref,pref,pref:grow");
		pan5 = new FreshPanel('y',true);
		pan5.add(top, new CellConstraints(2,2));
		pan5.add(up, new CellConstraints(2,3));
		pan5.add(down, new CellConstraints(2,4));
		pan5.add(bottom, new CellConstraints(2,5));
	}
	private JComponent buildPlanifierPanel()
	{
		layout = new FormLayout("fill:pref, pref:grow, pref, pref:grow,pref","fill:pref:grow");
		initComponents();
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.add(pan1, new CellConstraints(1,1));
		builder.add(jspPan2, new CellConstraints(2,1));
		builder.add(pan3, new CellConstraints(3,1));
		builder.add(jspPan4, new CellConstraints(4,1));
		builder.add(pan5, new CellConstraints(5,1));
		
		return builder.getPanel();
	}
	public JComponent getComponent()
	{
		return buildPlanifierPanel();
	}
	public void actionPerformed(ActionEvent event)
	{
		if(event.getActionCommand().equals("new"))
		{
			
		}
		else if(event.getActionCommand().equals("ok"))
		{
			
		}
		else if(event.getActionCommand().equals("del"))
		{
			
		}
		else if(event.getActionCommand().equals("add"))
		{
			
		}
		else if(event.getActionCommand().equals("remove"))
		{
			
		}
		else if(event.getActionCommand().equals("auto"))
		{
			modeAuto = true;
		}
		else if(event.getActionCommand().equals("manuel"))
		{
			modeAuto = false;
		}
		
	}
}