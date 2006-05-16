package trollhammer;
import javax.swing.*;
import java.awt.*;

public class HdVUser extends JRadioButton
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

        // mettre son nom en rouge non en gras !
        if(login.equals(Client.session.getLogin())) {
            //this.setForeground(new Color(33,94,33));
			this.setFont(new Font(this.getFont().getFontName(),Font.BOLD,this.getFont().getSize()+1));
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

    String getLogin() {
        return this.login;
    }
}
