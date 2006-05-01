package trollhammer;
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
    private ArrayList<Integer> objets;

    int getFirst() {
        return objets.get(0);
    }

    int removeFirst() {
        return objets.remove(0);
    }

    /* getters-setters and BLAH BLAH BLAH */

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