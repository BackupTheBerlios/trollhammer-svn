package trollhammer;
import javax.swing.*;
import java.awt.*;

class GestionUtilisateur extends JPanel
{
    private String login;
    private String nom = "";
    private String prenom = "";
    private Color couleur_fond;
    private Color couleur_selectionne;
	private JLabel loginLabel = null;
	private JLabel nomLabel = null;
	
	public GestionUtilisateur(Utilisateur u)
	{
        super();
        login = u.getLogin();
        nom = u.getNom();
        prenom = u.getPrenom();

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //à modifier quand il y aura l'image
		this.setBorder(BorderFactory.createEtchedBorder());
		loginLabel = new JLabel(login);
		if(!(nom.equals("") || prenom.equals("")))
			nomLabel = new JLabel("( "+prenom+" )"+" "+"( "+nom+" )");
		else
			nomLabel = new JLabel(" ");
		if(u.getStatut() == StatutLogin.Banni)
			setColor(Color.RED);
		this.add(loginLabel);
        this.add(nomLabel);

        couleur_fond = this.getBackground();
        couleur_selectionne = Color.LIGHT_GRAY;
    }

    void selectionne(boolean estSelectionne) {
        if(estSelectionne) {
            this.setBackground(couleur_selectionne);
        } else {
            this.setBackground(couleur_fond);
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
        super(m);

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
