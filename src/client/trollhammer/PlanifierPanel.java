package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Set;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Collections;
import java.util.Comparator;

class PlanifierPanel implements ActionListener
{
	private FormLayout layout = null;
	private boolean modo = false;
	private boolean modeAuto = true;
	//pan1 champs d'une vente
	private String titrePan1 = "";
	private CoolPanel pan1 = null;
	private JComboBox nomBox = null;
    static private final String NOM_VIDE = "";

    /* jr : pas de sélection du mode, et pas de date de lancement,
     * juste une date d'ouverture, car ouverture == lancement.
     * (ceci a changé depuis les screenshots du manuel qui ont
     * servi a créer le panel). La suite a été enlevée.
	private JRadioButton manuel = null;
	private JRadioButton auto = null;
	private ButtonGroup butGroup = null;
    */
	private JFormattedTextField ouvDateFTF = null;
	private JFormattedTextField ouvHeureFTF = null;

	private SimpleDateFormat dateFormat = null;
    private SimpleDateFormat heureFormat = null;
    private SimpleDateFormat parseFormat = null;
	private JScrollPane descrPane = null;
	private JTextArea descrArea = null;
	private JButton nouveau = null;
	private JButton valider = null;
	private JButton supprimer = null;
	
	//pan2 objets accepte
	private JScrollPane jspPan2 = null;
	private FreshPanel pan2 = null;
    private JList listeAccepte = null;
    private ArrayList<PlanifierObjet> objs = null;
	
	//pan3 boutons ajouter enlever des objets
	private FreshPanel pan3 = null;
	private CoolPanel internPan3 = null;
	private JButton add = null;
	private JButton remove = null;
	
	//pan4 objets en vente
	private JScrollPane jspPan4 = null;
	private FreshPanel pan4 = null;
    private JList listeDansVente = null;
	
