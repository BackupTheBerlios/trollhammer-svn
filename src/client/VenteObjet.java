package trollhammer;
import javax.swing.*;
import java.awt.*;

abstract class VenteObjet extends JPanel
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

	
	public VenteObjet(Objet obj)
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
}

class VenteObjetAccepte extends VenteObjet
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

class VenteObjetPropose extends VenteObjet
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

class VenteObjetRefuse extends VenteObjet
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

class VenteObjetVendu extends VenteObjet
{
	public VenteObjetVendu(Objet obj)
	{
		super(obj);
		
		this.add(new JLabel("Vendu à "+prix_de_vente));
	}
}

class ValidationObjet extends VenteObjet {
    
    public ValidationObjet(Objet obj) {
        super(obj);

        this.add(new JLabel(description));
        this.add(new JLabel("Prix de base : "+prix_de_base));
    }
}
