package trollhammer;

/**
 * Sous-classe Adaptateur côté Serveur pour Vente.
 * Permet d'exécuter les opérations sur la vente
 * spécifiques au Serveur.
 */

/* jr : faites-en ce que vous voulez ! sous-type ou pas.
 * Ce bout de squelette date d'avant que je réalise
 * que l'approche 'référence' était
 * - selon moi - 
 * meilleure pour l'Utilisateur.
 * Surtout par rapport au Modérateur.
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
