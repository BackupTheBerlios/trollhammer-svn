package trollhammer;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

class LoginWindow implements ActionListener, KeyListener
{
	

	private JFrame loginFrame = null;  //  @jve:decl-index=0:visual-constraint="418,169"
	private JMenuBar menuBar = null;
	private JMenu fichierMenu = null;
	private JMenuItem exitMenuItem = null;
	private JMenu aideMenu = null;
	private JTextPane bienvenue = null;
	private String name = null;
	private String passwd = null;
    private String serveur = null;
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
        // ça c'est bon, on garde : on quitte juste le programme
        // si l'on ferme la fenêtre de Login.
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
		nomField.addKeyListener(this);
		passwordField = new JPasswordField();
		passwordField.addKeyListener(this);
		String[] data = {"localhost"};
		srvBox = new JComboBox(data);
		srvBox.setEditable(true);
		srvBox.addKeyListener(this);
		connectB = new JButton("Connecter");
		connectB.setActionCommand("connect");
		connectB.setToolTipText("Cliquez pour se connecter");
		connectB.addActionListener(this);
		RaZB = new JButton("RàZ");
		RaZB.setActionCommand("RaZ");
		RaZB.addActionListener(this);
		//insertion de l'image
		//imgLabel = new JLabel(new ImageIcon(img));
		
        // faire en sorte que tout le texte des champs soit sélectionné
        // quand on essaie de taper quelque chose dans le champ

        nomField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                nomField.selectAll();
            }
            public void focusLost(FocusEvent e) {
                nomField.setSelectionStart(0);
                nomField.setSelectionEnd(0);
            }
        });
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                passwordField.selectAll();
            }
            public void focusLost(FocusEvent e) {
                passwordField.setSelectionStart(0);
                passwordField.setSelectionEnd(0);
            }
        });

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
            serveur = (String) srvBox.getSelectedItem();
            // ajouter le serveur utilisé pour la connexion dans la liste !
            // (en l'enlevant d'abord pour être sûr de ne pas le dupliquer)
            // on le met au début de la liste, ça fait 'historique'
            Object selected = srvBox.getSelectedItem();
            srvBox.removeItem(selected);
            srvBox.insertItemAt(selected, 0);
            srvBox.setSelectedItem(selected);

            Client.hi.connecter(name, passwd, serveur);
            
		}
		else if(event.getActionCommand().equals("RaZ"))
		{
			Logger.log("LoginWindow", 2, "mouhahaha ultime remise à ZERO!!!");
			nomField.setText("");
			passwordField.setText("");
			
		}
		
	}
	public void keyPressed(KeyEvent e)
	{
		//Logger.log("LoginWindow",2,"KeyEvent!!!\n\t"+e.getKeyCode()+" =? "+KeyEvent.VK_ENTER+" (Enter)\n\t"+e.getKeyCode()+" =? "+KeyEvent.VK_ESCAPE+" (Esc)");
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			connectB.doClick();
		else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			RaZB.doClick();
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}

    void setVisible(boolean visible) {
        this.loginFrame.setVisible(visible);
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

            final LoginWindow w = this;
	    //mais Julien c'est moche!!
	    /*
            getFichierMenu().add(new JMenuItem(
                        new javax.swing.AbstractAction("Quitter"){
                public void actionPerformed(ActionEvent e) {
                    w.setVisible(false);
                    System.exit(0);
                }
            }));
	    */
		}
		return menuBar;
	}

	/**
	 * This method initializes fichierMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFichierMenu() {
		final LoginWindow w = this;
		if (fichierMenu == null) {
			fichierMenu = new JMenu();
			fichierMenu.setText("Fichier");
			exitMenuItem = new JMenuItem(new javax.swing.AbstractAction("Quitter") {
				public void actionPerformed(ActionEvent e)
				{
		                        w.setVisible(false);
					System.exit(0);
				}});
			exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
			fichierMenu.add(exitMenuItem);
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

    JFrame getFrame() {
        return loginFrame;
    }

    void messageErreur(Erreur e) {
        switch (e) {
            case Deconnecte:
                JOptionPane.showMessageDialog(this.loginFrame,
                        "Erreur : Déconnecté.");
                break;
            case Invalide:
                JOptionPane.showMessageDialog(this.loginFrame,
                        "Erreur : Invalide.");
                break;
            case Banni:
                JOptionPane.showMessageDialog(this.loginFrame,
                        "Erreur : Vous avez été banni de ce serveur.");
                break;
            case ExisteDeja:
                JOptionPane.showMessageDialog(this.loginFrame,
                        "Erreur : Existe déjà.");
                break;
            case NonTrouve:
                JOptionPane.showMessageDialog(this.loginFrame,
                        "Erreur : Non trouvé.");
                break;
            case DejaEffectue:
                // pas sensé arriver ?
                JOptionPane.showMessageDialog(this.loginFrame,
                        "Erreur : Déjà effectué.");
                break;
        }
    }
}
