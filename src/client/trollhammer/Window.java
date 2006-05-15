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
import java.awt.event.KeyEvent;

/* à cause du relais des méthodes de HI */
import java.util.Set;
import java.util.List;

class Window implements ActionListener
{
	private boolean modo = false;
	private JFrame frame = null;  //  @jve:decl-index=0:visual-constraint="0,0"
	private JMenuBar menuBar = null;
	private JMenu fichierMenu = null;
	private JMenuItem decoMenuItem = null;
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
	public Window getWindow()
	{
		return this;
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
			frame.setBounds(dim.width/2-400,0,800,600);
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
			try
			{
				frame.setContentPane(getTabbedPane());
			} catch (Exception e) {Logger.log("Window",0,"setContentPane(getTabbedPane())... "+e.getMessage()); e.printStackTrace();}
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
			
            final Window w = this;
			decoMenuItem = new JMenuItem(new javax.swing.AbstractAction("Déconnexion") {
                public void actionPerformed(ActionEvent e)
				{
                    w.doLogout();
                }
            });
			decoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.META_MASK));
			fichierMenu.add(decoMenuItem);
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
			try
			{
				tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
				hdv = new HdVPanel(modo, getWindow());tabbedPane.addTab("Hotel des ventes", null, hdv.getComponent(), null);
				vente = new VentePanel(modo);tabbedPane.addTab("Vente", null, vente.getComponent(), null);
				achat = new AchatPanel(modo);tabbedPane.addTab("Achat", null, achat.getComponent(), null);
			} catch (Exception e) {Logger.log("Window",0,"Initialisation et Ajout des Tabs au tabbedPane... "+e.getMessage()); e.printStackTrace();}
			// ATTENTION : SI MODIFICATION DE L'ORDRE DES ONGLETS,
			//             ALORS LE REPERCUTER DANS LE LISTENER
			//             CI-DESSOUS !
				
			if(modo)
			{
				valider = new ValiderPanel(modo);tabbedPane.addTab("Valider", null, valider.getComponent(), null);
				planifier = new PlanifierPanel(modo);tabbedPane.addTab("Planifier", null, planifier.getComponent(), null);
				gestion = new GestionPanel(modo);tabbedPane.addTab("Gestion des utilisateurs", null, gestion.getComponent(), null);
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
			try
			{
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
			} catch (Exception e) {Logger.log("Window",0,"le code de Rulius... "+e.getMessage()); e.printStackTrace();}
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

    void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
	
    /* les méthodes de HI sont relayées ici ! */
	
    void affichage(Evenement e) {
        hdv.affichage(e);
    }
	
    void affichageChat(String m, String i) {
        hdv.affichageChat(m, i);
    }
	
    void affichageEnchere(Integer prix, String i) {
        hdv.affichageEnchere(prix, i);
    }
	
    void affichageObjet(Objet o) {
        switch(Client.client.getMode()) {
            case Vente:
                this.vente.affichageObjet(o); break;
        }
    }
	
    void affichageListeObjets(Set<Objet> ol) {
        switch(Client.client.getMode()) {
            case Vente:
                this.vente.affichageListeObjets(ol); break;
            case Validation:
                this.valider.affichageListeObjets(ol); break;
            case Planification:
                this.planifier.affichageListeObjets(ol); break;
        }
    }
	
    void affichageUtilisateur(Utilisateur i) {
        this.gestion.affichageUtilisateur(i);
    }
	
    void affichageListeUtilisateurs(Set<Utilisateur> ul) {
        this.gestion.affichageListeUtilisateurs(ul);
    }
	
    void affichageListeParticipants(Set<Participant> pl) {
        hdv.affichageListeParticipants(pl);
    }
	
    /* modif p.r. au design : argument change de int i => Vente v, sinon,
		* je ne comprends pas à quoi ça sert ! (jr)
		*/
    void affichageVente(Vente v) {
        switch (Client.client.getMode()) {
            case Planification:
                planifier.affichageVente(v); break;
            case HotelDesVentes:
                // pas encore implémenté
                hdv.affichageVente(v); break;
        }
    }
	
    /* rajouté du design : oublié pendant la rédaction du DCD !!! (jr) */
    void affichageListeVentes(Set<Vente> vl) {
        planifier.affichageListeVentes(vl);
    }
	
    void message(Notification n) {
        switch (n) {
            // jr : j'ai pensé que c'eût été bien de logger aussi les démarrages/fins
            // et ventes en cours, en plus de leur fenêtre de dialogue.
            case DebutVente:
                JOptionPane.showMessageDialog(this.frame, "Démarrage d'une vente.");
                hdv.message(n);
                break;
            case VenteEnCours:
                // TRES ennuyeux.
                //JOptionPane.showMessageDialog(this.frame, "Une vente est en cours.");
                hdv.message(n);
                break;
            case FinVente:
                JOptionPane.showMessageDialog(this.frame, "Fin de la vente.");
                hdv.message(n);
                break;
            case LogOut:
                // jr : n'arrive jamais, car la déconnexion côté client se
                // fait avant même que la notification de logout n'arrive...
                // j'ai vérifié, et le Client attend pourtant réellement
                // que le Serveur interrompe la connexion, il ne fait rien
                // lui-même !
                // Il existe aussi le problème que si une déconnexion,
                // forcée ou non (à part kick) se passe, la fenêtre est
                // fermée de force, et la boîte de dialogue associée avec.
                JOptionPane.showMessageDialog(this.frame, "Vous êtes déconnecté.");
                break;
            case Deconnexion:
                // idem que ci-dessus.
                JOptionPane.showMessageDialog(this.frame,
                        "Vous avez été déconnecté du serveur.");
                break;
            case Kicke:
                // ça, par contre, je vous jure que ça arrive.
                JOptionPane.showMessageDialog(this.frame,
                        "Vous avez été expulsé du serveur.");
                break;
        }
    }
	
    void messageErreur(Erreur e) {
        switch (e) {
            case Deconnecte:
                JOptionPane.showMessageDialog(this.frame,
                        "Erreur : Déconnecté.");
                break;
            case Invalide:
                JOptionPane.showMessageDialog(this.frame,
                        "Erreur : Invalide.");
                break;
            case Banni:
                // pas sensé arriver ?
                JOptionPane.showMessageDialog(this.frame,
                        "Erreur : Vous avez été banni de ce serveur.");
                break;
            case ExisteDeja:
                JOptionPane.showMessageDialog(this.frame,
                        "Erreur : Existe déjà.");
                break;
            case NonTrouve:
                JOptionPane.showMessageDialog(this.frame,
                        "Erreur : Non trouvé.");
                break;
            case DejaEffectue:
                JOptionPane.showMessageDialog(this.frame,
                        "Erreur : Déjà effectué.");
                break;
        }
    }
}
