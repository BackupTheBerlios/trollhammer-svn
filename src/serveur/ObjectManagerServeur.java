package trollhammer;
import java.util.Set;
import java.util.HashSet;

/** Classe Manager des Objets du Serveur.
 * Regroupe et gère tous les Objets (au sens Trollhammer, pas Java).
 * 
 * @author squelette : Julien Ruffin
 */
class ObjectManagerServeur {
	int lastID = -1;

    Set<ObjetServeur> objets;
	
	/* Constructeur qui initalise une base d'objet vide. */
	public ObjectManagerServeur() {
		objets = new HashSet<ObjetServeur>();
	}
	
	ObjetServeur getObjet(int oid) {
        for(ObjetServeur o : objets) {
            if (o.getObjet().getId() == oid) {
				return o;
			}
        }
		return null;
	}
	
	
    /* méthodes du design */
    void invaliderProposition(int i, String sender) {

    }

	
    void obtenirListeObjets(Onglet t, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		Set<Objet> liste = new HashSet<Objet>();
		switch (t) {
		case Vente:
			for (ObjetServeur o : objets) {
				if (o.getObjet().getVendeur() == sender) {
					liste.add(o.getObjet());
				}
			}
			break;
		case Achat:
			for (ObjetServeur o : objets) {
				if (o.getObjet().getAcheteur() == sender) {
					liste.add(o.getObjet());
				}
			}
			break;
		case Validation:
			for (ObjetServeur o : objets) {
				if (o.getObjet().getStatut() == StatutObjet.Propose) {
					liste.add(o.getObjet());
				}
			}
			break;
		case Planification:
			for (ObjetServeur o : objets) {
				if (o.getObjet().getStatut() == StatutObjet.Accepte) {
					liste.add(o.getObjet());
				}
			}
			break;
		default : 
		}
		u.listeObjets(t, liste);
    }

    void validerProposition(int oid, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		ObjetServeur o = Serveur.objectmanager.getObjet(oid);
		boolean r = false;
		
		if (o != null) {
			
		}
	}

	// ls : probleme de concurrence, faudra a faire gaffe que ce soit synchro
    void add(ObjetServeur o, String i) {
		o.getObjet().setId(++lastID);
		objets.add(o);
    }

    void listeObjets(Onglet t, Set<Objet> ol) {

    }

    void updateListe(Set<Objet> ol) {

    }

    void update(Objet o) {

    }
	
}
