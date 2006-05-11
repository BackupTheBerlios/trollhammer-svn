package trollhammer;
import javax.swing.*;
import java.awt.*;

class AchatObjet extends JPanel
{
	private Objet obj;
	private int id;
    private String nom;
    private String description;
    private String moderateur;
	private int prix_de_base;
    private int prix_de_vente;
    private StatutObjet statut;
    private String acheteur;
    private String vendeur;
	
	public AchatObjet(Objet obj)
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
		this.add(new JLabel(String.valueOf(prix_de_vente)+".-"));
	}
}
