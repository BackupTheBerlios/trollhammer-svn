package trollhammer;

/**
 * Sous-classe Adaptateur côté Serveur pour Vente.
 * Permet d'exécuter les opérations sur la vente
 * spécifiques au Serveur.
 */
class VenteServeur extends Vente {

    void modoLeaving(String i) {

    }

    boolean isSuperviseur(String s) {
        return false;
    }

    boolean checkPAF(String s) {
        return false;
    }

    void sellObject(String i, int prix) {

    }

    Objet removeHead() {
        return null;
    }

    void insertObject(Objet o, int p, Utilisateur u, long date) {

    }

    void removeObject(Objet o, Utilisateur c) {

    }

}
