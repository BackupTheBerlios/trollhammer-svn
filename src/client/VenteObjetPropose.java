package trollhammer;
import javax.swing.*;
import java.awt.*;

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
