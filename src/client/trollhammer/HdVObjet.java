package trollhammer;
import javax.swing.*;
import java.awt.*;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
/*
 *@TODO modifier les constructeurs quand l'image existera!
 */
/*public class HdVObjet extends JPanel
{
	private FormLayout layout = null;
	private CellConstraints cc = null;
	private JLabel nomLabel = null;
	private final Color couleur_fond = this.getBackground();
	private final Color couleur_selectionne = Color.LIGHT_GRAY;
	private Objet o = null;
	public HdVObjet(Objet o)
	{
		super();
		layout = new FormLayout("2dlu,pref,2dlu","2dlu,pref,2dlu");
		this.setLayout(layout);
		this.o = o;
		cc = new CellConstraints();
		nomLabel = new JLabel(o.getNom());
		this.add(nomLabel, cc.xy(2,2));
	}
	void selectionne(boolean estSelectionne) {
        if(estSelectionne) {
            this.setBackground(couleur_selectionne);
        } else {
            this.setBackground(couleur_fond);
        }
    }
	public String getDescription()
	{
		return o.getDescription();
	}
}*/




public class HdVObjet extends JRadioButton
{
	//private ImageIcon imgIcon = null;
	private ImageIcon img = null;
	private JLabel label = null;
	private Objet o = null;
	private final Color couleur_fond = this.getBackground();
	private final Color couleur_selectionne = Color.LIGHT_GRAY;
	public HdVObjet(Objet o) 
	{
		super();
		this.o = o;
		this.img = new ImageIcon(o.getImage().getImage());
		int h = img.getIconHeight();
		int w = img.getIconWidth();
		if(w>h)
			this.img.setImage(img.getImage().getScaledInstance(30,-1,Image.SCALE_SMOOTH));
		else
			this.img.setImage(img.getImage().getScaledInstance(-1,30,Image.SCALE_SMOOTH));
		//img.getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
		this.setIcon(img);
	}
	/*public HdVObjet(Objet o)
	{
		super(o.getNom());
		//label = new JLabel(o.getNom());
		//this.setIcon(new ImageIcon(label));
		this.o = o;
	}*/
	void selectionne(boolean estSelectionne) {
        if(estSelectionne) {
            this.setBackground(couleur_selectionne);
			label.setBackground(couleur_selectionne);
        } else {
            this.setBackground(couleur_fond);
			label.setBackground(couleur_fond);
        }
    }
    public String getNom() {
        return o.getNom();
    }
	public String getDescription()
	{
		return o.getDescription();
	}
	public ImageIcon getImage()
	{
		return o.getImage();
	}
}
