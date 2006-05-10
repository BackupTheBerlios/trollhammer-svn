package trollhammer;

class Objet implements java.io.Serializable {
    
    private int id;
    private String nom;
    private String description;
    private String moderateur;
	private int prix_de_base;
    private int prix_de_vente;
    private StatutObjet statut;
    private String acheteur;
    private String vendeur;

    int getId() {
        return this.id;
    }

    String getNom() {
        return this.nom;
    }

    String getDescription() {
        return this.description;
    }

    String getModerateur() {
        return this.moderateur;
    }

    int getPrixDeBase() {
        return this.prix_de_base;
    }

    int getPrixDeVente() {
        return this.prix_de_vente;
    }

    StatutObjet getStatut() {
        return this.statut;
    }

    String getAcheteur() {
        return this.acheteur;
    }

    String getVendeur() {
        return this.vendeur;
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

    void setModerateur(String moderateur) {
        this.moderateur = moderateur;
    }

    void setPrixDeBase() {
        this.prix_de_base = prix_de_base;
    }

    void setPrixDeVente() {
        this.prix_de_vente = prix_de_vente;
    }

    void setStatut(StatutObjet statut) {
        this.statut = statut;
    }

    void setAcheteur() {
        this.acheteur = acheteur;
    }

    void setVendeur() {
        this.vendeur = vendeur;
    }

}
