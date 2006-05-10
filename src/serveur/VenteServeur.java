package trollhammer;
import java.util.List;
import java.util.ArrayList;

/**
 * Sous-classe Adaptateur côté Serveur pour Vente.
 * Permet d'exécuter les opérations sur la vente
 * spécifiques au Serveur.
 */

/* jr : faites-en ce que vous voulez ! sous-type ou pas.
 * Ce bout de squelette date d'avant que je réalise
 * que l'approche 'référence' était
 * - selon moi - 
 * meilleure pour l'Utilisateur.
 * Surtout par rapport au Modérateur.
 */
 
class VenteServeur extends Vente {

    void modoLeaving(String i) {

    }

    boolean isSuperviseur(String s) {
        return false;
    }

    boolean checkPAF(String s) {
        return false;
    }

    void sellObject(String i, int prix) {

    }

    Objet removeHead() {
        return null;
    }

    void insertObject(int o, int p, String u, long date) {
		// NB: modifié les types du 1er et du 3ème paramètre => que des ID
    }

    void removeObject(Objet o, Utilisateur c) {

    }
    
    // cfrey: eheh
	List<Objet> getObjets() {
		List<Objet> r = new ArrayList<Objet>();
		for (Integer oid : this.getOIds()) {
			r.add(Serveur.objectmanager.getObjet(oid).getObjet());
		}
		return r;
	}

}
