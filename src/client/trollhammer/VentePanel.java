package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

class VentePanel implements ActionListener
{
	//éléments du Panel de gauche
	private CoolPanel leftPanel = null;
	private JLabel imgLabel = null;
	private JButton parcourir = null;
	private ImageIcon img = null;
	private ImageIcon objImg = null;
	private JTextField objTitre = null;
	private JTextArea objDescr = null;
	private JScrollPane objDescrPane = null;
	private JTextField objPrix = null;
	private JButton proposer = null;
	private JButton raz = null;
	private JFileChooser chooser = null;
	
	//éléments du Panel de droite
	private JScrollPane rightPane = null;
	private FreshPanel rightPanel = null;
	private String titre = null;
	private JLabel titreLabel = null;
    private JList liste = null;
    private ArrayList <ObjetElementListe> objs = null;
	
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
		imgLabel = new JLabel((Icon)null,SwingConstants.CENTER);
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
        objPrix.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                objPrix.selectAll();
            }
            public void focusLost(FocusEvent e) {
                objPrix.setSelectionStart(0);
                objPrix.setSelectionEnd(0);
            }
        });
		proposer = new JButton("Proposer");
        proposer.setActionCommand("proposer");
        proposer.addActionListener(this);
		raz = new JButton("RàZ");
        raz.setActionCommand("raz");
        raz.addActionListener(this);
		//éléments du Panel de droite
		rightPanel = new FreshPanel('y',false);
        // a mettre dans leur propre panel, la liste les écrase
		//titreLabel = new JLabel(titre);
		rightPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		rightPane.setBorder(BorderFactory.createEtchedBorder());

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
                    ((ObjetElementListe) value).selectionne(isSelected);
                return (ObjetElementListe) value;
                }
                });

        liste.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                // implicitement, la sélection est toujours de taille un,
                // donc on peut ne prendre que l'index du début de la sélection
                int index = liste.getSelectedIndex(); 

                // index < 0 => invalide (-1 == non trouvé)
                if(index > -1 && objs.size() > 0) {
                    ObjetElementListe o = objs.get(index);
                    Client.hi.choisirObjet(o.getId());
                }
            }
        });

        rightPane.setViewportView(liste);
	}
	private JComponent buildVentePanel()
	{
		initComponents();
		//Left Panel
		leftPanel = new CoolPanel("pref, pref","pref, pref, 100dlu, pref, pref, pref, pref, pref, pref, pref, pref");
		leftPanel.addLabel("Proposer un", new CellConstraints(1,1,2,1));
		leftPanel.addLabel("objet à vendre: ", new CellConstraints(1,2,2,1));
		leftPanel.addC(imgLabel, new CellConstraints(1,3,2,1));
		leftPanel.addC(parcourir, new CellConstraints(1,4,2,1));
		leftPanel.addLabel("Titre: ", new CellConstraints(1,5,2,1));
		leftPanel.addC(objTitre, new CellConstraints(1,6,2,1));
		leftPanel.addLabel("Description: ", new CellConstraints(1,7,2,1));
		leftPanel.addC(objDescrPane, new CellConstraints(1,8,2,1));
		leftPanel.addLabel("Prix de base: ", new CellConstraints(1,9,2,1));
		leftPanel.addC(objPrix, new CellConstraints(1,10,2,1));
		leftPanel.addC(proposer, new CellConstraints(1,11));
		leftPanel.addC(raz, new CellConstraints(2,11));
		
		//right Panel
        //le titre se fait écrabouiller
		//rightPanel.add(titreLabel);
		
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
                int id = 0; // id par defaut = 0, le serveur corrige
                String nom = objTitre.getText();
                String description = objDescr.getText();
                int prix_de_base = Integer.parseInt(objPrix.getText());

                if(img != null && !nom.equals("") && !description.equals("") && prix_de_base > 0) {
                    // CREATION DE L'OBJET A PROPOSER
                    Objet o = new Objet();
                    // NB : l'ID de l'objet nouvellement créé vaut par défaut 0.
                    // Au Serveur de corriger le tir en attribuant une ID réelle.
                    o.setId(0);
                    o.setNom(nom);
                    o.setDescription(description);
                    o.setPrixDeBase(prix_de_base);
                    // normalement pas réglé à ce stade, mais c'est préférable,
                    // surtout si la valeur par défaut est zéro, ce qui dans cet
                    // enum se traduit par Statuto.Vendu !
                    o.setStatut(StatutObjet.Propose);
                    // le vendeur de l'objet est celui qui le propose, par définition
                    o.setVendeur(Client.session.getLogin());
                    // on ajoute la zolie zimage
                    o.setImage(objImg);

                    // EXPEDITION DE L'OBJET A PROPOSER
                    Client.hi.proposerObjet(o);

                    // vider les champs de la proposition d'objet
                    objTitre.setText("");
                    objDescr.setText("");
                    // champ de prix mis à zéro seulement si tout se passe bien,
                    // sinon il indique l'erreur.
                    objPrix.setText("");
                    // a terme, il faudra aussi enlever l'image sélectionnée ici
                    img = null;
                    imgLabel.setIcon(null);

                } else if(prix_de_base <= 0) {
                    throw new java.lang.NumberFormatException();
                }
            } catch(java.lang.NumberFormatException nfe) {
                Logger.log("VentePanel", 1, nfe.getMessage()+" (prix incorrect)");
                objPrix.setText("prix invalide");
            }
        } else if(event.getActionCommand().equals("raz")) {
            objTitre.setText("");
            objDescr.setText("");
            objPrix.setText("");
			imgLabel.setIcon(null);
            img = null;
			imgLabel.updateUI();
            // a faire : déselectionner l'image...
        } else if(event.getActionCommand().equals("browse"))
		{
			chooser = new JFileChooser();
			// Note: source for ExampleFileFilter can be found in FileChooserDemo,
			// under the demo/jfc directory in the JDK.
			VentePanelFileFilter filter = new VentePanelFileFilter();
			filter.addExtension("jpg");
			filter.addExtension("gif");
			filter.addExtension("png");
			filter.setDescription("JPG, GIF & PNG Images");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(leftPanel);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				//System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
				img = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
				int h = img.getIconHeight();
				int w = img.getIconWidth();
				if(w>h)
					img.setImage(img.getImage().getScaledInstance(120,-1,Image.SCALE_SMOOTH));
				else
					img.setImage(img.getImage().getScaledInstance(-1,120,Image.SCALE_SMOOTH));
				Logger.log("VentePanel",2,chooser.getSelectedFile().getName());
				objImg = new ImageIcon(img.getImage());
				imgLabel.setIcon(img);
				imgLabel.updateUI();
				
			}
		}
	}

    /* relai de methode de HI, affiche la liste des objets de l'onglet Vente ! */
    void affichageListeObjets(Set<Objet> ol) {
        objs = new ArrayList<ObjetElementListe>();
        for(Objet o : ol) {
            ObjetElementListe element = null;
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
            objs.add(element);
        }

        // classement de la liste des Objets,
        // par ID (donc 'chronologiquement' !).
        Collections.sort(objs, new ComparateurObjetID<ObjetElementListe>());

        liste.setListData(objs.toArray());
    }

    void affichageObjet(Objet o) {
        objTitre.setText(o.getNom());
        objDescr.setText(o.getDescription());
        objPrix.setText(Integer.toString(o.getPrixDeBase()));

        // update image tadadaaam
        img = o.getImage();
        imgLabel.setIcon(img);
        imgLabel.updateUI();

        leftPanel.validate();
        leftPanel.repaint();
    }
}
