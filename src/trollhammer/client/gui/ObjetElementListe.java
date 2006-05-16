package trollhammer.client.gui;
import trollhammer.client.*;
import trollhammer.commun.*;
import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

abstract class ObjetElementListe extends JPanel
{
	protected Objet obj;
	protected int id;
    protected String nom;
    protected String description;
    protected String moderateur;
	protected int prix_de_base;
    protected int prix_de_vente;
    protected StatutObjet statut;
    protected String acheteur;
    protected String vendeur;
    protected Color couleur_fond;
    protected Color couleur_selectionne;
	protected ImageIcon img = null;
	protected JPanel leftPan = null;
	protected JPanel rightPan = null;

	
	public ObjetElementListe(Objet obj)
	{
		super();
		id = obj.getId();
		nom = obj.getNom();
		description = obj.getDescription();
		moderateur = obj.getModerateur();
		prix_de_base = obj.getPrixDeBase();
		prix_de_vente = obj.getPrixDeVente();
		acheteur = obj.getAcheteur();
		vendeur = obj.getVendeur();
		//begin l'image...
		this.img = new ImageIcon(obj.getImage().getImage());
		int h = img.getIconHeight();
		int w = img.getIconWidth();
		if(w>h)
			this.img.setImage(img.getImage().getScaledInstance(50,-1,Image.SCALE_SMOOTH));
		else
			this.img.setImage(img.getImage().getScaledInstance(-1,50,Image.SCALE_SMOOTH));
		//this.add(img);
		//end l'image...
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); //à modifier quand il y aura l'image
		this.setBorder(BorderFactory.createEtchedBorder());
		leftPan = new JPanel();
		leftPan.setLayout(new BoxLayout(leftPan, BoxLayout.X_AXIS));
		leftPan.add(new JLabel(img, SwingConstants.CENTER));
		rightPan = new JPanel();
		rightPan.setLayout(new BoxLayout(rightPan, BoxLayout.Y_AXIS));
		rightPan.add(new JLabel(nom));
		this.add(leftPan);
		this.add(rightPan);

        couleur_fond = this.getBackground();
        couleur_selectionne = Color.LIGHT_GRAY;
    }

    void selectionne(boolean estSelectionne) {
        if(estSelectionne) {
            this.setBackground(couleur_selectionne);
			rightPan.setBackground(couleur_selectionne);
        } else {
            this.setBackground(couleur_fond);
			rightPan.setBackground(couleur_fond);
        }
    }

    int getId() {
        return this.id;
    }
}

class VenteObjetAccepte extends ObjetElementListe
{
	public VenteObjetAccepte(Objet obj)
	{
		super(obj);
		
        //this.add(new JLabel(nom));
		rightPan.add(new JLabel("Accepté"));
	}
}

class VenteObjetEnVente extends VenteObjetAccepte
{
	public VenteObjetEnVente(Objet obj)
	{
		super(obj);
		/**
		*TODO comment faire pour savoir quand l'objet sera vendu...
		*/
		rightPan.add(new JLabel("Sera vendu prochainement."));
	}
}

class VenteObjetPropose extends ObjetElementListe
{
    //jr : on ne peut pas retirer une proposition, rien dans le Design pour.
	//private JButton retirer = null;
	public VenteObjetPropose(Objet obj)
	{
		super(obj);
		//retirer = new JButton("Retirer");
		
		rightPan.add(new JLabel("En attente de validation"));
		//this.add(retirer);
		
	}
}

class VenteObjetRefuse extends ObjetElementListe
{
	public VenteObjetRefuse(Objet obj)
	{
		super(obj);
		/**
		* TODO cause du refus?
		*/
		rightPan.add(new JLabel("Cet Objet à été refusé"));
	}
}

class VenteObjetVendu extends ObjetElementListe
{
	public VenteObjetVendu(Objet obj)
	{
		super(obj);
		
		rightPan.add(new JLabel("Vendu "+prix_de_vente+".- à "+obj.getAcheteur()));
	}
}

class ValiderObjet extends ObjetElementListe {
    
    public ValiderObjet(Objet obj) {
        super(obj);
		rightPan.add(new JLabel(description));
		rightPan.add(new JLabel(String.valueOf(prix_de_base)+".-"));
    }
}

class AchatObjet extends ObjetElementListe {

    public AchatObjet(Objet obj) {
        super(obj);

		rightPan.add(new JLabel(description));
		rightPan.add(new JLabel("Acheté "+String.valueOf(prix_de_vente)+".-"));
    }
}

class PlanifierObjet extends ObjetElementListe {

    public PlanifierObjet(Objet obj) {
        super(obj);
		rightPan.add(new JLabel(description));
		rightPan.add(new JLabel(String.valueOf(prix_de_base)+".-"));
    }
}

/** Un comparateur pour ordonner les ObjetElementListe affichés dans la GUI.
 * L'ordre se fait par les IDs des Objets que les ObjetElementListe représentent.
 * Ce comparateur s'utilise ensuite dans la fonction Collections.sort(liste, comparateur).
 *
 * @author Julien Ruffin
 */
class ComparateurObjetID<T extends ObjetElementListe> implements Comparator<ObjetElementListe> {

    public int compare(ObjetElementListe e1, ObjetElementListe e2) {
        return new Integer(e1.getId()).compareTo(
                new Integer(e2.getId()));
    }
    public boolean equals(ObjetElementListe e1, ObjetElementListe e2) {
        return new Integer(e1.getId()).equals(
                new Integer(e2.getId()));
    }
}
