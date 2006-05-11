package trollhammer;
import javax.swing.*;
import java.awt.*;

public class HdVObjet extends JRadioButton
{
	private boolean enCours;
	private ImageIcon img;
	public HdVObjet(boolean enCours, ImageIcon img)
	{
		super(img);
		this.img= img;
		this.enCours = enCours;
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
