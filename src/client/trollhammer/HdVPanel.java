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

class HdVPanel extends JComponent implements ActionListener
{
	private FormLayout layout = null;
	private FreshPanel listeObjetsPanel = null;
	private CoolPanel infoAdjPanel = null;
	private CoolPanel selectPanel = null;
	private JLabel imgLabel = null;
	private JScrollPane descrObjetPane = null;
	private JTextArea descrObjetTextArea = null;
	private String descrObjet = null;
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
	private byte nbCdM = 0;
	private double prixEnCours = 0.;
	private double prochaineEnchere = 0.;
	private Vector<HdVObjet> vobjs = null;
	private String adjEnCours = "";
	private ButtonGroup grpl = null;
    private String victime = null;
	private ButtonGroup ObjetsVentegrp = null;
    private Window mw;

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
		prixEnCours = Client.client.getPrixCourant();
		infoAdjPanel = new CoolPanel("pref:grow, right:pref","pref, pref");
		infoAdjPanel.addLabel("Prix d'adjudication: ", new CellConstraints(1,1));
		infoAdjPanel.addC(new JLabel(""+prixEnCours), new CellConstraints(2,1));
		infoAdjPanel.addLabel("Nombres de coups de marteau: ",new CellConstraints(1,2));
		infoAdjPanel.addC(new JLabel(""+nbCdM), new CellConstraints(2,2));
		
