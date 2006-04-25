package trollhammer;

class Utilisateur extends Participant {

    private String mot_de_passe;

    String getMotDePasse() {
        return this.mot_de_passe;
    }

    void setMotDePasse(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

}

class Modérateur extends Utilisateur {

}
