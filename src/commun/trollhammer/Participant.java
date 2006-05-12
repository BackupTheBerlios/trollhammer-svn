package trollhammer;

class Participant implements java.io.Serializable {
    
    private String login;
    private String nom;
    private String prenom;
    private StatutLogin statut;

    Participant(String login, String nom, String prenom) {
        this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.statut = StatutLogin.Deconnecte;
    }

    String getLogin() {
        return this.login;
    }

    String getNom() {
        return this.nom;
    }

    String getPrenom() {
        return this.prenom;
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

    void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    void setStatut(StatutLogin statut) {
        this.statut = statut;
    }

}
