package trollhammer;

/** Classe wrapper Client pour la Vente.
 * Permet d'effectuer des opérations sur la vente qui sont
 * spécifiques au Client.
 * Peut très bien ne servir à rien.
 */

class VenteClient extends Vente {

    /* les trois fonctions suivantes peuvent être mises dans Vente */
    void getFirst() {

    }

    void getHead() {

    }

    void removeFirst() {

    }

    /* jr : celle-ci ne peut pas l'être, fonctions spécifiques à Client.
     * je propose un déplacement vers VenteManagerClient */
    void setPrices() {

    }

}
