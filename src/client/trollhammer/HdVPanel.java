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
	private CoolPanel descrObjetPanel = null;
	private JScrollPane descrObjetPane = null;
	private JTextArea descrObjetTextArea = null; //avant JTextArea
	private String descrObjet = null;
	private FreshPanel sallePanel = null;
	private CoolPanel logPanel = null;
	private JScrollPane logPane = null; //inside Pane
	private String log = null;
	private JTextArea logArea = null; //avant JTextArea
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
	private Vector<HdVObjet> lstObjVect = null;
	//test
	//private HdVUser sup = null;
	//private HdVUser l1 = null;
	//private HdVUser l2 = null;
	private ButtonGroup grpl = null;

    private Window mw;

	public HdVPanel(boolean modo, Window mw)
	{
		this.modo = modo;
        this.mw = mw;
	}
	private void initHdVComponents()
	{
		
		//Liste des objets
		listeObjetsPanel = new FreshPanel('x', true);
		
		//Informations adjudications
		infoAdjPanel = new CoolPanel("pref:grow, pref","pref, pref");
		infoAdjPanel.addLabel("Prix d'adjudication: ", new CellConstraints(1,1));
		infoAdjPanel.addLabel("Nombres de coups de marteau: ",new CellConstraints(1,2));
		
		//Informations sur l'objets sélectionné
		selectPanel = new CoolPanel("left:pref:grow","pref,pref,pref,fill:pref:grow");
		imgLabel = new JLabel("image non\ndisponible");
		imgLabel.setPreferredSize(new Dimension(150,150));
		imgLabel.setBorder(BorderFactory.createEtchedBorder());
		descrObjetPanel = new CoolPanel("fill:pref","fill:pref:grow");
		//descrObjet = "Bonjour je m'appelle Monsieur Pougnasse et j'aime les noix... pas vous??";
		descrObjetTextArea = new JTextArea(descrObjet);
		descrObjetTextArea.setRows(7);
		descrObjetTextArea.setEditable(false);
		descrObjetTextArea.setWrapStyleWord(true);
		descrObjetTextArea.setLineWrap(true);
		descrObjetPane = new JScrollPane(descrObjetTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		descrObjetPane.setWheelScrollingEnabled(true);
		descrObjetPanel.add(descrObjetPane, new CellConstraints(1,1));
		selectPanel.addLabel("Image: ", new CellConstraints(1,1));
		selectPanel.add(imgLabel, new CellConstraints(1,2));
		selectPanel.addLabel("Description: ", new CellConstraints(1,3));
		selectPanel.add(descrObjetPane, new CellConstraints(1,4));
		
		//Salle//test...
		//sup = new HdVUser("Mr.Smith",true,true);
		//l1 = new HdVUser("BOFH",true);
		//l2 = new HdVUser("FredFooBar",false);
		grpl = new ButtonGroup();
		//grpl.add(sup);
		//grpl.add(l1);
		//grpl.add(l2);
		sallePanel = new FreshPanel('y',true);
		//sallePanel.add(sup);
		//sallePanel.add(l1);
		//sallePanel.add(l2);
		
		//Log
		logPanel = new CoolPanel("fill:pref","fill:pref:grow");
		logArea = new JTextArea(log);
		logArea.setEditable(false);
		logArea.setWrapStyleWord(true);
		logArea.setLineWrap(true);
		logPane = new JScrollPane(logArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logPane.setWheelScrollingEnabled(true);

    	logPanel.add(logPane, new CellConstraints(1,1));
		
		//Adjudication en cours
		adjPanel = new CoolPanel("pref","pref,pref");
		
		adjPanel.addLabel("Adjudication en cours: ", new CellConstraints(1,1));
		
		//enchère
		encherePanel = new CoolPanel("pref:grow,pref,pref","pref");
		enchereButton = new JButton("Enchérir!");
        enchereButton.setActionCommand("encherir");
        enchereButton.addActionListener(this);
		encherePanel.addLabel("prochain prix d'adjudication: ", new CellConstraints(1,1));
		encherePanel.add(enchereButton, new CellConstraints(3,1));
		
		//Chat
		chatPanel = new CoolPanel("fill:pref","pref, pref");
		chatField = new JTextField(15);
        chatField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chatButton.doClick();
            }
        });
		chatButton = new JButton("Envoyer");
        chatButton.setActionCommand("sendchat");
        chatButton.addActionListener(this);
		chatPanel.add(chatField, new CellConstraints(1,1));
		chatPanel.add(chatButton, new CellConstraints(1,2));
		
		//Panel des commandes
		cmdPanel = new CoolPanel("left:pref,pref,fill:pref:grow, right:pref","pref");
		logOutButton = new JButton("Déconnecter");
        logOutButton.setActionCommand("disconnect");
        logOutButton.addActionListener(this);
		cmdPanel.add(logOutButton, new CellConstraints(1,1));
		if(modo)
		{
			cdmButton = new JButton("Coup de Marteau");
            cdmButton.setActionCommand("trollhammer");
            cdmButton.addActionListener(this);
			kickButton = new JButton("Expulser");
			kickButton.setActionCommand("kick");
			kickButton.addActionListener(this);
			cmdPanel.add(cdmButton, new CellConstraints(2,1));
			cmdPanel.add(kickButton, new CellConstraints(3,1));
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
		builder.addLabel("Information sur l'objet sélectionné", cc.xy(2,3));
		builder.addLabel("Salle: ", cc.xy(3,3));
		builder.addLabel("Log: ", cc.xy(4,3));
		builder.add(selectPanel, cc.xy(2,4));
		builder.add(sallePanel, cc.xy(3,4));
		builder.add(logPane, cc.xy(4,4));
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

    void affichageListeParticipants(Set<Participant> pl) {
        grpl = new ButtonGroup();
        sallePanel.removeAll();
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
                grpl.add(u);
                sallePanel.add(u);
                // update graphique
                sallePanel.validate();
            }
        }
    }
	void affichageListeObjets(Set<Objet> ol)
	{
		lstObjVect = new Vector<HdVObjet>();
		for(Objet o : ol)
		{
			if(o.getStatut() == StatutObjet.EnVente)
				lstObjVect.add(new HdVObjet(o));
        }
		for(HdVObjet hdVObj: lstObjVect)
		{
			listeObjetsPanel.add(hdVObj);
		}
        
	}
}
