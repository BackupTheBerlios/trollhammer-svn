

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Vector;

class Window implements ActionListener
{
	private boolean modo = false;
	private JFrame frame = null;  //  @jve:decl-index=0:visual-constraint="0,0"
	private JMenuBar menuBar = null;
	private JMenu fileMenu = null;
	private JMenu helpMenu = null;
	private JTabbedPane tabbedPane = null;
	//Panels de l'onglets HDV
	private FormLayout HDVLayout = null;
	private CoolPanel listeObjetsPanel = null;
	private CoolPanel infoAdjPanel = null;
	private JTextPane prixAdj = null;
	private JTextPane nbCoupDeMasse = null;
	private CoolPanel selectPanel = null;
	private JLabel imgLabel = null;
	private JScrollPane descrObjetPane = null;
	private JTextArea descrObjetTextArea = null;
	private String descrObjet = null;
	private CoolPanel sallePanel = null;
	private CoolPanel logPanel = null;
	private JScrollPane logPane = null; //inside Pane
	private String log = null;
	private JTextArea logArea = null;
	private CoolPanel adjPanel = null;
	private CoolPanel encherePanel = null;
	private JButton enchereButton = null;
	private CoolPanel chatPanel = null;
	private JTextField chatField = null;
	private JButton chatButton = null;
	private CoolPanel cmdPanel = null;
	private JButton logOutButton = null;
	private JButton cdmButton = null;
	private JButton kickButton = null;
	
	private byte nbCdM = 0;
	private double prixEnCours = 0.;
	private double prochaineEnchere = 0.;
	private Vector lstObjVect = null;
	
	//Panel Vente
	private FormLayout venteLayout = null;
	//Panel Achat
	private FormLayout achatLayout = null;
	//Panel Valider MODO
	private FormLayout validerLayout = null;
	//panel Planifier MODO
	private FormLayout planifierLayout = null;
	//Panel Gestion MODO
	private FormLayout gestionLayout = null;
	
	public Window(boolean modo)
	{
		this.modo = modo;
		getFrame();
	}


