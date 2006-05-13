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

    private Utilisateur utilisateur_selectionne = null;
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
		supprimer.setActionCommand("del");
		supprimer.addActionListener(this);
		bannir = new JButton("Bannir");
		bannir.setActionCommand("ban");
		bannir.addActionListener(this);
		
		gauchePanel.addLabel("Propriétés: ", new CellConstraints(1,1));
		gauchePanel.addLabel("Nom d'utilisateur ", new CellConstraints(1,3));
		gauchePanel.addC(loginField, new CellConstraints(2,3,2,1));
		gauchePanel.addLabel("Mot de passe ", new CellConstraints(1,4));
		gauchePanel.addC(passwdField, new CellConstraints(2,4,2,1));
		gauchePanel.addLabel("Nom ", new CellConstraints(1,5));
		gauchePanel.addC(nomField, new CellConstraints(2,5,2,1));
		gauchePanel.addLabel("Prénom ", new CellConstraints(1,6));
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
        final GestionPanel gp = this;
        liste.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                // implicitement, la sélection est toujours de taille un,
                // donc on peut ne prendre que l'index du début de la sélection
                int index = liste.getSelectedIndex(); 
                if(index > -1) { // index < 0 => invalide (-1 == non trouvé)
                    if(utilisateurs.size() > 0
                        && utilisateurs.get(index) !=
                        GestionUtilisateur.nouvel_utilisateur) {
                        GestionUtilisateur u = utilisateurs.get(index);
                        // mettre le bouton ajouter en mode 'modifier'
                        gp.boutonModifier();
                        Client.hi.choisirUtilisateur(utilisateurs.get(index).getLogin());
                    } else if(utilisateurs.get(index) == GestionUtilisateur.nouvel_utilisateur) {
                        // pas besoin d'aller farfouiller dans HI pour afficher
                        // le 'template' nouvel_utilisateur. Déclenchons directement
                        // l'affichage de celui-ci.
                        gp.razChamps();
                        gp.boutonAjouter();
                    }
                }
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
        utilisateur_selectionne = i;
        loginField.setText(i.getLogin());
        passwdField.setText(i.getMotDePasse());
        nomField.setText(i.getNom());
        prenomField.setText(i.getPrenom());

        // en fonction que l'utilisateur est banni ou pas.
        // le bouton 'Bannir' va se changer en 'Débannir'.
        if(utilisateur_selectionne.getStatut() == StatutLogin.Banni) {
            boutonDebannir();
        } else {
            boutonBannir();
        }
    }

	public void actionPerformed(ActionEvent event)
	{
        Logger.log("GestionPanel", 2, event.getActionCommand());
		if(event.getActionCommand().equals("add"))
		{
            Utilisateur u = new Utilisateur(loginField.getText(),
                    nomField.getText(), prenomField.getText(),
                    passwdField.getText());

            Client.hi.editerUtilisateur(Edition.Creer, u);
		}
		else if(event.getActionCommand().equals("mod"))
		{
            // le login n'est pas modifiable.
            //utilisateur_selectionne.setLogin(loginField.getText());
            utilisateur_selectionne.setNom(nomField.getText());
            utilisateur_selectionne.setPrenom(prenomField.getText());
            utilisateur_selectionne.setMotDePasse(passwdField.getText());

            Client.hi.editerUtilisateur(Edition.Modifier, utilisateur_selectionne);
		}
		else if(event.getActionCommand().equals("del"))
		{
            // houlà, c'est sérieux ça.
            Client.hi.editerUtilisateur(Edition.Supprimer, utilisateur_selectionne);
		}
		else if(event.getActionCommand().equals("ban"))
		{
            // jr : modif p.r. Design : kicker l'Utilisateur avant de le marquer
            // banni. En effet, être banni et connecté est contradictoire.
            // Gros kick seulement si l'utilisateur est actuellement connecté,
            // bien sûr.
            if(utilisateur_selectionne.getStatut() == StatutLogin.Connecte_Utilisateur
            || utilisateur_selectionne.getStatut() == StatutLogin.Connecte_Moderateur) {

                Client.hi.kicker(utilisateur_selectionne.getLogin());
            }
            utilisateur_selectionne.setStatut(StatutLogin.Banni);
            Client.hi.editerUtilisateur(Edition.Modifier, utilisateur_selectionne);

            // switch du bouton bannir sur 'débannir'
            boutonDebannir();
		} else if(event.getActionCommand().equals("unban"))
		{
            // unban, ou le salut du pénitent.
            utilisateur_selectionne.setStatut(StatutLogin.Deconnecte);
            Client.hi.editerUtilisateur(Edition.Modifier, utilisateur_selectionne);

            // switch du bouton débannir sur 'bannir'
            boutonBannir();
		}
		
	}

    void razChamps() {
        loginField.setText("");
        passwdField.setText("");
        nomField.setText("");
        prenomField.setText("");
    }

    void boutonModifier() {
        loginField.setEditable(false);
        ajouter.setText("Modifier");
        ajouter.setActionCommand("mod");
    }

    void boutonAjouter() {
        loginField.setEditable(true);
        ajouter.setText("Ajouter");
        ajouter.setActionCommand("add");
    }

    void boutonBannir() {
        bannir.setText("Bannir");
        bannir.setActionCommand("ban");
    }

    void boutonDebannir() {
        bannir.setText("Débannir");
        bannir.setActionCommand("unban");
    }
}
