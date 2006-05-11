package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Set;
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
	private FreshPanel rightPanel = null;
	private String titre = null;
	private JLabel titreLabel = null;
	
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
		parcourir.setActionCommand("browse");
		parcourir.addActionListener(this);
		objTitre = new JTextField();
		objDescr = new JTextArea();
		objDescr.setRows(7);
		objDescr.setLineWrap(true);
		objDescr.setWrapStyleWord(true);
		objDescrPane = new JScrollPane(objDescr, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		objPrix = new JTextField();
		objPrix.setHorizontalAlignment(JTextField.RIGHT);
		proposer = new JButton("Proposer");
        proposer.setActionCommand("proposer");
        proposer.addActionListener(this);
		raz = new JButton("RàZ");
        raz.setActionCommand("raz");
        raz.addActionListener(this);
		//éléments du Panel de droite
		rightPanel = new FreshPanel('y',false);
		titreLabel = new JLabel(titre);
		rightPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		rightPane.setBorder(BorderFactory.createEtchedBorder());
		
	}
	private JComponent buildVentePanel()
	{
		initComponents();
		//Left Panel
		leftPanel = new CoolPanel("pref, pref","pref, pref, 100dlu, pref, pref, pref, pref, pref, pref, pref, pref");
		leftPanel.addLabel("Proposer un", new CellConstraints(1,1,2,1));
		leftPanel.addLabel("objet à vendre: ", new CellConstraints(1,2,2,1));
		leftPanel.add(imgLabel, new CellConstraints(1,3,2,1));
		leftPanel.add(parcourir, new CellConstraints(1,4,2,1));
		leftPanel.addLabel("Titre: ", new CellConstraints(1,5,2,1));
		leftPanel.add(objTitre, new CellConstraints(1,6,2,1));
		leftPanel.addLabel("Description: ", new CellConstraints(1,7,2,1));
		leftPanel.add(objDescrPane, new CellConstraints(1,8,2,1));
		leftPanel.addLabel("Prix de base: ", new CellConstraints(1,9,2,1));
		leftPanel.add(objPrix, new CellConstraints(1,10,2,1));
		leftPanel.add(proposer, new CellConstraints(1,11));
		leftPanel.add(raz, new CellConstraints(2,11));
		
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
        Logger.log("VentePanel", 2, event.getActionCommand());
        if(event.getActionCommand().equals("proposer")) {
            try {
                // CREATION DE L'OBJET A PROPOSER
                Objet o = new Objet();
                // NB : l'ID de l'objet nouvellement créé vaut par défaut 0.
                // Au Serveur de corriger le tir en attribuant une ID réelle.
                o.setId(0);
                o.setNom(objTitre.getText());
                o.setDescription(objDescr.getText());
                o.setPrixDeBase(Integer.parseInt(objPrix.getText()));
                // normalement pas réglé à ce stade, mais c'est préférable,
                // surtout si la valeur par défaut est zéro, ce qui dans cet
                // enum se traduit par Statuto.Vendu !
                o.setStatut(StatutObjet.Propose);
                // le vendeur de l'objet est celui qui le propose, par définition
                o.setVendeur(Client.session.getLogin());

                // EXPEDITION DE L'OBJET A PROPOSER
                Client.hi.proposerObjet(o);

                // vider les champs de la proposition d'objet
                objTitre.setText("");
                objDescr.setText("");
                // champ de prix mis à zéro seulement si tout se passe bien,
                // sinon il indique l'erreur.
                objPrix.setText("");
                // a terme, il faudra aussi enlever l'image sélectionnée ici
            } catch(java.lang.NumberFormatException nfe) {
                Logger.log("VentePanel", 1, nfe.getMessage()+" (prix incorrect)");
                objPrix.setText("prix invalide");
            }
        } else if(event.getActionCommand().equals("raz")) {
            objTitre.setText("");
            objDescr.setText("");
            objPrix.setText("");
            // a faire : déselectionner l'image...
        } else if(event.getActionCommand().equals("browse"))
		{
			//blabla
		}
	}

    /* relai de m�thode de HI, affiche la liste des objets de l'onglet Vente ! */
    void affichageListeObjets(Set<Objet> ol) {
        Vector <VenteObjet> liste = new Vector<VenteObjet>();
        for(Objet o : ol) {
            VenteObjet element = null;
            switch(o.getStatut()) {
                case Propose:
                    element = new VenteObjetPropose(o); break;
                case Accepte:
                    element = new VenteObjetAccepte(o); break;
                case EnVente:
                    element = new VenteObjetEnVente(o); break;
                case Vendu:
                    element = new VenteObjetVendu(o); break;
                case Refuse:
                    element = new VenteObjetRefuse(o); break;
            }
            liste.add(element);
        }

        JList vue = new JList(liste);
        vue.setCellRenderer(new ListCellRenderer() {
                // This is the only method defined by ListCellRenderer.
                // We just reconfigure the JLabel each time we're called.

                public Component getListCellRendererComponent(
                    JList list,
                    Object value,            // value to display
                    int index,               // cell index
                    boolean isSelected,      // is the cell selected
                    boolean cellHasFocus)    // the list and the cell have the focus
                {
                    ((VenteObjet) value).selectionne(isSelected);
                return (VenteObjet) value;
                }
                });
        rightPane.setViewportView(vue);
    }
}
