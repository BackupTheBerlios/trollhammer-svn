package trollhammer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

class LoginWindow implements ActionListener {
	

	private JFrame loginFrame = null;  //  @jve:decl-index=0:visual-constraint="418,169"
	private JMenuBar menuBar = null;
	private JMenu fichierMenu = null;
	private JMenu aideMenu = null;
	private JTextPane bienvenue = null;
	private String name = null;
	private String passwd = null;
	private boolean modo = false;
	private JTextField nomField = null;
	private JPasswordField passwordField = null;
	private JComboBox srvBox = null;
	private JButton connectB = null;
	private JButton RaZB = null;
	private FormLayout layout = null;
	private JLabel imgLabel = null;
	//private Image img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(System.getProperty("user.dir") + "/ressources/images/hammer.gif"));//à changer pour System.getProperty("user.dir") + ".../hammer.gif"
	public LoginWindow()
	{
		if (loginFrame == null)
			loginFrame = new JFrame();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		loginFrame.setBounds(dim.width/2-150,dim.height/2-100,300,200);
		loginFrame.setTitle("Trollhammer login");
		loginFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		loginFrame.setJMenuBar(getMenuBar());
		loginFrame.add(buildPanel());
		loginFrame.setVisible(true);
		
	}
	
	private void initComponents()
	{
		bienvenue = new JTextPane();
		bienvenue.setText("Veuillez s'il vous plait vous identifier.");
		nomField = new JTextField();
		passwordField = new JPasswordField();
		String[] data = {"one","two","tree"};
		srvBox = new JComboBox(data);
		srvBox.setEditable(true);
		connectB = new JButton("Connecter");
		connectB.setActionCommand("connect");
		connectB.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);
		connectB.setToolTipText("Cliquez pour se connecter");
		connectB.addActionListener(this);
		RaZB = new JButton("RàZ");
		RaZB.setActionCommand("RaZ");
		RaZB.addActionListener(this);
		//insertion de l'image
		//imgLabel = new JLabel(new ImageIcon(img));
		
	}
	private JComponent buildPanel()
	{
		initComponents();
		layout = new FormLayout(
				"10dlu, pref, pref:grow, pref:grow, 10dlu",	//3cols
				"pref, pref, pref, pref, pref, pref");	//6rows
				
		//col 4 & 6: equal widths
		layout.setColumnGroups(new int[][]{{2,3}});
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Veuillez s'il vous plait vous identifier.", cc.xyw(2,1,3));
		builder.addLabel("Nom: ", cc.xy(2,2));
		builder.add(nomField, cc.xyw(3,2,2));
		builder.addLabel("Password: ", cc.xy(2,3));
		builder.add(passwordField, cc.xyw(3,3,2));
		builder.addLabel("Serveur: ", cc.xy(2,4));
		builder.add(srvBox, cc.xyw(3,4,2));
		builder.add(connectB, cc.xy(3,5));
		builder.add(RaZB, cc.xy(4,5));
		//builder.add(imgLabel, cc.xy(4,6));
		
		return builder.getPanel();
	}
	public void actionPerformed(ActionEvent event)
	{
		Logger.log("LoginWindow", 2, event.getActionCommand());
		if(event.getActionCommand().equals("connect"))
		{
			Logger.log("LoginWindow", 2, "haha tu essaie de te connecter!!");
			name = nomField.getText();
			passwd = String.copyValueOf(passwordField.getPassword());
			modo = false;
			if(name.equals("modo"))
				modo = true;
			//Logger.log("LoginWindow", 2, "modo == "+modo);
			//Logger.log("LoginWindow", 2, "passwd: "+passwd);
			SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					new Window(modo);
				}
			});
			loginFrame.setVisible(false);
			
		}
		else if(event.getActionCommand().equals("RaZ"))
		{
			Logger.log("LoginWindow", 2, "mouhahaha utlime remise à ZERO!!!");
			nomField.setText("");
			passwordField.setText("");
			
		}
		
	}
	
	/**
	 * This method initializes menuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getFichierMenu());
			menuBar.add(getAideMenu());
		}
		return menuBar;
	}

	/**
	 * This method initializes fichierMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFichierMenu() {
		if (fichierMenu == null) {
			fichierMenu = new JMenu();
			fichierMenu.setText("Fichier");
		}
		return fichierMenu;
	}

	/**
	 * This method initializes aideMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getAideMenu() {
		if (aideMenu == null) {
			aideMenu = new JMenu();
			aideMenu.setText("?");
		}
		return aideMenu;
	}
}
