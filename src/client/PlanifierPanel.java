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
	//pan1
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
	
	//pan2
	private JScrollPane jspPan2 = null;
	private JPanel pan2 = null;
	
	//pan3
	private JPanel pan3 = null;
	private JButton add = null;
	private JButton remove = null;
	
	//pan4
	private JScrollPane jspPan4 = null;
	private JPanel pan4 = null;
	
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
		jspPan2 = new JScrollPane(pan2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//pan3
		add = new JButton(">>");
		add.setActionCommand("add");
		add.addActionListener(this);
		remove = new JButton("<<");
		remove.setActionCommand("remove");
		remove.addActionListener(this);
		pan3 = new JPanel();
		pan3.setLayout(new BorderLayout());
		pan3.add(add, BorderLayout.NORTH);
		pan3.add(remove, BorderLayout.SOUTH);
		
		//pan4
		jspPan4 = new JScrollPane(pan4, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
	}
	private JComponent buildPlanifierPanel()
	{
		layout = new FormLayout("pref, pref:grow, pref, pref:grow","fill:pref:grow");
		initComponents();
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.add(pan1, new CellConstraints(1,1));
		builder.add(jspPan2, new CellConstraints(2,1));
		builder.add(pan3, new CellConstraints(3,1));
		builder.add(jspPan4, new CellConstraints(4,1));
		
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