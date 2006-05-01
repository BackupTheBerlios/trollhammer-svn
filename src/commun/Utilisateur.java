package trollhammer.commun;

class Utilisateur extends Participant {

    private String mot_de_passe;

    Utilisateur(String login, String nom, String prenom, String mot_de_passe) {
        super(login, nom, prenom);
        this.mot_de_passe = mot_de_passe;
    }

    String getMotDePasse() {
        return this.mot_de_passe;
    }

    void setMotDePasse(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

}

class Moderateur extends Utilisateur {

    Moderateur(String login, String nom, String prenom, String mot_de_passe) {
        super(login, nom, prenom, mot_de_passe);
    }

}