	//pan5 boutons de gestions du placement des objets en vente
	private FreshPanel pan5 = null;
	private CoolPanel internPan5 = null;
	private JButton up = null;		//déplace un Objet vers le haut
	private JButton down= null;		//déplace un Objet vers le bas
	private JButton top= null;	//déplace un Objet en tête de liste
	private JButton bottom= null;	//déplace un Objet en fin de liste
	
	
	public PlanifierPanel(boolean modo)
	{
		this.modo = modo;
	}
	private void initComponents()
	{
		//pan1 
		titrePan1 = "Planifier une vente: ";
		nomBox = new JComboBox();
		nomBox.setEditable(true);
        nomBox.addItem(NOM_VIDE);
        nomBox.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    if(e.getItem() instanceof Vente) {
                        Client.hi.choisirVente(((Vente) e.getItem()).getId());
                    } else if(e.getItem() == NOM_VIDE) {
                        razChamps();
                    }
                }
            }
        });
        /*
        // renderer dit 'du furieux', qui corrige à la volée
        // le texte affiché pour les Ventes de son toString()
        // par défaut pour y mettre son nom à la place !
        //
        // ne marche pas toujours, pas quand la vente est sélectionnée.
        // remplacé par un simple override de toString() dans Vente...
        //
        nomBox.setRenderer(new DefaultListCellRenderer(){
            public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                if(value instanceof Vente) {
                    Vente v = (Vente) value;
                    label.setText(v.getNom());
                } 
                return (Component) label;
            }
        });*/

		dateFormat = new SimpleDateFormat("dd.MM.yy");
        heureFormat = new SimpleDateFormat("HH:mm");
        parseFormat = new SimpleDateFormat(dateFormat.toPattern()+"' '"+
                heureFormat.toPattern());

        /*
		ouvertureFTF = new JFormattedTextField(dateFormat);
		ouvertureFTF.setColumns(16);
		ouvertureFTF.setInputVerifier(new InputVerifier() {
			public boolean verify(JComponent input) {
				if (!(input instanceof JFormattedTextField))
					return true; // give up focus
				return ((JFormattedTextField) input).isEditValid();
			}
		});*/
		ouvDateFTF = new JFormattedTextField(dateFormat);
        ouvDateFTF.setColumns(10);
		ouvHeureFTF = new JFormattedTextField(heureFormat);
        ouvDateFTF.setColumns(5);
		descrArea = new JTextArea();
		descrArea.setRows(10);
		descrArea.setColumns(16);
		descrArea.setLineWrap(true);
		descrArea.setWrapStyleWord(true);
		descrPane = new JScrollPane(descrArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		nouveau = new JButton("RàZ"); //va falloir faire qu'on puisse le modifier si on séléctionne une vente déjà existante
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
		pan1.addC(nomBox, new CellConstraints(2,3,2,1));
		pan1.addLabel("Ouverture: ", new CellConstraints(1,6));
        pan1.addLabel("le (jj.mm.aa)", new CellConstraints(2,6));
		pan1.add(ouvDateFTF, new CellConstraints(3,6));
        pan1.addLabel("à (hh:mm)", new CellConstraints(2,7));
		pan1.add(ouvHeureFTF, new CellConstraints(3,7));
		pan1.addLabel("Description: ", new CellConstraints(1,8,3,1));
		pan1.addC(descrPane, new CellConstraints(1,9,3,1));
		pan1.addC(nouveau, new CellConstraints(1,11));
		pan1.addC(valider, new CellConstraints(2,11));
		pan1.addC(supprimer, new CellConstraints(3,11));
		
		
		
		//pan2
		jspPan2 = new JScrollPane(pan2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listeAccepte = new JList();
        jspPan2.add(listeAccepte);

        listeAccepte.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeAccepte.setCellRenderer(new ListCellRenderer() {
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

        jspPan2.setViewportView(listeAccepte);
        jspPan2.setPreferredSize(new Dimension(150, 150));
		
		//pan3
		try
		{
			add = new JButton(new ImageIcon(this.getClass().getResource("/ressources/img/add.png")));
		} catch(NullPointerException e)
		{
			add = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/add.png"));
		}
		
		add.setActionCommand("add");
		add.addActionListener(this);
		try
		{
			remove = new JButton(new ImageIcon(this.getClass().getResource("/ressources/img/remove.png")));
		} catch(NullPointerException e)
		{
			remove = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/remove.png"));
		}
		remove.setActionCommand("remove");
		remove.addActionListener(this);
		pan3 = new FreshPanel('y',true);
		internPan3 = new CoolPanel("pref:grow,pref,pref:grow","pref:grow,pref,pref,pref:grow");
		internPan3.setRowGroups(new int[][]{{1,4}});
		pan3.setLayout(new BorderLayout());
		internPan3.addC(add, new CellConstraints(2,2));
		internPan3.addC(remove, new CellConstraints(2,3));
		pan3.add(internPan3, BorderLayout.CENTER);
		
		//pan4
		jspPan4 = new JScrollPane(pan4, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listeDansVente = new JList();
        jspPan4.add(listeDansVente);

        listeDansVente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeDansVente.setCellRenderer(new ListCellRenderer() {
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

        jspPan4.setViewportView(listeDansVente);
        jspPan4.setPreferredSize(new Dimension(150, 150));
		
		//pan5
		try
		{
			up = new JButton(new ImageIcon(this.getClass().getResource("/ressources/img/up.png")));
		} catch(NullPointerException e)
		{
			up = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/up.png"));
		}
        up.setActionCommand("up");
        up.addActionListener(this);
		try
		{
			down = new JButton(new ImageIcon(this.getClass().getResource("/ressources/img/down.png")));
		} catch(NullPointerException e)
		{
			down = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/down.png"));
		}
        down.setActionCommand("down");
        down.addActionListener(this);
		try
		{
			top = new JButton(new ImageIcon(this.getClass().getResource("/ressources/img/top.png")));
		} catch(NullPointerException e)
		{
			top = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/top.png"));
		}
        top.setActionCommand("top");
        top.addActionListener(this);
		try
		{
			bottom = new JButton(new ImageIcon(this.getClass().getResource("/ressources/img/bottom.png")));
		} catch(NullPointerException e)
		{
			bottom = new JButton(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/bottom.png"));
		}
        bottom.setActionCommand("bottom");
        bottom.addActionListener(this);
		internPan5 = new CoolPanel("pref:grow,pref,pref:grow","pref:grow,pref,pref,pref,pref,pref:grow");
		pan5 = new FreshPanel('y',true);
		pan5.setLayout(new BorderLayout());
		internPan5.addC(top, new CellConstraints(2,2));
		internPan5.addC(up, new CellConstraints(2,3));
		internPan5.addC(down, new CellConstraints(2,4));
		internPan5.addC(bottom, new CellConstraints(2,5));
		pan5.add(internPan5, BorderLayout.CENTER);
	}
	private JComponent buildPlanifierPanel()
	{
		layout = new FormLayout("fill:pref, pref:grow, pref, pref:grow,pref","fill:pref:grow");
		initComponents();
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.add(pan1, new CellConstraints(1,1));
		builder.add(jspPan2, new CellConstraints(2,1));
		builder.add(pan3, new CellConstraints(3,1));
		builder.add(jspPan4, new CellConstraints(4,1));
		builder.add(pan5, new CellConstraints(5,1));
		
		return builder.getPanel();
	}
	public JComponent getComponent()
	{
		return buildPlanifierPanel();
	}

    void affichageListeObjets(Set<Objet> ol) {
        objs = new ArrayList<PlanifierObjet>();

        for(Objet o : ol) {
            objs.add(new PlanifierObjet(o));
        }

        // classement de la liste des Objets,
        // par ID (donc 'chronologiquement' !).
        Collections.sort(objs, new ComparateurObjetID<PlanifierObjet>());

        listeAccepte.setListData(objs.toArray());
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                jspPan2.validate();
                jspPan2.repaint();
                listeAccepte.validate();
                listeAccepte.repaint();
            }
        });
    }

    void affichageListeVentes(Set<Vente> vl) {
        Logger.log("PlanifierPanel", 2, LogType.DBG, "Reçu liste de Ventes de taille "+vl.size());
        // la vente sélectionnée actuellement.
        Object vp =  nomBox.getSelectedItem();
        // reset de liste
        nomBox.removeAllItems();
        //nomBox.addItem(NOM_VIDE);
        
        ArrayList<Vente> ventes = new ArrayList<Vente>(vl);
        Collections.sort(ventes, new Comparator<Vente>(){
            public int compare(Vente e1, Vente e2) {
                return new Integer(e1.getId()).compareTo(
                    new Integer(e2.getId()));
            }
            public boolean equals(Vente e1, Vente e2) {
                return new Integer(e1.getId()).equals(
                    new Integer(e2.getId()));
            }
        });

        // reconstruction de liste
        for(Vente v : ventes) {
            nomBox.addItem(v);
        }

        // resélection de la vente précédemment sélectionnée, si elle existe toujours.
        // et précision de ceci au Client, pour que la FSM suive. Sinon ?
        // KABOOM.
        if(vp != null && vp instanceof Vente) {
            if(Client.ventemanager.getVente(((Vente) vp).getId()) != null) {
                nomBox.setSelectedItem(vp);
                Client.hi.choisirVente(((Vente) vp).getId());
            } else {
                razChamps();
            }
        } else if(vp != null && vp instanceof String) {
            // SI vp != null et est une String ayant servi à créer
            // une vente, alors on cherche dans la liste des nouvelles ventes
            // la vente portant ce nom ! sinon, BOOM.
            Vente the_vente = null;

            for(Vente vte : ventes) {
                if(vte.getNom().equals((String) vp)) {
                    the_vente = vte;
                }
            }

            if(the_vente != null) {
                nomBox.setSelectedItem(the_vente);
                Client.hi.choisirVente(the_vente.getId());
            } else {
                razChamps();
            }

        } else {
            // si rien de clair sélectionné avant, BOOM.
            razChamps();
        }

        // tadaaaam !
    }

    void affichageVente(Vente v) {
        // rien à sélectionner, déjà fait avant (puisqu'une sélection
        // a appellé l'update)
        //nomBox.setSelectedItem(NOM_VIDE);
        //

        // par contre, on garde l'ID de l'objet sélectionné dans la liste d'objets
        // de la vente en mémoire. (l'ID parce que l'Objet même est remplacé
        // lors d'un update de liste...)

        Object sel = listeDansVente.getSelectedValue();
        Object nsel = null; // la nouvelle sélection
        int selID;

        // ça se corse. l'update ne doit se faire que
        // si la vente qui arrive porte un nom identique
        // à la vente sélectionnée, pour des raisons de cohérence.
        Object vente_prec = nomBox.getSelectedItem();

        if(vente_prec != null && vente_prec.toString().equals(v.toString())) {
            if(sel instanceof PlanifierObjet) {
                selID = ((PlanifierObjet) sel).getId();   
            } else {
                selID = -1;
            }

            GregorianCalendar date = new GregorianCalendar();
            date.setTimeInMillis(v.getDate());

            ouvDateFTF.setText(dateFormat.format(date.getTime()));
            ouvHeureFTF.setText(heureFormat.format(date.getTime()));
            descrArea.setText(v.getDescription());

            // les objets de la vente ! hé oui !

            ArrayList<PlanifierObjet> vobjs = new ArrayList<PlanifierObjet>();

            for(int i : v.getOIds()) {
                Objet o = Client.objectmanager.getObjet(i);
                if(o != null) {
                    PlanifierObjet planobj = new PlanifierObjet(o);

                    vobjs.add(planobj);

                    // utilisé pour savoir si l'objet
                    // a une ID identique à celui sélectionné
                    // précédemment. Si oui, sa représentation
                    // (PlanifierObjet) est celle qu'il faut
                    // sélectionner !
                    if(o.getId() == selID) {
                        nsel = planobj;
                    }
                }
            }

            listeDansVente.setListData(vobjs.toArray());

            // si l'objet précédemment sélectionné est toujours dans la liste
            // d'objets à afficher, le resélectionner.
            if(nsel != null) {
                listeDansVente.setSelectedValue(nsel, true);
            }

            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    jspPan4.validate();
                    jspPan4.repaint();
                    listeDansVente.validate();
                    listeDansVente.repaint();
                }
            });
        }

    }

    /*
	private JComboBox nomBox = null;
	private JFormattedTextField ouvDateFTF = null;
	private JFormattedTextField ouvHeureFTF = null;

	private SimpleDateFormat dateFormat = null;
    private SimpleDateFormat heureFormat = null;
	private JTextArea descrArea = null;
	private JButton nouveau = null;
	private JButton valider = null;
	private JButton supprimer = null;
    */
	public void actionPerformed(ActionEvent event)
	{
        String commande = event.getActionCommand();
        Logger.log("PlanifierPanel", 2, commande);
		if(commande.equals("new"))
		{
            razChamps();
		}
		else if(commande.equals("ok"))
		{
            // parsing de la date. C'est joyeux.
            
            Object selectionne;
            GregorianCalendar ouvDate = null; 
            long date;
            Vente v;

            try {
                ouvDate = new GregorianCalendar();
                // parsing d'une date + heure à partir du contenu des champs
                // y étant dédiés... concaténés. Le format de parsing est
                // aussi une concaténation des deux formats des champs.
                ouvDate.setTime(parseFormat.parse(ouvDateFTF.getText()+
                            " "+ouvHeureFTF.getText())
                        );

                date = ouvDate.getTimeInMillis();

                Logger.log("PlanifierPanel", 2, LogType.DBG, "Date comprise :"+
                        ouvDate.getTime()+" ("+date+")");

                // parsing éventuel du nom. Si le nom est en fait une Vente
                // -- affichée -- alors c'est une modification,
                // sinon, bonne grosse création des familles.

                selectionne = nomBox.getSelectedItem();

                if(selectionne instanceof Vente) {
                    // cas modification
                    // bug : nom devient à peu près comme ID,
                    // immutable, puisqu'il sert à sélectionner!
                    // s'il est changé, alors c'est une nouvelle vente
                    // avec le nouveau nom qui est créée...
                    
                    // autre bug avec un workaround :
                    // la liste d'objets se vide si on fait
                    // un cast de selectionne et qu'on le renvoie.
                    // Alors, on prend son ID et
                    // on cherche la vente correcte dans le manager.
                    // v = (Vente) selectionne;
                    v = Client.ventemanager.getVente(((Vente) selectionne).getId());
                    v.setDescription(descrArea.getText());
                    v.setDate(date);

                    Logger.log("PlanifierPanel", 1, LogType.INF, "Modification Vente");
                    Client.hi.editerVente(Edition.Modifier, v);
                } else if(selectionne instanceof String
                        && !((String) selectionne).equals("")) {
                    // cas création
                    v = new Vente();
                    v.setNom((String) selectionne);
                    v.setDescription(descrArea.getText());
                    v.setDate(date);
                    v.setMode(Mode.Automatique);

                    Logger.log("PlanifierPanel", 1, LogType.INF, "Creation Vente");
                    Client.hi.editerVente(Edition.Creer, v);
                }

            } catch (Exception e) {
                Logger.log("PlanifierPanel", 1, LogType.WRN, "Ne peut pas interpréter la date : "+e.getMessage());
            }
        }
        else if(commande.equals("del"))
        {
            Object selectionne = nomBox.getSelectedItem();

            if(selectionne instanceof Vente) {
                // cas modification
                // bug : nom devient à peu près comme ID,
                // immutable, puisqu'il sert à sélectionner!
                // s'il est changé, alors c'est une nouvelle vente
                // avec le nouveau nom qui est créée...
                Vente v = (Vente) selectionne;
                Client.hi.editerVente(Edition.Supprimer, v);
            }
        }
        else if(commande.equals("add"))
        {
            if(!listeAccepte.isSelectionEmpty()
                    && nomBox.getSelectedItem() instanceof Vente
                    && listeAccepte.getSelectedValue() instanceof PlanifierObjet) {
                int oid = ((PlanifierObjet) listeAccepte.getSelectedValue()).getId();
                int vid = ((Vente) nomBox.getSelectedItem()).getId();

                // ajouter l'objet en fin de liste dans la Vente
                Client.hi.ajouterObjetVente(oid, vid, -1);
            }
        }
        else if(commande.equals("remove"))
        {
            if(!listeDansVente.isSelectionEmpty()
                    && nomBox.getSelectedItem() instanceof Vente
                    && listeDansVente.getSelectedValue() instanceof PlanifierObjet) {
                int oid = ((PlanifierObjet) listeDansVente.getSelectedValue()).getId();
                int vid = ((Vente) nomBox.getSelectedItem()).getId();

                // ajouter l'objet en fin de liste dans la Vente
                Client.hi.retirerObjetVente(oid, vid);
            }
        } else if(commande.equals("up")) {
// jr : aucune des commandes ci-dessous ne marche. envoyer deux messages
// 'enlever' et 'ajouter' à la suite se fait juste trop vite pour que la
// FSM du client accepte que ajouterObjetVente se fasse alors même
// que la réponse du retirerObjetVente() n'est pas arrivée.
// On pourrait résoudre ça par des... beau gros moniteurs !
// (ma tentative naïve de le faire a profondément raté.)

            // monter d'un cran dans la liste des ventes
            if(!listeDansVente.isSelectionEmpty()
                    && nomBox.getSelectedItem() instanceof Vente
                    && listeDansVente.getSelectedValue() instanceof PlanifierObjet) {
                //int index = listeDansVente.getSelectedIndex();
                int oid = ((PlanifierObjet) listeDansVente.getSelectedValue()).getId();
                int vid = ((Vente) nomBox.getSelectedItem()).getId();

                /*
                // si on garde l'index, retire l'objet puis l'insère à
                // (index - 2), considérant que toute la liste aura été
                // décalée de 1 après le retrait, alors replacer l'objet
                // sur (index - 2) l'aura fait bouger d'un cran vers le haut.
                Client.hi.retirerObjetVente(oid, vid);
                // jr : idée : faire un wait() ici
                // (mais c'est le notify() à faire ailleurs qui merde)
                // (IllegalMonitorStateException)
                Client.hi.ajouterObjetVente(oid, vid, index-2);
                */

                Client.ventemanager.moveObjet(vid, oid, TypeDeplacement.UP);
            }
        } else if(commande.equals("down")) {
            // descendre d'un cran dans la liste des ventes
            if(!listeDansVente.isSelectionEmpty()
                    && nomBox.getSelectedItem() instanceof Vente
                    && listeDansVente.getSelectedValue() instanceof PlanifierObjet) {
                //int index = listeDansVente.getSelectedIndex();
                int oid = ((PlanifierObjet) listeDansVente.getSelectedValue()).getId();
                int vid = ((Vente) nomBox.getSelectedItem()).getId();

                /*
                // si on garde l'index, retire l'objet puis l'insère à
                // ce même index, considérant que toute la liste aura été
                // décalée de 1 après le retrait, alors replacer l'objet
                // sur le même index l'aura fait bouger d'un cran vers le bas.
                /*Client.hi.retirerObjetVente(oid, vid);
                Client.hi.ajouterObjetVente(oid, vid, index);*/
                
                Client.ventemanager.moveObjet(vid, oid, TypeDeplacement.DOWN);
            }
        } else if(commande.equals("top")) {
            // insérer au tout début de la liste des ventes
            if(!listeDansVente.isSelectionEmpty()
                    && nomBox.getSelectedItem() instanceof Vente
                    && listeDansVente.getSelectedValue() instanceof PlanifierObjet) {
                //int index = listeDansVente.getSelectedIndex();
                int oid = ((PlanifierObjet) listeDansVente.getSelectedValue()).getId();
                int vid = ((Vente) nomBox.getSelectedItem()).getId();

                // no-brainer, duh
                /*
                Client.hi.retirerObjetVente(oid, vid);
                Client.hi.ajouterObjetVente(oid, vid, 0);
                */

                Client.ventemanager.moveObjet(vid, oid, TypeDeplacement.TOP);
            }
        } else if(commande.equals("bottom")) {
            // insérer en queue de la liste des ventes
            if(!listeDansVente.isSelectionEmpty()
                    && nomBox.getSelectedItem() instanceof Vente
                    && listeDansVente.getSelectedValue() instanceof PlanifierObjet) {
                int index = listeDansVente.getSelectedIndex();
                int oid = ((PlanifierObjet) listeDansVente.getSelectedValue()).getId();
                int vid = ((Vente) nomBox.getSelectedItem()).getId();

                // on a l'index spécial -1 pour ça, c'est pas beau la technique ?
                /*Client.hi.retirerObjetVente(oid, vid);
                Client.hi.ajouterObjetVente(oid, vid, -1);*/

                Client.ventemanager.moveObjet(vid, oid, TypeDeplacement.BOTTOM);
            }
        }
    }

    private void razChamps() {
        nomBox.setSelectedItem(NOM_VIDE);
        ouvDateFTF.setText("");
        ouvHeureFTF.setText("");
        descrArea.setText("");
        // vider l'affichage des objets de la vente
        listeDansVente.setModel(new DefaultListModel());
    }
}
