//J'aime tester ma GUI sans devoir tjs me connecter au serveur!
package trollhammer;

public class TestGUI
{
	public static void main(String[]args)
	{	
		SwingUtilities.invokeLater(new Runnable()
								   {
										public void run()
										{
											new Window(true);
										}
								   });
	}
}