		//Informations sur l'objets sélectionné
		selectPanel = new CoolPanel("pref,left:pref:grow,pref","pref,center:pref,pref,fill:pref:grow");
		selectPanel.setColumnGroups(new int[][] {{1,3}});
		imgLabel = new JLabel("image non\ndisponible");
		imgLabel.setPreferredSize(new Dimension(150,150));
		imgLabel.setBorder(BorderFactory.createEtchedBorder());
		descrObjetTextArea = new JTextArea(descrObjet);
		descrObjetTextArea.setColumns(17);
		descrObjetTextArea.setEditable(false);
		descrObjetTextArea.setWrapStyleWord(true);
		descrObjetTextArea.setLineWrap(true);
		descrObjetPane = new JScrollPane(descrObjetTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		descrObjetPane.setWheelScrollingEnabled(true);
		//selectPanel.addLabel("Image: ", new CellConstraints(1,1,3,1));
		selectPanel.addC(imgLabel, new CellConstraints(2,2,CellConstraints.CENTER,CellConstraints.CENTER));
		selectPanel.addLabel("Description: ", new CellConstraints(1,3,3,1));
		selectPanel.addC(descrObjetPane, new CellConstraints(1,4,3,1));
		
		//Salle
		grpl = new ButtonGroup();
		sallePanel = new FreshPanel('y',true);
		
		//Log
		logPanel = new CoolPanel("fill:pref:grow","fill:pref:grow");
		logArea = new JTextArea(log);
		logArea.setEditable(false);
		logArea.setWrapStyleWord(true);
		logArea.setLineWrap(true);
		logPane = new JScrollPane(logArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logPane.setWheelScrollingEnabled(true);

        // ceci évite que la fenêtre de chat modifie sa taille et fasse foirer
        // le layout en poussant tout le bas de l'interface hors-fenêtre.
        logPane.setPreferredSize(logPane.getSize());

    	logPanel.addC(logPane, new CellConstraints(1,1));
		
		//Adjudication en cours
		if(Client.client.getDernierEncherisseur() == null)
			adjEnCours = "Aucune...";
		else if(Client.client.getDernierEncherisseur().equals(Client.session.getLogin()))
			adjEnCours = "EN VOTRE FAVEUR";
		else
			adjEnCours = "CONTRE VOUS!";
		adjPanel = new CoolPanel("fill:pref:grow","pref,center:pref");
		adjPanel.addLabel("Adjudication en cours: ", new CellConstraints(1,1));
		adjPanel.addLabel(adjEnCours, new CellConstraints(1,2,CellConstraints.CENTER,CellConstraints.CENTER));
		
		//enchère
		prochaineEnchere = Client.client.getNouveauPrix();
		encherePanel = new CoolPanel("pref:grow,right:pref,","pref:grow,pref");
		enchereButton = new JButton("Enchérir!");
        enchereButton.setActionCommand("encherir");
        enchereButton.addActionListener(this);
		encherePanel.addLabel("prochain prix d'adjudication: ", new CellConstraints(1,1));
		encherePanel.addC(new JLabel(""+prochaineEnchere), new CellConstraints(2,1));
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
			kickButton = new JButton("Expulser");
			kickButton.setActionCommand("kick");
			kickButton.addActionListener(this);
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
		builder.addLabel("Informations adjudications: ", cc.xy(4,1));
		builder.add(listeObjetsPanel, cc.xyw(2,2,2));
		builder.add(infoAdjPanel, cc.xy(4,2));
		builder.addLabel("Information sur l'objet sélectionné: ", cc.xy(2,3));
		builder.addLabel("Salle: ", cc.xy(3,3));
		builder.addLabel("Log: ", cc.xy(4,3));
		builder.add(selectPanel, cc.xy(2,4));
		builder.add(sallePanel, cc.xy(3,4));
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
        Logger.log("Window", 2, event.getActionCommand());
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
                Client.hi.kicker(victime);
            }
		} else if(event.getActionCommand().equals("setvictime"))
        {
            // seulement les HdVUsers lancent ceci. On peut donc faire
            // un cast sur l'objet déclencheur.
            HdVUser selectionne = (HdVUser) event.getSource();
            this.victime = selectionne.getLogin();
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

    /* relai (partiel) des méthodes de HI */
    void affichageChat(String m, String i) {
        this.texteLog("<"+i+"> "+m+"\n");
    }
	public void affichageVente(Vente v)
	{
		Logger.log("HdVPanel",0,"@@@ Affichage Panel @@@");
		vobjs = new Vector<HdVObjet>();
		for(int i : v.getOIds())
		{
			Logger.log("HdVPanel",0,"@@@ for.. @@@");
            Objet o = Client.objectmanager.getObjet(i);
            if(o != null)
			{
                vobjs.add(new HdVObjet(o));
            }
        }
		affichageListeObjets();
	}
	private void affichageListeObjets()
	{
		ObjetsVentegrp = new ButtonGroup();
		for(HdVObjet o: vobjs)
		{
			listeObjetsPanel.add(o);
			ObjetsVentegrp.add(o);
			o.addActionListener(this);
		}
	}

    void affichageListeParticipants(Set<Participant> pl) {
        grpl = new ButtonGroup();
        sallePanel.removeAll();
        this.victime = null; // reset de la victime !
        for(Participant p : pl) {
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
                u.addActionListener(this);
                u.setActionCommand("setvictime");
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
	/*void affichageListeObjets(Set<Objet> ol)
	{
        lstObjVect = new Vector<HdVObjet>();
        for(Objet o : ol)
        {
            if(o.getStatut() == StatutObjet.EnVente)
                lstObjVect.add(new HdVObjet(o));
        }
        //listeObjetsPanel.setListData(lstObjVect); beuhaaaaah va pas
    }*/

    void affichage(Evenement e) {    
        switch (e) {    
            case CoupDeMassePAF1:      
                texteLog("--- PREMIER COUP DE MARTEAU ---");
                break;   
            case CoupDeMassePAF2:   
                texteLog("--- SECOND COUP DE MARTEAU ---");
                break;     
            case Adjuge:
				nbCdM = 3;
                texteLog("--- ADJUDICATION ---\n"     
                        +"Objet : "      
                        //le nom de l'objet qui vient d'être vendu (ouf!)     
                        +Client.objectmanager.getObjet(     
                            Client.ventemanager.getVenteEnCours().getFirst()   
                            ).getNom()+"\n"   
                        +"Vendu à : "   
                        +Client.client.getDernierEncherisseur()     
                        +"Au prix de "     
                        +Client.client.getPrixCourant()   
                        );
                break;      
            case VenteAutomatique:     
                texteLog("--- Vente en mode automatique ---");    
                break;   
            default :   
        }      
    }     

    void affichageEnchere(Integer prix, String i) {    
        texteLog(i+" enchérit à "+prix);
		majChamps();
    }    
	private void majChamps()
	{
		majEncherePanel();
		majAdjPanel();
		majInfoAdjPanel();
	}
	private void majAdjPanel()
	{
		prochaineEnchere = Client.client.getNouveauPrix();
		try
		{
			adjPanel.repaint();
		} catch(Exception e){Logger.log("HdVPanel",0,"mais putain de adjPanel.repaint!!!!!!!!!!!!!!!!!!!!!!!!");}
	}
	private void majEncherePanel()
	{
		if(Client.client.getDernierEncherisseur() == null)
			adjEnCours = "Aucune...";
		else if(Client.client.getDernierEncherisseur().equals(Client.session.getLogin()))
			adjEnCours = "EN VOTRE FAVEUR";
		else
			adjEnCours = "CONTRE VOUS!";
		try
		{
			encherePanel.repaint();
		} catch(Exception e){Logger.log("HdVPanel",0,"mais putain de encherePanel.repaint!!!!!!!!!!!!!!!!!!!!");}
	}
	private void majInfoAdjPanel()
	{
		prixEnCours = Client.client.getPrixCourant();
		try
		{
			infoAdjPanel.repaint();
		} catch(Exception e){Logger.log("HdVPanel",0,"mais putain de infoAdjPanel. repaint!!!!!!!!!!!!!!!!!!!");}
	}
    void message(Notification n) {     
        switch (n) {      
            case DebutVente:     
                texteLog("--- Démarrage de vente ---");      
                break;     
            case VenteEnCours:    
                texteLog("--- Vente en cours ---");     
                break;      
            case FinVente:     
                texteLog("--- Fin de la vente ---");      
                break;   
        }   
    }      

}
