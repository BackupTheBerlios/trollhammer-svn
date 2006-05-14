package trollhammer;

/** Adaptateur Serveur pour la classe Objet.
 * Encapsule un Objet, en ajoutant les méthodes Objet spécifiques au Serveur.
 * Permet d'utiliser les mêmes getters/setters qu'une instance d'Objet.
 *
 * @author Lionel Sambuc
 * @author squelette : Julien Ruffin
 */
class ObjetServeur {

    Objet objet;

    public ObjetServeur(Objet o) {
        this.objet = o;
    }

	//ls : modif on garde tout de même trace de qui a refusé l'objet...
    boolean invalider(String i) {
		if (objet.getStatut() == StatutObjet.Propose) {
			objet.setStatut(StatutObjet.Refuse);
			objet.setModerateur(i);
			return true;
		}
		else {
			return false;
		}
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

// cfrey: ça se passe au niveau de ObjectManagerServeur
//    void sell(String i, Integer prix) {
//
//    }
// ls : redéplacé ici, le fait que l'on le fasse sur l'objet lui-même garanti
//      qu'il existe...
    /**
     * "Vend" l'objet, c-à-d affecte son prix de vente, l'acheteur, et change 
     * son statut.
     *
     * @param	i		acheteur
     * @param	prix	prix
     */
    void sell(String i, int prix) {
		this.setPrixDeVente(prix);
		this.setAcheteur(i);
		this.setStatut(StatutObjet.Vendu);
	}
	
	Objet getObjet() {
		return objet;
	}

    int getId() {
        return this.objet.getId();
    }

    String getNom() {
        return this.objet.getNom();
    }

    String getDescription() {
        return this.objet.getDescription();
    }

    String getModerateur() {
        return this.objet.getModerateur();
    }

    int getPrixDeBase() {
        return this.objet.getPrixDeBase();
    }

    int getPrixDeVente() {
        return this.objet.getPrixDeVente();
    }

    StatutObjet getStatut() {
        return this.objet.getStatut();
    }

    String getAcheteur() {
        return this.objet.getAcheteur();
    }

    String getVendeur() {
        return this.objet.getVendeur();
    }

    void setId(int id) {
        this.objet.setId(id);
    }

    void setNom(String nom) {
        this.objet.setNom(nom);
    }

    void setDescription(String description) {
        this.objet.setDescription(description);
    }

    void setModerateur(String moderateur) {
        this.objet.setModerateur(moderateur);
    }

    void setPrixDeBase(int prix_de_base) {
        this.objet.setPrixDeBase(prix_de_base);
    }

    void setPrixDeVente(int prix_de_vente) {
        this.objet.setPrixDeVente(prix_de_vente);
    }

    void setStatut(StatutObjet statut) {
        this.objet.setStatut(statut);
    }

    void setAcheteur(String acheteur) {
        this.objet.setAcheteur(acheteur);
    }

    void setVendeur(String vendeur) {
        this.objet.setVendeur(vendeur);
    }
}
