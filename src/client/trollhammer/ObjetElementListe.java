package trollhammer;
import javax.swing.*;
import java.awt.*;

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
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //à modifier quand il y aura l'image
		this.setBorder(BorderFactory.createEtchedBorder());
		this.add(new JLabel(nom));

        couleur_fond = this.getBackground();
        couleur_selectionne = Color.LIGHT_GRAY;
    }

    void selectionne(boolean estSelectionne) {
        if(estSelectionne) {
            this.setBackground(couleur_selectionne);
        } else {
            this.setBackground(couleur_fond);
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
		
		this.add(new JLabel("Accepté"));
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
		this.add(new JLabel("Sera vendu le ..."+"à partir de ..."));
	}
}

class VenteObjetPropose extends ObjetElementListe
{
	private JButton retirer = null;
	public VenteObjetPropose(Objet obj)
	{
		super(obj);
		retirer = new JButton("Retirer");
		
		this.add(new JLabel("En attente de validation"));
		this.add(retirer);
		
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
		this.add(new JLabel("Cet Objet à été refusé"));
	}
}

class VenteObjetVendu extends ObjetElementListe
{
	public VenteObjetVendu(Objet obj)
	{
		super(obj);
		
		this.add(new JLabel("Vendu à "+prix_de_vente));
	}
}

class ValiderObjet extends ObjetElementListe {
    
    public ValiderObjet(Objet obj) {
        super(obj);

		this.add(new JLabel(nom+": "+description));
		this.add(new JLabel(String.valueOf(prix_de_base)+".-"));
    }
}

class AchatObjet extends ObjetElementListe {

    public AchatObjet(Objet obj) {
        super(obj);

		this.add(new JLabel(nom));
		this.add(new JLabel("Acheté "+String.valueOf(prix_de_vente)+".-"));
    }
}

class PlanifierObjet extends ObjetElementListe {

    public PlanifierObjet(Objet obj) {
        super(obj);

		this.add(new JLabel(nom));
		this.add(new JLabel(description));
		this.add(new JLabel(String.valueOf(prix_de_base)+".-"));
    }
}
