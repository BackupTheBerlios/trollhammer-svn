package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Vector;
import java.util.Set;

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
    private Vector<GestionUtilisateur> utilisateurs = null;
    private JList liste = null;
	
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
		gauchePanel.addC(loginField, new CellConstraints(2,3,2,1));
		gauchePanel.addLabel("Mot de passe: ", new CellConstraints(1,4));
		gauchePanel.addC(passwdField, new CellConstraints(2,4,2,1));
		gauchePanel.addLabel("Nom: ", new CellConstraints(1,5));
		gauchePanel.addC(nomField, new CellConstraints(2,5,2,1));
		gauchePanel.addLabel("Prénom; ", new CellConstraints(1,6));
		gauchePanel.addC(prenomField, new CellConstraints(2,6,2,1));
		gauchePanel.addC(ajouter, new CellConstraints(1,8));
		gauchePanel.addC(supprimer, new CellConstraints(2,8));
		gauchePanel.addC(bannir, new CellConstraints(3,8));
		
		//DROITE
		droitePanel = new FreshPanel('y',false);
		
		droitePane = new JScrollPane(droitePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		droitePane.setBorder(BorderFactory.createEtchedBorder());

        liste = new JList();
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.setCellRenderer(new ListCellRenderer() {
                // This is the only method defined by ListCellRenderer.
                // We just reconfigure the JLabel each time we're called.

                public Component getListCellRendererComponent(
                    JList list,
                    Object value,            // value to display
                    int index,               // cell index
                    boolean isSelected,      // is the cell selected
                    boolean cellHasFocus)    // the list and the cell have the focus
                {
                    ((GestionUtilisateur) value).selectionne(isSelected);
                return (GestionUtilisateur) value;
                }
                });

        // gérer l'affichage des propriétés de l'Utilisateur à sa sélection
        liste.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                // implicitement, la sélection est toujours de taille un,
                // donc on peut ne prendre que l'index du début de la sélection
                int index = liste.getSelectedIndex(); 
                if(utilisateurs.size() > 0) {
                    GestionUtilisateur u = utilisateurs.get(index);
                    if(u != GestionUtilisateur.nouvel_utilisateur)
                    Client.hi.choisirUtilisateur(utilisateurs.get(index).getLogin());
                } else if(index == 0 && utilisateurs.get
            }
        });

        droitePane.setViewportView(liste);
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

    void affichageListeUtilisateurs(Set<Utilisateur> ul) {
        utilisateurs = new Vector<GestionUtilisateur>();
        // le fameux 'nouvel utilisateur' en tête de liste
        utilisateurs.add(GestionUtilisateur.nouvel_utilisateur);

        for(Utilisateur u : ul) {
            GestionUtilisateur gu = null;
            if(u instanceof Moderateur) {
                gu = new GestionModerateur((Moderateur) u);
            } else {
                gu = new GestionUtilisateur(u);
            }
            utilisateurs.add(gu);
        }

        liste.setListData(utilisateurs);
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                droitePane.validate();
                droitePane.repaint();
                liste.validate();
                liste.repaint();
            }
        });
    }

    void affichageUtilisateur(Utilisateur i) {
        loginField.setText(i.getLogin());
        passwdField.setText(i.getMotDePasse());
        nomField.setText(i.getNom());
        prenomField.setText(i.getPrenom());
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
