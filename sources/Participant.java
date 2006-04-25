package trollhammer;

class Participant {
    
    private String login;
    private String nom;
    private String prénom;
    private StatutLogin statut;

    String getLogin() {
        return this.login;
    }

    String getNom() {
        return this.nom;
    }

    String getPrénom() {
        return this.prénom;
    }

    StatutLogin getStatut() {
        return this.statut;
    }

    void setLogin(String login) {
        this.login = login;
    }

    void setNom(String nom) {
        this.nom = nom;
    }

    void setPrénom(String prénom) {
        this.prénom = prénom;
    }

    void setStatut(StatutLogin statut) {
        this.statut = statut;
    }

}
