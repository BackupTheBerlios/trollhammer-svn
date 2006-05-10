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
	//éléments du Panel de gauche
	private CoolPanel leftPanel = null;
	private JLabel imgLabel = null;
	private JButton parcourir = null;
	private JTextField objTitre = null;
	private JTextArea objDescr = null;
	private JScrollPane objDescrPane = null;
	private JTextField objPrix = null;
	private JButton proposer = null;
	private JButton raz = null;
	
	//éléments du Panel de droite
	private JScrollPane rightPane = null;
	private JPanel rightPanel = null;
	private String titre = null;
	private JLabel titreLabel = null;
	private Vector listObjets = null;
	
	//autres éléments
	private boolean modo = false;
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
		//éléments du Panel de gauche
		imgLabel = new JLabel();
		imgLabel.setPreferredSize(new Dimension(150,150));
		imgLabel.setBorder(BorderFactory.createEtchedBorder());
		parcourir = new JButton("Parcourir");
		objTitre = new JTextField();
		objDescr = new JTextArea();
		objDescr.setRows(7);
		objDescr.setLineWrap(true);
		objDescr.setWrapStyleWord(true);
		objDescrPane = new JScrollPane(objDescr, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		objPrix = new JTextField();
		objPrix.setHorizontalAlignment(JTextField.RIGHT);
		proposer = new JButton("Proposer");
		raz = new JButton("RàZ");
		//éléments du Panel de droite
		rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createEtchedBorder());
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		titreLabel = new JLabel(titre);
		rightPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
	}
	private JComponent buildVentePanel()
	{
		initComponents();
		//Left Panel
		leftPanel = new CoolPanel("pref, pref","pref, 100dlu, pref, pref, pref, pref, pref, pref, pref, pref");
		leftPanel.addLabel("Proposer un\nobjet à vendre: ", new CellConstraints(1,1,2,1));
		leftPanel.add(imgLabel, new CellConstraints(1,2,2,1));
		leftPanel.add(parcourir, new CellConstraints(1,3,2,1));
		leftPanel.addLabel("Titre: ", new CellConstraints(1,4,2,1));
		leftPanel.add(objTitre, new CellConstraints(1,5,2,1));
		leftPanel.addLabel("Description: ", new CellConstraints(1,6,2,1));
		leftPanel.add(objDescrPane, new CellConstraints(1,7,2,1));
		leftPanel.addLabel("Prix de base: ", new CellConstraints(1,8,2,1));
		leftPanel.add(objPrix, new CellConstraints(1,9,2,1));
		leftPanel.add(proposer, new CellConstraints(1,10));
		leftPanel.add(raz, new CellConstraints(2,10));
		
		//right Panel
		rightPanel.add(titreLabel);
		
		//autres éléments
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPane);
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