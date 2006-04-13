package trollhammer;

/** Sous-classe interface côté Serveur pour Vente.
 * Permet d'exécuter les opérations sur la vente
 * spécifiques au Serveur.
 */
class VenteServeur extends Vente {

    void modoLeaving(IDUtilisateur i) {

    }

    boolean isSuperviseur(IDUtilisateur s) {

    }

    boolean checkPAF(IDUtilisateur s) {

    }

    void sellObject(IDUtilisateur i, Integer prix) {

    }

    Objet removeHead() {

    }

    void insertObject(Objet o, Position p, Utilisateur u, Integer date) {

    }

    void removeObject(Objet o, Utilisateur c) {

    }

}
