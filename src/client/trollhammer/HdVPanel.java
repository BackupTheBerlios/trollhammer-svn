package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Set;
import java.util.Vector;
import java.util.Collections;
import java.util.Comparator;

class HdVPanel extends JComponent implements ActionListener
{
	private FormLayout layout = null;
	private FreshPanel listeObjetsPanel = null;
	private CoolPanel infoAdjPanel = null;
	private CoolPanel selectPanel = null;
	private CoolPanel imgPanel = null;
	private JScrollPane descrObjetPane = null;
	private JTextArea descrObjetTextArea = null;
	private CoolPanel salleANDobjEnCoursPanel = null;
	private CoolPanel objEnCoursPanel = null;
	private JLabel imgObjEnCoursLabel = null;
	private ImageIcon imgObjEnCours = null;
	private FreshPanel sallePanel = null;
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
	
	private boolean modo = false;
	private JLabel nbCdMLabel = null;
	private JLabel prixEnCours = null;
	private JLabel prochaineEnchere = null;
	private Vector<HdVObjet> vobjs = null;
	private JList lobjs = null;
	private JLabel adjEnCours = null;
	private ButtonGroup grpl = null;
    private String victime = null;
	private ButtonGroup ObjetsVentegrp = null;
    private Window mw;

    // afficher les messages "vente en cours" ? utilisé
    // pour s'assurer qu'on ne le fait qu'au login
    private boolean afficher_message_encours = false;


	public HdVPanel(boolean modo, Window mw)
	{
		this.modo = modo;
        this.mw = mw;
	}
	private void initHdVComponents()
	{
		//majChamps(); //nullPointerException quand tu nous tiens.....
		
		//Liste des objets
		listeObjetsPanel = new FreshPanel('x', true);
		
		//Informations adjudications
		prixEnCours = new JLabel(Client.client.getPrixCourant()+".-");
		infoAdjPanel = new CoolPanel("pref:grow, right:pref","pref, pref");
		nbCdMLabel = new JLabel("0");
		infoAdjPanel.addLabel("Prix d'adjudication: ", new CellConstraints(1,1));
		infoAdjPanel.addC(prixEnCours, new CellConstraints(2,1));
		infoAdjPanel.addLabel("Nombre de coups de marteau: ",new CellConstraints(1,2));
		infoAdjPanel.addC(nbCdMLabel, new CellConstraints(2,2));
		
		//Informations sur l'objets sélectionné
		selectPanel = new CoolPanel("pref,left:pref:grow,pref","pref,center:pref,pref,fill:pref:grow");
		selectPanel.setColumnGroups(new int[][] {{1,3}});
		imgPanel = new CoolPanel("center:pref:grow","pref:grow,pref,pref,pref,pref:grow");
		imgPanel.setRowGroups(new int[][] {{1,4}});
		imgPanel.setPreferredSize(new Dimension(150,150));
		imgPanel.setBorder(BorderFactory.createEtchedBorder());
		imgPanel.addC(new JLabel("Image"), new CellConstraints(1,2));
		imgPanel.addC(new JLabel("non disponible"), new CellConstraints(1,3));
		descrObjetTextArea = new JTextArea();
		descrObjetTextArea.setColumns(17);
		descrObjetTextArea.setEditable(false);
		descrObjetTextArea.setWrapStyleWord(true);
		descrObjetTextArea.setLineWrap(true);
		descrObjetPane = new JScrollPane(descrObjetTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		descrObjetPane.setWheelScrollingEnabled(true);
		//selectPanel.addLabel("Image: ", new CellConstraints(1,1,3,1));
		selectPanel.addC(imgPanel, new CellConstraints(2,2,CellConstraints.CENTER,CellConstraints.CENTER));
		selectPanel.addLabel("Description: ", new CellConstraints(1,3,3,1));
		selectPanel.addC(descrObjetPane, new CellConstraints(1,4,3,1));
		
		//Salle && objet en cours...
		grpl = new ButtonGroup();
		salleANDobjEnCoursPanel = new CoolPanel("pref:grow","pref,fill:pref:grow");
		objEnCoursPanel = new CoolPanel("center:pref:grow","pref");
		imgObjEnCoursLabel = new JLabel((ImageIcon)null, SwingConstants.CENTER);
		imgObjEnCoursLabel.setPreferredSize(new Dimension(55,55));
		objEnCoursPanel.addC(imgObjEnCoursLabel, new CellConstraints(1,1));
		sallePanel = new FreshPanel('y',true);
		salleANDobjEnCoursPanel.addC(objEnCoursPanel, new CellConstraints(1,1));
		salleANDobjEnCoursPanel.addC(sallePanel, new CellConstraints(1,2));
		
		//Log
		logPanel = new CoolPanel("fill:pref:grow","fill:pref:grow");
		logArea = new JTextArea(log);
		logArea.setEditable(false);
		logArea.setWrapStyleWord(true);
		logArea.setLineWrap(true);
		logPane = new JScrollPane(logArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logPane.setWheelScrollingEnabled(true);

        this.afficher_message_encours = true;

        // ceci évite que la fenêtre de chat modifie sa taille et fasse foirer
        // le layout en poussant tout le bas de l'interface hors-fenêtre.
        logPane.setPreferredSize(logPane.getSize());

    	logPanel.addC(logPane, new CellConstraints(1,1));
		
		//Adjudication en cours
		if(Client.client.getDernierEncherisseur() == null)
			adjEnCours = new JLabel("Aucune...");
		else if(Client.client.getDernierEncherisseur().equals(Client.session.getLogin()))
			adjEnCours = new JLabel("EN VOTRE FAVEUR");
		else
			adjEnCours = new JLabel("CONTRE VOUS!");
		adjPanel = new CoolPanel("fill:pref:grow","pref,pref:grow");
		adjPanel.addLabel("Adjudication en cours: ", new CellConstraints(1,1,CellConstraints.CENTER,CellConstraints.CENTER));
		adjPanel.add(adjEnCours, new CellConstraints(1,2,CellConstraints.CENTER,CellConstraints.CENTER));
		
		//enchère
		prochaineEnchere = new JLabel(Client.client.getNouveauPrix()+".-");
		encherePanel = new CoolPanel("pref:grow,right:pref,","pref:grow,pref");
		enchereButton = new JButton("Enchérir!");
        enchereButton.setActionCommand("encherir");
        enchereButton.addActionListener(this);
		encherePanel.addLabel("Prochain prix d'adjudication: ", new CellConstraints(1,1));
		encherePanel.addC(prochaineEnchere, new CellConstraints(2,1));
		encherePanel.addC(enchereButton, new CellConstraints(1,2,CellConstraints.CENTER,CellConstraints.CENTER));
		
		//Chat
		chatPanel = new CoolPanel("fill:pref:grow","pref, pref");
		chatField = new JTextField();
        chatField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chatButton.doClick();
            }
        });
		chatButton = new JButton("Envoyer");
        chatButton.setActionCommand("sendchat");
        chatButton.addActionListener(this);
		chatPanel.addC(chatField, new CellConstraints(1,1));
		chatPanel.addC(chatButton, new CellConstraints(1,2,CellConstraints.CENTER,CellConstraints.CENTER));

