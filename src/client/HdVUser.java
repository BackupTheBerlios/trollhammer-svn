package trollhammer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HdVUser extends JRadioButton /*implements ActionListener*/
{
	private String login;
	private boolean modo;
	private boolean sup;
	public HdVUser(String login, boolean modo)
	{
		super(login);
		this.modo = modo;
		this.login = login;
		if(modo)
		{
			this.setIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/modoIcon.gif"));
			this.setForeground(Color.BLUE);
			this.setSelectedIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/modoIconSelected.gif"));
		}
		else
		{
			this.setIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/userIcon.gif"));
			this.setForeground(Color.BLACK);
			this.setSelectedIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/userIconSelected.gif"));
		}
		
	}
	public HdVUser(String login, boolean superviseur, boolean modo)
	{
		super(login);
		this.modo=modo;
		this.login=login;
		sup = superviseur;
		this.setIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/modoIcon.gif"));
		this.setSelectedIcon(new ImageIcon(System.getProperty("user.dir")+"/ressources/img/modoIconSelected.gif"));
		this.setForeground(new Color(217,217,25));	//Gold
			
	}
}
