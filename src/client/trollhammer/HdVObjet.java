package trollhammer;
import javax.swing.*;
import java.awt.*;
/*
 *@TODO modifier les constructeurs quand l'image existera!
 */


public class HdVObjet extends JRadioButton
{
	private boolean enCours;
	private String nom;
	private ImageIcon img;
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
}
