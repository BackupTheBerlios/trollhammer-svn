//J'aime tester ma GUI sans devoir tjs me connecter au serveur!
package trollhammer.client.gui;
import trollhammer.client.*;
import trollhammer.commun.*;
import javax.swing.SwingUtilities;
public class TestGUI
{
	public static void main(String[]args)
	{	
		SwingUtilities.invokeLater(new Runnable()
								   {
										public void run()
										{
											new Window(true,null);
										}
								   });
	}
}
