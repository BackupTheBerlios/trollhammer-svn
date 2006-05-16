package trollhammer.client.gui;
import trollhammer.client.*;
import trollhammer.commun.*;
import javax.swing.*;
import java.awt.*;
import com.jgoodies.forms.layout.CellConstraints;

class GestionUtilisateur extends CoolPanel
{
    private String login;
    private String nom = "";
    private String prenom = "";
    private Color couleur_fond;
    private Color couleur_selectionne;
	private FreshPanel infoPanel = null;
	private JLabel loginLabel = null;
	private JLabel nomLabel = null;
	private ImageIcon img = null;
	
	public GestionUtilisateur(Utilisateur u)
	{
		this(u, new ImageIcon(System.getProperty("user.dir")+"/ressources/img/user.gif"));
	}
	
	public GestionUtilisateur(Utilisateur u, ImageIcon img)
	{
        super("pref, pref","pref");
		//this.setRowGroups(new int[][] {{1,2}});
        login = u.getLogin();
        nom = u.getNom();
        prenom = u.getPrenom();
		this.img = img;
		//this.setBorder(BorderFactory.createEtchedBorder());
		loginLabel = new JLabel(login);
		if(!(nom.equals("") || prenom.equals("")))
			nomLabel = new JLabel("( "+prenom+", "+nom+" )");
		else
			nomLabel = new JLabel(" ");
		if(u.getStatut() == StatutLogin.Banni)
			setColor(Color.RED);
		infoPanel = new FreshPanel('y',false);
		infoPanel.add(loginLabel);
		infoPanel.add(nomLabel);
		this.addC(new JLabel(img), new CellConstraints(1,1));
		this.addC(infoPanel, new CellConstraints(2,1));

        couleur_fond = this.getBackground();
        couleur_selectionne = Color.LIGHT_GRAY;
    }

    void selectionne(boolean estSelectionne) {
        if(estSelectionne) {
            this.setBackground(couleur_selectionne);
			infoPanel.setBackground(couleur_selectionne);
        } else {
            this.setBackground(couleur_fond);
			infoPanel.setBackground(couleur_fond);
        }
    }

    public String getLogin() {
        return this.login;
    }
	protected void setColor(Color c)
	{
		loginLabel.setForeground(c);
		nomLabel.setForeground(c);
	}

    // l'éternel 'nouvel utilisateur' !
    static final GestionUtilisateur nouvel_utilisateur =
        new GestionUtilisateur(
                new Utilisateur("Nouvel Utilisateur", "", "", "")
                );
}

class GestionModerateur extends GestionUtilisateur {
	
    public GestionModerateur(Moderateur m) {
        super(m, new ImageIcon(System.getProperty("user.dir")+"/ressources/img/modo.gif"));
        // petit hack en attendant d'avoir les images :
        // mettre le texte des modos en bleu.
        /*for(Component c : this.getComponents()) {
            c.setForeground(Color.BLUE);
        }*/
		if(m.getStatut() != StatutLogin.Banni)
			setColor(Color.BLUE);
		else
			setColor(Color.RED);
		
    }
}
