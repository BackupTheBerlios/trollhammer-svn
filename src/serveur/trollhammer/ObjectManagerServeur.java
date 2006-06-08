package trollhammer;
import java.util.Set;
import java.util.HashSet;

/** Classe Manager des Objets du Serveur.
 * Regroupe et gère tous les Objets (au sens Trollhammer, pas Java).
 * 
 * @author squelette : Julien Ruffin
 */
class ObjectManagerServeur {
	private int lastID = -1;

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
		
        // jr : modifié un peu l'ordre des choses, car selon le Protocol Model,
        // cette opération renvoie d'abord la liste des objets et ensuite
        // le résultat de l'opération. Pour que la liste soit à jour,
        // il faut déjà avoir effectué l'opération d'invalidation lorsqu'on
        // l'envoie, sinon elle n'est pas à jour. C'est ensuite seulement
        // que l'on regarde si elle a réussi, et envoie une réponse en fonction.
		if (o != null) {
            boolean invalidationok = o.invalider(sender);
            this.obtenirListeObjets(Onglet.Validation, sender);
			if (invalidationok) {
				u.resultatEdition(StatutEdition.Reussi);
			} else {
				u.resultatEdition(StatutEdition.DejaEffectue);
			}
		} else {
			u.resultatEdition(StatutEdition.NonTrouve);
		}
    }

    void validerProposition(int oid, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		ObjetServeur o = Serveur.objectmanager.getObjet(oid);
		
        // jr : voir le commentaire ci-dessus, c'est la même modification.
		if (o != null) {
            boolean validationok = o.valider(sender);
            this.obtenirListeObjets(Onglet.Validation, sender);
			if (validationok) {
				u.resultatEdition(StatutEdition.Reussi);
			} else {
				u.resultatEdition(StatutEdition.DejaEffectue);
			}
		} else {
			u.resultatEdition(StatutEdition.NonTrouve);
		}		
	}
	
    void obtenirListeObjets(Onglet t, String sender) {
		UtilisateurServeur u = Serveur.usermanager.getUtilisateur(sender);
		Set<Objet> liste = new HashSet<Objet>();
		switch (t) {
		case Vente:
			for (ObjetServeur o : objets) {
				if (o.getVendeur().equals(sender)) {
					liste.add(o);
				}
			}
			break;
		case Achat:
			for (ObjetServeur o : objets) {
				if (o.getAcheteur() != null && o.getAcheteur().equals(sender)) {
					liste.add(o);
				}
			}
			break;
		case Validation:
			for (ObjetServeur o : objets) {
				if (o.getStatut() == StatutObjet.Propose) {
					liste.add(o);
				}
			}
			break;
		case Planification:
			for (ObjetServeur o : objets) {
				if (o.getStatut() == StatutObjet.Accepte) {
					liste.add(o);
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
	
	//getter pour sauvegarder le truc
	public int getLastId() {
		return lastID;
	}

	public void setLastId(int truc) {
		this.lastID = truc;
	}

    public Set<ObjetServeur> getObjets() {
		return objets;
	}
}
