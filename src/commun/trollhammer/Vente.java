package trollhammer;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>Modèlise une vente aux enchères. Implémente les opérations qui sont
 * utilisées tant par le client que le serveur, ainsi que les getter / setter</p>
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
    /**
	 * <p>Constructeur par defaut. Se contente d'initialiser la liste d'objets
	 * avec une ArrayList d'Integer.</p>
     */
    Vente() {
        objets = new ArrayList<Integer>();
    }

	/**
	 * <p>Constructeur d'une vente, sans lui affecter sa liste d'objets.</p>
	 *
	 * @param	id			Integer qui sert d'identificateur unique de la vente.
	 * @param	nom			Nom de la vente.
	 * @param	description	Description libre de la vente.
	 * @param	date		Date à laquelle la vente doit se passer.
	 * @param	mode		Mode de la vente, ie Automatique ou Manuel pour 
	 *						l'adjudication des objets.
	 * @param	superviseur	L'id du superviseur de la vente, c'est-à-dire le 
	 *						modérateur responsable de donner les coups de marteau.
	 */
	Vente(int id, String nom, String description, long date, Mode mode, String superviseur) {
        this(); // créer la liste d'objets vide !
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.date = date;
		this.mode = mode;
		this.superviseur = superviseur;
	}

	/**
	 * <p>Constructeur d'une vente complet.</p>
	 *
	 * @param	id			Integer qui sert d'identificateur unique de la vente.
	 * @param	nom			Nom de la vente.
	 * @param	description	Description libre de la vente.
	 * @param	date		Date à laquelle la vente doit se passer.
	 * @param	mode		Mode de la vente, ie Automatique ou Manuel pour 
	 *						l'adjudication des objets.
	 * @param	superviseur	L'id du superviseur de la vente, c'est-à-dire le 
	 *						modérateur responsable de donner les coups de marteau.
	 * @param	oids		La liste  des identificateurs d'objets qui seront 
	 *						vendu dans cette vente.
	 */
    Vente(int id, String nom, String description, long date, Mode mode,
            String superviseur, List<Integer> oids) {
        this(id, nom, description, date, mode, superviseur);
        this.objets = oids;
    }
	// Constructeurs : END

    /** 
	 * <p>Override du toString() par défaut, permet l'affichage du nom de la
	 * vente dans la combo box de l'onglet 'Planifier'. Implémenté de cette
	 * façon car d'autres solutions, notamment l'override du renderer, ne
	 * marchent pas.</p>
     */
    public String toString() {
        return this.nom;
    }

	// Méthodes du design : START
	/**
	 * <p>Renvoie le premier objet de la liste des opbjet à vendre.</p>
	 *
	 * <p>Renvoie une IndexOutOfBoundsException si la liste est vide.</p>
	 */
	public int getFirst() {
        return objets.get(0).intValue();
    }

	/**
	 * <p>Renvoie l'objet en postion 0 (tête de liste), et le supprime de la 
	 * liste des objets de la vente.</p>
	 */
    public int removeFirst() {
        return objets.remove(0).intValue();
    }

	/**
	 * <p>Insère l'id d'un objet à la position spécifiée. Si l'index n'est pas 
	 * une valeur comprise entre 0 et la taille de la liste - 1, renvoie une
	 * IndexOutOfBoundsException.</p>
	 *
	 * @param	index	Position à laquelle insérer l'id de l'objet.
	 * @param	elt		Id de l'objet.
	 */
	public void addOId(int index, int elt) {
		this.objets.add(index, new Integer(elt));
        Logger.log("Vente", 2, LogType.DBG, "Insertion de l'OID " + elt
                + " à la position " + index + " réussi");
	}
	
	/**
	 * <p>Insère l'id d'un objet à la fin de la liste.</p>
	 *
	 * @param	elt		Id de l'objet.
	 */
	public void addOId(int elt) {
		this.objets.add(new Integer(elt));
        Logger.log("Vente", 2, LogType.DBG, "Insertion de l'OID " + elt
                + " en fin de liste réussi"); 
	}

	/**
	 * <p>enlève un objet de la liste par son id
	 * si l'oid n'est pas dans la liste, on fait rien.</p>
	 *
	 * @param	oid		Id de l'objet à supprimer.
	 */
	public void removeOId(int oid) {
		Integer id = null;
		for (Integer i : this.getOIds()) {
			if (i.intValue() == oid) {
				id = i;
			}
		}
		
		if (id != null) {
			this.objets.remove(id);
		}
		//ls : OLD implementation.
		/* 
		int p = this.getOIds().indexOf(oid);
		if (p != -1) {
			this.objets.remove(p);
		}*/
	}
	// Méthodes du design : END
	
    /** <p>Place l'Objet désigné par le paramètre <i>oid</i> à la position
     * donnée par <i>td</i> dans la liste d'Objets de la Vente.</p>
     * <p>En pratique, cette méthode renvoie une édition de la vente avec
     * la liste d'objets modifiée (où le déplacement a été fait).</p>
     *
     * @param   oid     ID de l'Objet à déplacer.
     * @param   td      Type de déplacement à effectuer.
     */
    void moveObjet(int oid, TypeDeplacement td) {
	//ls : Verison revue, qui me semble plus propre, quand à l'utilisation de la
	//     List<Integer>
		Integer t = null;
		int index = -1;
		for(Integer i : objets) {
			if (i.intValue() == oid) {
				index = objets.indexOf(i);
				t = i;
			}
		}
		
		if (t != null) {
			switch (td) {
				case UP:
					if(index != 0) {
						index--;
					}
					break;
				case DOWN:
					if(index != objets.size() - 1) {
						index++;
					}
					break;
				case TOP:
					index = 0;
					break;
				case BOTTOM:
					index = -1;
					break;
			}
			// on enlève l'objet de la liste à sa position actuelle
			objets.remove(t);
            // pour le remettre à la nouvelle.
            if(index != -1) {
                // cas index précis
                objets.add(index, t);
            } else {
                // cas "mettre à la fin"
                objets.add(t);
            }
		}
        
/*		List<Integer> objets = this.getOIds();
		if(objets.contains(new Integer(oid))) {
            int index = objets.indexOf(oid);

            switch (td) {
                case UP:
                    if(index != 0) {
                        index--;
                    }
                    break;
                case DOWN:
                    if(index != objets.size() - 1) {
                        index++;
                    }
                    break;
                case TOP:
                    index = 0;
                    break;
                case BOTTOM:
                    index = -1;
                    break;
            }

            // cast bizarre pour être sûr d'appliquer remove(Object o) et pas
            // remove(int index), puisque oid est un int !
            //
            // on enlève l'objet de la liste à sa position actuelle
            objets.remove((Object) oid);
            // pour le remettre à la nouvelle.
            if(index != -1) {
                // cas index précis
                objets.add(index, oid);
            } else {
                // cas "mettre à la fin"
                objets.add(oid);
            }
        }*/
    }

	// Setters & Getters : START
	// cfrey: accès au truc private
	public List<Integer> getOIds() {
		return this.objets;
	}

    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public String getDescription() {
        return this.description;
    }

    public long getDate() {
        return this.date;
    }

    public Mode getMode() {
        return this.mode;
    }

    public String getSuperviseur() {
        return this.superviseur;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setSuperviseur(String superviseur) {
        this.superviseur = superviseur;
    }
   	// Setters & Getters : END
}
