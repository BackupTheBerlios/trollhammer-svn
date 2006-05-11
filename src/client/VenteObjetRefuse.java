package trollhammer;
import javax.swing.*;
import java.awt.*;

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
