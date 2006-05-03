package trollhammer;

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
	private HdVPanel hdv = null;
	//Panel Vente
	private VentePanel vente = null;
	//Panel Achat
	private AchatPanel achat = null;
	//Panel Valider MODO
	private ValiderPanel valider = null;
	//panel Planifier MODO
	private PlanifierPanel planifier = null;
	//Panel Gestion MODO
	private GestionPanel gestion = null;
	
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
			tabbedPane.addTab("Hotel des ventes", null, hdv.getComponent(), null);
			tabbedPane.addTab("Vente", null, vente.getComponent(), null);
			tabbedPane.addTab("Achat", null, achat.getComponent(), null);
			if(modo)
			{
				tabbedPane.addTab("Valider", null, valider.getComponent(), null);
				tabbedPane.addTab("Planifier", null, planifier.getComponent(), null);
				tabbedPane.addTab("Gestion des utilisateurs", null, gestion.getComponent(), null);
			}
		}
		return tabbedPane;
	}

	public void actionPerformed(ActionEvent event)
	{
		
		
	}
}
