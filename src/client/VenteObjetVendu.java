package trollhammer;
import javax.swing.*;
import java.awt.*;

class VenteObjetVendu extends VenteObjet
{
	public VenteObjetVendu(Objet obj)
	{
		super(obj);
		
		this.add(new JLabel("Vendu à "+prix_de_vente));
	}
}
