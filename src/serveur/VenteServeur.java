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
 *
 * LS : je ne vois absolument pas ce que ça change pour toi.. car de toute 
 * façons tu créer un objet de ta classe adapter, non? de plus dans tout les cas 
 * tu dois sauvegarder d'abord le utilisateurs, puis les modérateurs... Le seul
 * truc que ça change c'est que moi derrière je me retrouve avec des boucles
 * qui font des appels de fonction inutils pour chaque objet / utilisateur de
 * la liste... et vu que l'on traite que ça... ça va être super efficace...
 * dans le genre
 * - selon moi -
 * en plus si on parle de faire des adapter selon les notions de
 * "Design patterns", et bien comment dire... je sais!!
 * __no comment__
 *
 */
 
class VenteServeur extends Vente {

	public VenteServeur(int id, String nom, String description, long date, Mode mode, String superviseur) {
		super(id, nom, description, date, mode, superviseur);
	}
	
	/**
	 * Utilisé lors de la déconnection d'un modérateur. Le VenteManager nous
	 * indique qu'un modérateur se déconnecte de la <b>vente en cours</b>.
	 *
	 * @param 	i	id modérateur qui se déconnecte de la vente en cours
	 * @author	cfrey
	 */
    void modoLeaving(String i) {
		// si le modérateur qui se déconnecte de la vente en cours est le
		// superviseur ...
		if (this.getSuperviseur() == i) {
			this.setSuperviseur(null);
			this.setMode(Mode.Automatique);
		}
    }

	/**
	 * Vérifie si un utilisateur est un superviseur de la vente ou pas.
	 *
	 * @param	s	identifiant utilisateur
	 * @return	True si l'utilisateur est un superviseur de la vente, False 
	 * 			sinon.
	 * @author	cfrey
	 */
    boolean isSuperviseur(String s) {
        return this.getSuperviseur() == s;
    }
	
	/**
	 * Vente de l'objet si ce n'est pas le superviseur qui est le dernier
	 * enchérisseur.
	 *
	 * @param	i		identifiant du dernier enchérisseur (donc acheteur)
	 * @param	prix	prix courant
	 * @param	oid		identifiant de l'objet
	 * @author	cfrey
	 */
    void sellObject(String i, int prix, int oid) {
		if (i != this.getSuperviseur()) {
			Serveur.objectmanager.sell(i, prix, oid);
		}
// si le dernier enchérisseur est le superviseur, pour l'instant il ne
// se passe rien ici ...
    }

	/**
	 * Coupe la tête de la liste des objets d'une vente.
	 *
	 * @return	l'objet qui était en tête de la liste
	 * @author	cfrey
	 */
    Objet removeHead() {
        return Serveur.objectmanager.getObjet(this.removeFirst()).getObjet();
    }

    void insertObject(int o, int p, String u, long date) {
		// NB: modifié les types du 1er et du 3ème paramètre => que des ID
    }

    void removeObject(Objet o, Utilisateur c) {

    }
    
    /**
     * Retourne la liste des objets de la vente, qui sont au niveau de la vente
     * stockés sous forme d'une liste d'identifiants.
     *
     * @return	la liste des objets de la vente
     * @author	cfrey
     */
	List<Objet> getObjets() {
		List<Objet> r = new ArrayList<Objet>();
		for (Integer oid : this.getOIds()) {
			r.add(Serveur.objectmanager.getObjet(oid).getObjet());
		}
		return r;
	}

}
