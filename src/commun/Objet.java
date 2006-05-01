package trollhammer.commun;

class Objet implements java.io.Serializable {
    
    private int id;
    private int nom;
    private int description;
    private int prix_de_base;
    private int prix_de_vente;
    private StatutObjet statut;
    private String acheteur;
    private String vendeur;

    int getId() {
        return this.id;
    }

    int getNom() {
        return this.nom;
    }

    int getDescription() {
        return this.description;
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

    void setId() {
        this.id = id;
    }

    void setNom() {
        this.nom = nom;
    }

    void setDescription() {
        this.description = description;
    }

    void setPrixDeBase() {
        this.prix_de_base = prix_de_base;
    }

    void setPrixDeVente() {
        this.prix_de_vente = prix_de_vente;
    }

    void setStatut() {
        this.statut = statut;
    }

    void setAcheteur() {
        this.acheteur = acheteur;
    }

    void setVendeur() {
        this.vendeur = vendeur;
    }

}