		//Panel des commandes
		cmdPanel = new CoolPanel("pref,pref,pref","pref");
		logOutButton = new JButton("Déconnecter");
        logOutButton.setActionCommand("disconnect");
        logOutButton.addActionListener(this);
		cmdPanel.addC(logOutButton, new CellConstraints(1,1));
		if(modo)
		{
			cdmButton = new JButton("Coup de Marteau");
            cdmButton.setActionCommand("trollhammer");
            cdmButton.addActionListener(this);
            cdmButton.setEnabled(false);
			kickButton = new JButton("Expulser");
			kickButton.setActionCommand("kick");
			kickButton.addActionListener(this);
            kickButton.setEnabled(false);
			cmdPanel.addC(cdmButton, new CellConstraints(2,1));
			cmdPanel.addC(kickButton, new CellConstraints(3,1));
		}
	}

	private JComponent buildHdVPanel()
	{
		initHdVComponents();
		layout = new FormLayout(
								"10dlu, pref, pref:grow, pref, 10dlu", //5cols
								"fill:pref, fill:pref, fill:pref, fill:pref:grow, fill:pref, fill:pref, fill:pref" //7rows
								);
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Liste des objets: ", cc.xyw(2,1,2));
		builder.addLabel("Informations Adjudication: ", cc.xy(4,1));
		builder.add(listeObjetsPanel, cc.xyw(2,2,2));
		builder.add(infoAdjPanel, cc.xy(4,2));
		builder.addLabel("Informations sur l'objet sélectionné: ", cc.xy(2,3));
		//builder.addLabel("Salle: ", cc.xy(3,3));
		builder.addLabel("Log: ", cc.xy(4,3));
		builder.add(selectPanel, cc.xy(2,4));
		builder.add(salleANDobjEnCoursPanel, cc.xy(3,4));
		builder.add(logPanel, cc.xy(4,4));
		builder.addLabel("Chat: ", cc.xy(4,5));
		builder.add(adjPanel, cc.xy(2,6));
		builder.add(encherePanel, cc.xy(3,6));
		builder.add(chatPanel, cc.xy(4,6));
		builder.add(cmdPanel, cc.xyw(2,7,3));
		
		return builder.getPanel();
	}
	public JComponent getComponent()
	{
		return buildHdVPanel();
	}

	public void actionPerformed(ActionEvent event)
	{
        Logger.log("Window", 2, event.getActionCommand()+" mouahahahahahahahahahHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHA");
        if(event.getActionCommand().equals("disconnect"))
		{
            mw.doLogout();
        } else if(event.getActionCommand().equals("sendchat"))
		{
            Client.hi.ecrireChat(chatField.getText());
            chatField.setText("");
        } else if(event.getActionCommand().equals("trollhammer"))
		{
            Client.hi.executerModo(ActionModo.CoupDeMassePAF);
        } else if(event.getActionCommand().equals("encherir"))
		{
            enchereButton.setEnabled(false);
            Client.hi.executer(Action.Encherir);
        } else if(event.getActionCommand().equals("kick"))
		{
			//ben il faut kicker ^^
            //jr : et je vais le faire. MOUAHAHAH.
            //la sélection de victime se fait via le champ du même nom,
            //mis à jour dès qu'un bouton d'utilisateur est sélectionné. 
            //(par le ActionListener qu'est HdV, donne aux boutons).
            //le reset de victime est assuré par l'update de la liste
            //(car après un update plus rien n'est sélectionné)
            if(victime != null) {
                kickButton.setEnabled(false);
                Client.hi.kicker(victime);
                this.victime = null;
            }
		} else if(event.getActionCommand().equals("setvictime"))
        {
            // seulement les HdVUsers lancent ceci. On peut donc faire
            // un cast sur l'objet déclencheur.
            //HdVUser selectionne = (HdVUser) event.getSource();
            //this.victime = selectionne.getLogin();
			//becholey: pask on se la pète comme des porcs!!
			this.victime = ((HdVUser) event.getSource()).getLogin();
            kickButton.setEnabled(true);
        }
		else if(event.getActionCommand().equals("afficheObjet"))
		{
			//HdVObjet objetSelectionne = (HdVObjet) event.getSource();
			//descrObjetTextArea.setText(objetSelectionne.getDescription());
			//becholey: pask on se la pète enocre plus comme des porcs!!
			descrObjetTextArea.setText(((HdVObjet) event.getSource()).getDescription());
			imgPanel.removeAll();
			imgPanel.addC(new JLabel(((HdVObjet) event.getSource()).getImage(), SwingConstants.CENTER), new CellConstraints(1,3));
			imgPanel.updateUI();
		}
	}

    /** Ajoute du texte à la fin du contenu du panneau de log.
     * Généralisation de ce qui se faisait au départ uniquement pour le chat,
     * cette méthode permet d'avoir l'autoscrolling du texte pour tous les messages
     * rajoutés dans le panneau de log.
     */
    void texteLog(String texte) {
        // jr : autoscroll pour le log. Cet autoscroll ne s'active que si
        // le texte est déjà scrollé tout en bas, sinon, il ne se passe rien.
        // technique trouvée sur les forums Java de Sun.

        // est-ce que la scrollbar est déjà tout en bas ?
        JScrollBar vbar = logPane.getVerticalScrollBar();
        boolean autoScroll = ((vbar.getValue() + vbar.getVisibleAmount()) == vbar.getMaximum());
        
        // rajouter le texte qui vient d'arriver
        logArea.append(texte);
       
        // scroller si déjà en bas.
        if( autoScroll ) logArea.setCaretPosition( logArea.getDocument().getLength() );
    }

    /** Idem que texteLog(String texte), mais fait un saut de ligne à la fin.
    */
    void texteLogln(String texte) {
        this.texteLog(texte+"\n");
    }

    /* relai (partiel) des méthodes de HI */
    void affichageChat(String m, String i) {
        this.texteLog("<"+i+"> "+m+"\n");
    }
	public void affichageVente(Vente v) //version avec JRadionButton
	{
		listeObjetsPanel.add(new JLabel("Veuillez patienter..."));
		Logger.log("HdVPanel",2, LogType.DBG, "Affichage Vente");
		vobjs = new Vector<HdVObjet>();
		for(int i : v.getOIds())
		{
			//Logger.log("HdVPanel",0,"@@@ for.. @@@");
            Objet o = Client.objectmanager.getObjet(i);
            if(o != null)
			{
                vobjs.add(new HdVObjet(o));
            }
        }
		//Affichage de l'objet en cours !!!!!!!!DOIT ÊTRE SYNCRO!!!!!!!
		
        // jr : rajouté test de taille, comme ça vente vide ou pas de vente
        // ne fait pas d'exception quand on essaie d'avoir le firstElement
        // d'un vecteur... vide...
        // C'est cela qui est à l'origine du bug qui quitte l'interface à
        // la fin de la dernière vente planifiée (comme montré dans notre...
        // démo...)
        if(vobjs.size() > 0) {
            imgObjEnCours = new ImageIcon(vobjs.firstElement().getImage().getImage());
            int h = imgObjEnCours.getIconHeight();
            int w = imgObjEnCours.getIconWidth();
            Logger.log("HdVPanel",2,"imgObjEnCours h: "+h+", w: "+w);
            if(w>h)
                imgObjEnCours.setImage(imgObjEnCours.getImage().getScaledInstance(50,-1,Image.SCALE_SMOOTH));
            else
                imgObjEnCours.setImage(imgObjEnCours.getImage().getScaledInstance(-1,50,Image.SCALE_SMOOTH));

            imgObjEnCoursLabel.setIcon(imgObjEnCours);

        }
		affichageListeObjets();
        majChamps();
	}
	private void affichageListeObjets()
	{
		listeObjetsPanel.removeAll();
		
		ObjetsVentegrp = new ButtonGroup();
		for(HdVObjet o: vobjs)
		{
			
			o.addActionListener(this);
			o.setActionCommand("afficheObjet");
			listeObjetsPanel.add(o);
			ObjetsVentegrp.add(o);
			//Logger.log("HdVPanel",0,"@@@ ADD add ADD @@@");
		}
		
        // update graphique
        SwingUtilities.invokeLater(new Runnable()
								   {
            public void run()
		{
                listeObjetsPanel.validate();
                listeObjetsPanel.repaint();
		}
								   });
	}

    void affichageListeParticipants(Set<Participant> pl) {
        grpl = new ButtonGroup();
        sallePanel.removeAll();
        this.victime = null; // reset de la victime !

        // liste ordonnée des participants
        Vector<Participant> participants = new Vector<Participant>();
        participants.addAll(pl);

        // classement de la liste des Participants.
        Collections.sort(participants,
                new Comparator<Participant>(){
                    public int compare(Participant p1, Participant p2) {
                        return (p1.getLogin()).compareTo(p2.getLogin());
                    }
                    public boolean equals(Participant p1, Participant p2) {
                        return (p1.getLogin()).equals(p2.getLogin());
                    }
                }
        );

        for(Participant p : participants) {
            HdVUser u = null;
            if(p.getStatut() == StatutLogin.Connecte_Utilisateur) {
                // false == utilisateur pas modo
                u = new HdVUser(p.getLogin(), false);
            } else if(p.getStatut() == StatutLogin.Connecte_Moderateur) {
                // true == utilisateur modo
                u = new HdVUser(p.getLogin(), true); 
            }

            if(u != null) {
                // assurer la mise à jour de la victime du kick
                // (seulement si modo, sinon on override l'icône
                // "sélectionné" pour qu'elle soit identique
                // à l'icône déselectionné !)
                if(modo) {
                    u.addActionListener(this);
                    u.setActionCommand("setvictime");
                } else {
                    u.setSelectedIcon(u.getIcon());
                }
                grpl.add(u);
                sallePanel.add(u);
            }
        }

        // update graphique (en differe pour eviter les problemes de synchro)
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()
        {
            sallePanel.validate();
            sallePanel.repaint();
        }
        }); // pask on s'la pète!!!
    }

    void affichage(Evenement e) {    
        switch (e) {    
            case CoupDeMassePAF1:
                texteLogln("- PREMIER COUP DE MARTEAU -");
				nbCdMLabel.setText("1");
                break;   
            case CoupDeMassePAF2:   
                texteLogln("- SECOND COUP DE MARTEAU -");
				nbCdMLabel.setText("2");
                break;     
            case Adjuge:
				
				nbCdMLabel.setText("3"); // à gicler si besoin est...
                texteLogln("- ADJUDICATION -\n"     
                        +"Objet : "
                        //le nom de l'objet qui vient d'être vendu (ouf!)     
                        +Client.objectmanager.getObjet(     
                            Client.ventemanager.getVenteEnCours().getFirst()   
                            ).getNom()+"\n"   
                        +"Vendu à : "   
                        +Client.client.getDernierEncherisseur()+"\n"
                        +"Au prix de "     
                        +Client.client.getPrixCourant()+".-"
                        );
				
				nbCdMLabel.setText("0");
                enchereButton.setEnabled(true);
                break;      
            case VenteAutomatique:     
                texteLogln("- Vente en mode automatique -");    
                if(modo) {
                    cdmButton.setEnabled(true);
                }
                break;   
            default :   
        }      
    }     

    void affichageEnchere(Integer prix, String i) {    
        texteLogln(i+" enchérit à "+prix);

        // réactiver le bouton enchérir si l'enchérisseur
        // n'est pas soi-même ! (et si on n'est pas superviseur)
        if(!i.equals(Client.session.getLogin())
                && !Client.session.getLogin().equals(Client.client.getSuperviseur())) {
            enchereButton.setEnabled(true);
        }
		majChamps();
    }    
	private void majChamps()
	{
		//majEncherePanel();
		//majAdjPanel();
		//majInfoAdjPanel();
		Logger.log("HdVPanel",0,"### MAJCHAMPS.........###");
		if(Client.client.getDernierEncherisseur() == null)
			adjEnCours.setText("Aucune...");
		else if(Client.client.getDernierEncherisseur().equals(Client.session.getLogin()))
			adjEnCours.setText("EN VOTRE FAVEUR");
		else
			adjEnCours.setText("CONTRE VOUS!");
		prochaineEnchere.setText(Client.client.getNouveauPrix()+".-");
		prixEnCours.setText(Client.client.getPrixCourant()+".-");
		SwingUtilities.invokeLater(
								   new Runnable()
								   {
									   public void run()
								   {
										   adjPanel.validate();
										   adjPanel.repaint();
										   encherePanel.validate();
										   encherePanel.repaint();
										   infoAdjPanel.validate();
										   infoAdjPanel.repaint();
										   imgObjEnCoursLabel.validate();
										   imgObjEnCoursLabel.repaint();
										   
								   }
								   }); // pask on s'la pète!!!
		
	}

    void message(Notification n) {     
        switch (n) {      
            case DebutVente:     
                texteLogln("- Démarrage de vente -");      
                enchereButton.setEnabled(true);
                if(modo) {
                    cdmButton.setEnabled(true);
                }
                break;     
            case VenteEnCours:    
                if(!afficher_message_encours) {
                    texteLogln("- Vente en cours -");     
                    // on l'a affiché, on ne le refait plus
                    // avant... la prochaine instance de HdVPanel
                    afficher_message_encours = false;
                }
                enchereButton.setEnabled(true);
                if(modo) {
                    cdmButton.setEnabled(true);
                }
                break;      
            case FinVente:     
                texteLogln("- Fin de la vente -");      
                enchereButton.setEnabled(false);
                if(modo) {
                    cdmButton.setEnabled(false);
                }
					imgPanel.removeAll();
					descrObjetTextArea.setText("");
				imgPanel.addC(new JLabel("Image"), new CellConstraints(1,2));
				imgPanel.addC(new JLabel("non disponible"), new CellConstraints(1,3));
				
                break;   
        }   
    }      

    /** Fonction appellée par Window à chaque fois que l'utilisateur sélectionne
     * l'Onglet Hôtel Des Ventes, permet d'initialiser les boutons à 'désactivé'.
     */
    void initTab() {
        // jr : a chaque fois que le tab est affiché,
        // et si aucune réception de
        // VenteEnCours ou DebutVente, on désactive le bouton 'enchérir'.
        // tout pareil pour le kick et le coup de MASSE.
        enchereButton.setEnabled(false);
        if(modo) {
            kickButton.setEnabled(false);
        }
    }

    /* rajout p.r. Design : afficher quand un utilisateur devient
     * superviseur de vente, pour dire que la vente passe en manuel !
     */

    void superviseur(String i) {
        texteLogln("- Vente en mode manuel -");      
        texteLogln("- commissaire priseur : "+i+" -");
        if(modo) {
            if(i.equals(Client.session.getLogin())) {
                cdmButton.setEnabled(true);
                enchereButton.setEnabled(false);
            } else {
                cdmButton.setEnabled(false);
                enchereButton.setEnabled(true);
            }
        }
    }
}
