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
	 */
    void modoLeaving(String i) {
		// si le modérateur qui se déconnecte de la vente en cours est le
		// superviseur ...
		if (this.getSuperviseur().equals(i)) {
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
	 */
    boolean isSuperviseur(String s) {
        return this.getSuperviseur().equals(s);
    }
	
	/**
	 * Vente de l'objet si ce n'est pas le superviseur qui est le dernier
	 * enchérisseur.
	 *
	 * @param	i		identifiant du dernier enchérisseur (donc acheteur)
	 * @param	prix	prix courant
	 */
	 //ls : arrrg, encors ces foutu probleme OO...
    void sellObject(String i, int prix) {
		if (i != this.getSuperviseur()) {
			ObjetServeur o = new ObjetServeur(this.removeHead());
			o.sell(i, prix);
		}
// si le dernier enchérisseur est le superviseur, pour l'instant il ne
// se passe rien ici ...
    }

	/**
	 * Coupe la tête de la liste des objets d'une vente.
	 *
	 * @return	l'objet qui était en tête de la liste
	 */
    Objet removeHead() {
        return Serveur.objectmanager.getObjet(this.removeFirst()).getObjet();
    }

	/**
	 * Ajoute un objet (id) dans la liste des objets (ids) d'une vente. Si la
	 * position d'insertion vaut -1, cela signifie insertion à la fin de la
	 * liste.
	 *
	 * @param	oid		identifiant de l'objet à insérer
	 * @param	p		position dans la liste
	 * @param	u		identifiant du modérateur qui veut insérer l'objet ???
	 *					pas utilisé ici, à voir, je sais pas ce qu'on avait bu
	 *					quand on faisait les schémas ...
	 * @param	date	date de l'insertion (dateCourante dans 
	 *					insérerObjetVente) ??? pas utilisé ici, à voir
	 */
    void insertObject(int oid, int p, String u, long date) {
		if (-1 < p && p < this.getOIds().size()) {
			this.addOId(p, oid);
		} else {
			// à la place d'avoir un index out of bound, on insère implicitement
			// à la fin de la liste et ça gère donc le comportement spécifié 
			// pour p == -1
			this.addOId(oid);
		}
		// l'insertion ne peut pas échouer, on modifier des propriétés de
		// l'objet, mais pour l'instant ça n'existe pas ...
		// ObjetServeur o = Serveur.objectmanager.getObjet(oid);
		// if (o != null) {
		// 	o.setDateInsertion = date;
		// 	o.setModérateurQuiAAjoutéL'ObjetALaVente = u;
		// }
    }

	/**
	 * Enlève de la liste des objets d'une vente un objet identifié par son 
	 * identifiant (pas sa position).
	 *
	 * @param	oid		identifiant de l'objet
	 * @param	u		identifiant de l'utilisateur ??? pas utilisé ici, à voir
	 */
    void removeObject(int oid, String u) {
		this.removeOId(oid);
    }
    
    /**
     * Retourne la liste des objets de la vente, qui sont au niveau de la vente
     * stockés sous forme d'une liste d'identifiants.
     *
     * @return	la liste des objets de la vente
     */
	List<Objet> getObjets() {
		List<Objet> r = new ArrayList<Objet>();
		for (Integer oid : this.getOIds()) {
			r.add(Serveur.objectmanager.getObjet(oid).getObjet());
		}
		return r;
	}
}
