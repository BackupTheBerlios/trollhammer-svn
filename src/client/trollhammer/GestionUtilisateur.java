package trollhammer;
import javax.swing.*;
import java.awt.*;

class GestionUtilisateur extends JPanel
{
    protected String login;
    protected String nom;
    protected String prenom;
    protected Color couleur_fond;
    protected Color couleur_selectionne;
	
	public GestionUtilisateur(Utilisateur u)
	{
        super();
        login = u.getLogin();
        nom = u.getNom();
        prenom = u.getPrenom();

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //à modifier quand il y aura l'image
		this.setBorder(BorderFactory.createEtchedBorder());
		this.add(new JLabel(login));
        this.add(new JLabel(prenom+" "+nom));

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

    String getLogin() {
        return this.login;
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
        for(Component c : this.getComponents()) {
            c.setForeground(Color.BLUE);
        }
    }
}
