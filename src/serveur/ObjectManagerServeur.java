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
            if (o.getId() == oid) {
				return o;
			}
        }
		return null;
	}
	
	
    /* méthodes du design */
    void invaliderProposition(int i, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		ObjetServeur o = Serveur.objectmanager.getObjet(i);
		
		if (o != null) {
			if (o.invalider(sender)) {
				u.resultatEdition(StatutEdition.Reussi);
			} else {
				u.resultatEdition(StatutEdition.DejaEffectue);
			}
		} else {
			u.resultatEdition(StatutEdition.NonTrouve);
		}
		this.obtenirListeObjets(Onglet.Validation, sender);
    }

    void validerProposition(int oid, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		ObjetServeur o = Serveur.objectmanager.getObjet(oid);
		
		if (o != null) {
			if (o.valider(sender)) {
				u.resultatEdition(StatutEdition.Reussi);
			} else {
				u.resultatEdition(StatutEdition.DejaEffectue);
			}
		} else {
			u.resultatEdition(StatutEdition.NonTrouve);
		}		
		this.obtenirListeObjets(Onglet.Validation, sender);
	}
	
    void obtenirListeObjets(Onglet t, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		Set<Objet> liste = new HashSet<Objet>();
		switch (t) {
		case Vente:
			for (ObjetServeur o : objets) {
				if (o.getVendeur().equals(sender)) {
					liste.add(o.getObjet());
				}
			}
			break;
		case Achat:
			for (ObjetServeur o : objets) {
				if (o.getAcheteur() != null && o.getAcheteur().equals(sender)) {
					liste.add(o.getObjet());
				}
			}
			break;
		case Validation:
			for (ObjetServeur o : objets) {
				if (o.getStatut() == StatutObjet.Propose) {
					liste.add(o.getObjet());
				}
			}
			break;
		case Planification:
			for (ObjetServeur o : objets) {
				if (o.getStatut() == StatutObjet.Accepte) {
					liste.add(o.getObjet());
				}
			}
			break;
		default : 
		}
		u.listeObjets(t, liste);
    }

	// ls : probleme de concurrence, faudra a faire gaffe que ce soit synchro
    void add(ObjetServeur o, String i) {
		o.setId(++lastID);
		objets.add(o);
    }

    /**
     * "Vend" l'objet, c-à-d affecte son prix de vente, l'acheteur, et change 
     * son statut.
     *
     * @param	i		acheteur
     * @param	prix	prix
     * @param	oid		identifiant de l'objet
     * @author	cfrey
     */
    void sell(String i, int prix, int oid) {
    	ObjetServeur o = this.getObjet(oid);
    	
    	if (o != null) {
    		o.setPrixDeVente(prix);
    		o.setAcheteur(i);
    		o.setStatut(StatutObjet.Vendu);
    	}
// là on a un problème si o == null mais que faire ?
    }
	
}
