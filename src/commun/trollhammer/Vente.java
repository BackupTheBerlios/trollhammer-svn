package trollhammer;
import java.util.List;
import java.util.ArrayList;

class Vente implements java.io.Serializable {

    private int id;
    private String nom;
    private String description;
    private long date;
    private Mode mode;
    private String superviseur;

    // une liste de POINTEURS sur des objets. Leurs IDs.
    // risque d'incohérences monstrueuses si on prend les objets
    // avec alors même qu'on fournit la liste dans les messages d'update. (jr)
    private List<Integer> objets;
	
	public Vente(int id, String nom, String description, long date, Mode mode, String superviseur) {
        this(); // créer la liste d'objets vide !
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.date = date;
		this.mode = mode;
		this.superviseur = superviseur;
	}

    /** Override du toString() par défaut, permet l'affichage du nom de la vente
     * dans la combo box de l'onglet 'Planifier'.
     * Implémenté de cette façon car d'autres solutions, notamment l'override du renderer, ne marchent pas.
     */
    public String toString() {
        return this.nom;
    }

    /** Constructeur par defaut.
    * Se contente d'initialiser la liste d'objets avec une ArrayList d'Integer.
    */
    Vente() {
        objets = new ArrayList<Integer>();
    }

    /** Redéfinition de l'affichage de la Vente.
     * Ainsi, l'affichage dans la liste des ventes du Client donne le nom
     * et non le combo classe/hashcode.
     */

    int getFirst() {
        return objets.get(0);
    }

    int removeFirst() {
        return objets.remove(0);
    }

    /* getters-setters and BLAH BLAH BLAH */

	// cfrey: accès au truc private
	List<Integer> getOIds() {
		return this.objets;
	}

	// cfrey: add "public" pour objets
	void addOId(int index, int elt) {
		this.objets.add(index, elt);
	}
	
	// cfrey: append "public" pour objets
	void addOId(int elt) {
		this.objets.add(elt);
	}

	// cfrey: enlève un objet de la liste par son id
	//		  si l'oid n'est pas dans la liste, on fait rien.
	void removeOId(int oid) {
		int p = this.getOIds().indexOf(oid);
		if (p != -1) {
			this.objets.remove(p);
		}
	}

	// probablement inutile, cf ci-dessus
    void setOIds(List<Integer> objs) {
        this.objets = objs;
    }

    int getId() {
        return this.id;
    }

    String getNom() {
        return this.nom;
    }

    String getDescription() {
        return this.description;
    }

    long getDate() {
        return this.date;
    }

    Mode getMode() {
        return this.mode;
    }

    String getSuperviseur() {
        return this.superviseur;
    }

    void setId(int id) {
        this.id = id;
    }

    void setNom(String nom) {
        this.nom = nom;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setDate(long date) {
        this.date = date;
    }

    void setMode(Mode mode) {
        this.mode = mode;
    }

    void setSuperviseur(String superviseur) {
        this.superviseur = superviseur;
    }

}
