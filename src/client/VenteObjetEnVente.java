package trollhammer;
import javax.swing.*;
import java.awt.*;

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
