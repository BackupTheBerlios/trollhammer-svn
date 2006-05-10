package trollhammer;

/** Adaptateur Serveur pour la classe Objet.
 * Encapsule un Objet, en ajoutant les méthodes Objet spécifiques au Serveur.
 * Permet d'utiliser les mêmes getters/setters qu'une instance d'Objet.
 *
 * @author squelette : Julien Ruffin
 */
class ObjetServeur {

    Objet objet;

    public ObjetServeur(Objet o) {
        this.objet = o;
    }

    boolean invalider(String i) {
        return false;
    }

    boolean valider(String i) {
		if (objet.getStatut() == StatutObjet.Propose) {
			objet.setStatut(StatutObjet.Accepte);
			objet.setModerateur(i);
			return true;
		}
		else {
			return false;
		}
    }

    void sell(String i, Integer prix) {

    }
	
	Objet getObjet() {
		return objet;
	}
}
