package trollhammer;
import javax.swing.*;
import java.awt.*;

class VenteObjetAccepte extends VenteObjet
{
	public VenteObjetAccepte(Objet obj)
	{
		super(obj);
		
		this.add(new JLabel("Accepté"));
	}
}
