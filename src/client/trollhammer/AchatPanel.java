package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.Set;

class AchatPanel
{
	private boolean modo = false;
	private JScrollPane jsp = null;
	private FreshPanel pan = null;
	private JLabel titreLabel = null;
	private String titre = null;
    private JList liste = null;
    private Vector<AchatObjet> objs = null;

	public AchatPanel(boolean modo)
	{
		this.modo = modo;
		if(modo)
			titre = "vous ";
	}
	private JComponent buildAchatPanel()
	{
		pan = new FreshPanel('y',false);
        // a mettre dans leur propre panel, la liste les écrase
		//titreLabel = new JLabel("Liste des objets qui "+titre+"ont été adjugés: ");
		//pan.add(titreLabel);
		jsp = new JScrollPane(pan, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

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
		return jsp;
	}

	public JComponent getComponent()
	{
		return buildAchatPanel();
	}

    void afficherListeObjets(Set<Objet> ol) {
        objs = new Vector<AchatObjet>();
        for(Objet o : ol) {
            objs.add(new AchatObjet(o));
        }

        liste.setListData(objs);
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                liste.validate();
                liste.repaint();
            }
        });
    }

}
