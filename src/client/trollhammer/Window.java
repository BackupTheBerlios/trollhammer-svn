package trollhammer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.Vector;

/* à cause du relais des méthodes de HI */
import java.util.Set;
import java.util.List;

class Window implements ActionListener
{
	private boolean modo = false;
	private JFrame frame = null;  //  @jve:decl-index=0:visual-constraint="0,0"
	private JMenuBar menuBar = null;
	private JMenu fichierMenu = null;
	private JMenu helpMenu = null;
	private JTabbedPane tabbedPane = null;
	//Panels de l'onglets HDV
	private HdVPanel hdv = null;
	//Panel Vente
	private VentePanel vente = null;
	//Panel Achat
	private AchatPanel achat = null;
	//Panel Valider MODO
	private ValiderPanel valider = null;
	//panel Planifier MODO
	private PlanifierPanel planifier = null;
	//Panel Gestion MODO
	private GestionPanel gestion = null;

    // la LoginWindow. On la passe au début, et la garde pour
    // la rendre visible à nouveau à la déconnexion (callback)
    private LoginWindow lw = null;
	
	public Window(boolean modo, LoginWindow lw)
	{
		this.modo = modo;
        this.lw = lw;
		getFrame();
	}


	/**
	 * This method initializes frame	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	private JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame("Trollhammer");
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setBounds(dim.width/2-500,0,1000,800);
			frame.setMinimumSize(new Dimension(800,600));
			frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

            // on veut détruire la fenêtre Trollhammer, mais certainement pas
            // tout quitter quand on la ferme : il faut faire un logout !
            frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

            // ce que l'on décide de faire ici...
            
            
            {
                /* juste pour garder la référence et l'utiliser dans l'Adapter
                 * (c'est absolument dégueulasse, je sais)
                 */
                final Window w = this;
                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        w.doLogout();
                    }
                });
            }
			frame.setVisible(true);
			frame.setJMenuBar(getMenuBar());
			frame.setContentPane(getTabbedPane());
		}
		return frame;
	}
	
	/**
	 * This method initializes menuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getFichierMenu());
			menuBar.add(getHelpMenu());

            final Window w = this;
            getFichierMenu().add(new JMenuItem(new javax.swing.AbstractAction("Déconnexion"){
                public void actionPerformed(ActionEvent e) {
                    w.doLogout();
                }
            }));
		}
		return menuBar;
	}

	/**
	 * This method initializes fichierMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFichierMenu() {
		if (fichierMenu == null) {
			fichierMenu = new JMenu("Fichier");
		}
		return fichierMenu;
	}
	/**
	 * This method initializes helpMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu("?");
		}
		return helpMenu;
	}

	/**
	 * This method initializes tabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
			hdv = new HdVPanel(modo, this);
			vente = new VentePanel(modo);
			achat = new AchatPanel(modo);
            // ATTENTION : SI MODIFICATION DE L'ORDRE DES ONGLETS,
            //             ALORS LE REPERCUTER DANS LE LISTENER
            //             CI-DESSOUS !
			tabbedPane.addTab("Hotel des ventes", null, hdv.getComponent(), null);
			tabbedPane.addTab("Vente", null, vente.getComponent(), null);
			tabbedPane.addTab("Achat", null, achat.getComponent(), null);
			if(modo)
			{
				valider = new ValiderPanel(modo);
				planifier = new PlanifierPanel(modo);
				gestion = new GestionPanel(modo);
				tabbedPane.addTab("Valider", null, valider.getComponent(), null);
				tabbedPane.addTab("Planifier", null, planifier.getComponent(), null);
				tabbedPane.addTab("Gestion des utilisateurs", null, gestion.getComponent(), null);
			}

            /* Listener s'occupant de gérer les changements dans la barre d'onglets.
             * Lors d'un changement de tab sélectionné, le Listener va voir lequel
             * est effectivement actif et lance un HI.voir() avec le bon argument.
             * C'est à moitié contraire au design, où voir() peut _diriger_
             * quel tab afficher. En pratique, comme la fonction 'directrice' de
             * voir() n'est utilisée qu'à l'affichage de la fenêtre principale,
             * il est possible de la rendre implicite en faisant cet affichage
             * dans la transition de la fenêtre de Login à la fenêtre principale,
             * et de ne pas implémenter la fonction "voir() change les tabs", ce qui
             * serait en soi particulièrement bizarre à implémenter...
             */
            tabbedPane.addChangeListener(new ChangeListener(){
                public void stateChanged(ChangeEvent e) {
                    // récupération de l'index de l'Onglet fraîchement sélectionné
                    // (il serait possible de le faire en fonction de son titre aussi)
                    int onglet = tabbedPane.getSelectedIndex();

                    switch(onglet) {
                        case 0: // hôtel des ventes
                            Client.hi.voir(Onglet.HotelDesVentes); break;
                        case 1: // vente
                            Client.hi.voir(Onglet.Vente); break;
                        case 2: // achat
                            Client.hi.voir(Onglet.Achat); break;
                        case 3: // valider
                            Client.hi.voir(Onglet.Validation); break;
                        case 4: // planifier
                            Client.hi.voir(Onglet.Planification); break;
                        case 5: // gestion des utilisateurs
                            Client.hi.voir(Onglet.GestionUtilisateurs); break;
                    }
                }
            });
		}
		return tabbedPane;
	}

	public void actionPerformed(ActionEvent event)
	{
		
		
	}

    /** Lancer la déconnexion, fermer la fenêtre, revenir à la fenêtre de Login.
     * Utilisé lorsque la fenêtre est fermée, ou que l'utilisateur choisit de
     * se déconnecter.
     */
    public void doLogout() {
        Client.hi.executer(Action.Deconnecter);
        frame.setVisible(false);
        lw.setVisible(true);
    }

    /* les méthodes de HI sont relayées ici ! */

    void affichage(Evenement e) {

    }

    void affichageChat(String m, String i) {
        hdv.affichageChat(m, i);
    }

    void affichageEnchere(Integer prix, String i) {

    }

    void affichageObjet(Objet o) {

    }

    void affichageListeObjets(Set<Objet> ol) {
        switch(Client.client.getMode()) {
            case Vente:
                this.vente.affichageListeObjets(ol);
            case Validation:
                this.valider.affichageListeObjets(ol);
        }
    }

    void affichageUtilisateur(Utilisateur i) {

    }

    void affichageListeUtilisateurs(Set<Utilisateur> ul) {

    }

    void affichageListeParticipants(Set<Participant> pl) {
        hdv.affichageListeParticipants(pl);
    }

    /* modif p.r. au design : argument change de int i => Vente v, sinon,
     * je ne comprends pas à quoi ça sert ! (jr)
     */
    void affichageVente(Vente v) {

    }

    /* rajouté du design : oublié pendant la rédaction du DCD !!! (jr) */
    void affichageListeVentes(Set<Vente> vl) {

    }

    void message(Notification n) {

    }

    void messageErreur(Erreur e) {

    }
}