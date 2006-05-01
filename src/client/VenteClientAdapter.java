package trollhammer.client;

/** Classe adaptateur Client pour la Vente.
 * Permet d'effectuer des opérations sur la vente qui sont
 * spécifiques au Client.
 * Peut très bien ne servir à rien.
 */

class VenteClientAdapter {

    static void setPrices(Vente v) {
        Objet o = Client.objectmanager.getObject(v.getFirst());
        if(o != null) {
            int pb = o.getPrixDeBase();
            Client.client.setPrixCourant(pb);
            Client.client.setNouveauPrix((int) 1.1*pb);
        }
    }

}