	/**
	 * This method initializes frame	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	private JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame("Trollhammer");
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setBounds(dim.width/2-400,dim.height/2-300,800,600);
			frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setJMenuBar(getMenuBar());
			frame.setContentPane(getTabbedPane());
		}
		return frame;
	}
	private void initHDVComponents()
	{
		//Liste des objets
		listeObjetsPanel = new CoolPanel();
		lstObjVect = new Vector(5,1);
		
		//Informations adjudications
		infoAdjPanel = new CoolPanel("pref:grow, pref","pref, pref");
		prixAdj = new JTextPane();
		prixAdj.setText("Prix d'adjudication: ");
		nbCoupDeMasse = new JTextPane();
		nbCoupDeMasse.setText("Nombres de coups de marteau: ");
		infoAdjPanel.add(prixAdj, new CellConstraints(1,1));
		infoAdjPanel.add(nbCoupDeMasse,new CellConstraints(1,2));
		
		//Informations sur l'objets séctionné
		selectPanel = new CoolPanel("left:pref","pref,pref,pref,fill:pref:grow");
		imgLabel = new JLabel("image non\ndisponible");
		imgLabel.setBorder(BorderFactory.createBevelBorder(0));
		descrObjet = "Bonjour je m'appelle Monsieur Pougnasse et j'aime les noix... pas vous??";
		descrObjetTextArea = new JTextArea(descrObjet,10,15);
		//descrObjetTextArea.setLineWrap(true);
		descrObjetPane = new JScrollPane(descrObjetTextArea);
		//System.out.println("before: "+descrObjetTextArea.getWrapStyleWord());
		descrObjetTextArea.setWrapStyleWord(true);
		//System.out.println("after: "+descrObjetTextArea.getWrapStyleWord());
		selectPanel.addLabel("Image: ", new CellConstraints(1,1));
		selectPanel.add(imgLabel, new CellConstraints(1,2));
		selectPanel.addLabel("Description: ", new CellConstraints(1,3));
		selectPanel.add(descrObjetPane, new CellConstraints(1,4));
		//Salle
		sallePanel = new CoolPanel();
		//Log
		logPanel = new CoolPanel("fill:pref","fill:pref:grow");
		logArea = new JTextArea(log,10,15);
		logPane = new JScrollPane(logArea);
		logPane.setWheelScrollingEnabled(true);
		logPanel.add(logPane, new CellConstraints(1,1));
		//Adjudication en cours
		adjPanel = new CoolPanel("pref","pref,pref");
		
		adjPanel.addLabel("Adjudication en cours: ", new CellConstraints(1,1));
		//enchère
		encherePanel = new CoolPanel("pref:grow,pref,pref","pref");
		enchereButton = new JButton("Enchérir!");
		encherePanel.addLabel("prochain prix d'adjudication: ", new CellConstraints(1,1));
		encherePanel.add(enchereButton, new CellConstraints(3,1));
		//Chat
		chatPanel = new CoolPanel("fill:pref","pref, pref");
		chatField = new JTextField(15);
		chatButton = new JButton("Envoyer");
		chatPanel.add(chatField, new CellConstraints(1,1));
		chatPanel.add(chatButton, new CellConstraints(1,2));
		//Panel des commandes
		cmdPanel = new CoolPanel("left:pref,pref,fill:pref:grow, right:pref","pref");
		logOutButton = new JButton("Déconnecter");
		cmdPanel.add(logOutButton, new CellConstraints(1,1));
		if(modo)
		{
			cdmButton = new JButton("Coup de Marteau");
			kickButton = new JButton("Expulser");
			cmdPanel.add(cdmButton, new CellConstraints(2,1));
			cmdPanel.add(kickButton, new CellConstraints(3,1));
		}
	}

	private JComponent buildHDVPanel()
	{
		initHDVComponents();
		HDVLayout = new FormLayout(
								"10dlu, pref, pref:grow, pref, 10dlu", //5cols
								"fill:pref, fill:pref, fill:pref, fill:pref:grow, fill:pref, fill:pref, fill:pref" //7rows
								);
		PanelBuilder builder = new PanelBuilder(HDVLayout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Liste des objets: ", cc.xyw(2,1,2));
		builder.addLabel("Informations adjudications: ", cc.xy(4,1));
		builder.add(listeObjetsPanel, cc.xyw(2,2,2));
		builder.add(infoAdjPanel, cc.xy(4,2));
		builder.addLabel("Information sur l'objet sélectionné: ", cc.xy(2,3));
		builder.addLabel("Salle: ", cc.xy(3,3));
		builder.addLabel("Log: ", cc.xy(4,3));
		builder.add(selectPanel, cc.xy(2,4));
		builder.add(sallePanel, cc.xy(3,4));
		builder.add(logPane, cc.xy(4,4));
		builder.addLabel("Chat: ", cc.xy(4,5));
		builder.add(adjPanel, cc.xy(2,6));
		builder.add(encherePanel, cc.xy(3,6));
		builder.add(chatPanel, cc.xy(4,6));
		builder.add(cmdPanel, cc.xyw(2,7,3));
		
		
		return builder.getPanel();
	}
	private JComponent buildVentePanel()
	{
		venteLayout = new FormLayout("","");
		
		PanelBuilder builder = new PanelBuilder(venteLayout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		
		return builder.getPanel();
	}
	private JComponent buildAchatPanel()
	{
		achatLayout = new FormLayout("","");
		
		PanelBuilder builder = new PanelBuilder(achatLayout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		
		return builder.getPanel();
	}
	private JComponent buildValiderPanel()
	{
		validerLayout = new FormLayout("","");
		
		PanelBuilder builder = new PanelBuilder(validerLayout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		
		return builder.getPanel();
	}
	private JComponent buildPlanifierPanel()
	{
		planifierLayout = new FormLayout("","");
		
		PanelBuilder builder = new PanelBuilder(planifierLayout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		
		return builder.getPanel();
	}
	private JComponent buildGestionPanel()
	{
		gestionLayout = new FormLayout("","");
		
		PanelBuilder builder = new PanelBuilder(gestionLayout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		
		return builder.getPanel();
	}
	
	
	
	
	/**
	 * This method initializes menuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getFileMenu());
			menuBar.add(getHelpMenu());
		}
		return menuBar;
	}

	/**
	 * This method initializes fileMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu("File");
		}
		return fileMenu;
	}
	/**
	 * This method initializes helpMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu("?");
		}
		return helpMenu;
	}

	/**
	 * This method initializes tabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Hotel des ventes", null, buildHDVPanel(), null);
			tabbedPane.addTab("Vente", null, buildVentePanel(), null);
			tabbedPane.addTab("Achat", null, buildAchatPanel(), null);
			if(modo)
			{
				tabbedPane.addTab("Valider", null, buildValiderPanel(), null);
				tabbedPane.addTab("Planifier", null, buildPlanifierPanel(), null);
				tabbedPane.addTab("Gestion des utilisateurs", null, buildGestionPanel(), null);
			}
		}
		return tabbedPane;
	}

	public void actionPerformed(ActionEvent event)
	{
		
		
	}

	private class CoolPanel extends JPanel
	{
		private FormLayout layout = null;
		private PanelBuilder builder = null;
		private CellConstraints cc = null;
		CoolPanel()
		{
			this.setBackground(Color.WHITE);
			this.setBorder(BorderFactory.createEtchedBorder());
		}
		CoolPanel(String col, String row)
		{
			layout = new FormLayout(col,row);
			builder = new PanelBuilder(layout);
			builder.setDefaultDialogBorder();
			cc = new CellConstraints();
			this.add(builder.getPanel());
			this.setBackground(Color.WHITE);
			this.setBorder(BorderFactory.createEtchedBorder());
		}
		public void add(Component c, CellConstraints cc)
		{
			builder.add(c,cc);
		}
		public void addLabel(String s, CellConstraints cc)
		{
			builder.addLabel(s, cc);
		}
	}
}
