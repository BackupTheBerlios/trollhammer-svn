package trollhammer;
import javax.swing.*;
import java.awt.*;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
/*
 *@TODO modifier les constructeurs quand l'image existera!
 */
public class HdVObjet extends JPanel
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
}




/*public class HdVObjet extends JRadioButton
{
	private boolean enCours;
	private String nom;
	private ImageIcon img;
	private final Color couleur_fond = this.getBackground();
	private final Color couleur_selectionne = Color.LIGHT_GRAY;
	public HdVObjet(Objet o, ImageIcon img) 
	{
		super(img);
		this.img= img;
		this.enCours = enCours;
	}
	public HdVObjet(Objet o)
	{
		super(o.getNom());
		nom = o.getNom();
		enCours = (o.getStatut() == StatutObjet.EnVente);
	}
	public boolean getEnCours()
	{
		return enCours;
	}
	public void setEnCours(boolean enCours)
	{
		this.enCours = enCours;
	}
	void selectionne(boolean estSelectionne) {
        if(estSelectionne) {
            this.setBackground(couleur_selectionne);
        } else {
            this.setBackground(couleur_fond);
        }
    }
}*/
