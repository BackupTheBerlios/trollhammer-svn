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

class ValiderPanel implements ActionListener
{
	private boolean modo = false;
	private FormLayout layout = null;
	//private CoolPanel pan = null;
	//Panel du haut
	private FreshPanel hautPanel = null;
	private JScrollPane jsp = null;
    private JList liste = null;
    Vector <ValiderObjet> objs = null;
	//Panel du bas
	private CoolPanel basPanel = null;
	private JButton accepter = null;
	private JButton refuser = null;
	
	public ValiderPanel(boolean modo)
	{
		this.modo = modo;
	}
	private void initComponents()
	{
		//Panel du haut
		hautPanel = new FreshPanel('y',false);
		jsp = new JScrollPane(hautPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBorder(BorderFactory.createEtchedBorder());
		//Panel du bas
		basPanel = new CoolPanel();
		basPanel.setLayout(new BoxLayout(basPanel, BoxLayout.X_AXIS));
		accepter = new JButton("Accepter");
        accepter.setActionCommand("accepter");
        accepter.addActionListener(this);
		basPanel.add(accepter);
		refuser = new JButton("Refuser");
        refuser.setActionCommand("refuser");
        refuser.addActionListener(this);
		basPanel.add(refuser);
		
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

        jsp.setViewportView(liste);
	}
	private JComponent buildValiderPanel()
	{
		initComponents();
		layout = new FormLayout("fill:pref:grow","fill:pref:grow, fill:pref");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.add(jsp, cc.xy(1,1));
		builder.add(basPanel, cc.xy(1,2));
		
		return builder.getPanel();
	}
	public JComponent getComponent()
	{
		return buildValiderPanel();
	}

    void affichageListeObjets(Set<Objet> ol) {
        objs = new Vector<ValiderObjet>();

        for(Objet o : ol) {
            objs.add(new ValiderObjet(o));
        }

        liste.setListData(objs);
    }

	public void actionPerformed(ActionEvent event)
	{
        String commande = event.getActionCommand();
        Logger.log("ValiderPanel", 2, commande);

        if(commande.equals("accepter")) {
            // okay chums I'm back. Let's do this.
            int oid = objs.get(liste.getSelectedIndex()).getId();
            // LEEEEEEROOOOY NGNGNGNJEEEEENKIIIIINS!!!!!
            Client.hi.accepterProposition(oid);
        } else if (commande.equals("refuser")) {
            // it's not my fault !
            int oid = objs.get(liste.getSelectedIndex()).getId();
            // at least I have chicken.
            Client.hi.refuserProposition(oid);
        }
	}
}
