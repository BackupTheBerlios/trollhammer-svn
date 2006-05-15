package trollhammer;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>Modèlise une vente aux enchères.</p>
 */
class Vente implements java.io.Serializable {

    private int id;
    private String nom;
    private String description;
    private long date;
    private Mode mode;
    private String superviseur;
    private List<Integer> objets;
	
	// Constructeurs : START
	Vente(int id, String nom, String description, long date, Mode mode, String superviseur) {
        this(); // créer la liste d'objets vide !
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.date = date;
		this.mode = mode;
		this.superviseur = superviseur;
	}

    Vente(int id, String nom, String description, long date, Mode mode,
            String superviseur, List<Integer> oids) {
        this(id, nom, description, date, mode, superviseur);
        this.objets = oids;
    }
	// Constructeurs : END

    /** Override du toString() par défaut, permet l'affichage du nom de la vente
     * dans la combo box de l'onglet 'Planifier'.
     * Implémenté de cette façon car d'autres solutions, notamment l'override du renderer, ne marchent pas.
     */
    public String toString() {
        return this.nom;
    }

    /**
	 * <p>Constructeur par defaut. Se contente d'initialiser la liste d'objets
	 * avec une ArrayList d'Integer.</p>
     */
    Vente() {
        objets = new ArrayList<Integer>();
    }

	// Méthodes du design : START
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
        Logger.log("Vente", 2, LogType.DBG, "Insertion de l'OID "+elt
                +" à la position "+index+" réussi");
	}
	
	// cfrey: append "public" pour objets
	void addOId(int elt) {
		this.objets.add(elt);
        Logger.log("Vente", 2, LogType.DBG, "Insertion de l'OID "+elt
                +" en fin de liste réussi"); 
	}

	// cfrey: enlève un objet de la liste par son id
	//		  si l'oid n'est pas dans la liste, on fait rien.
	void removeOId(int oid) {
		int p = this.getOIds().indexOf(oid);
		if (p != -1) {
			this.objets.remove(p);
		}
	}
	// Méthodes du design : END
	
	// Setters & Getters : START
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
   	// Setters & Getters : END
}
