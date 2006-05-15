package trollhammer;
import java.util.List;

/**
 * Classe adaptateur Client pour la Vente.
 * Permet d'effectuer des opérations sur la vente qui sont
 * spécifiques au Client.
 */

class VenteClient extends Vente {
	public VenteClient(int id, String nom, String description, long date, Mode mode, String superviseur, List<Integer> objets) {
		super(id, nom, description, date, mode, superviseur, objets);
	}
	//ls : N'est appelée que lors de l'initialisation de la vente d'un objet...
	void setPrices() {
        if(this.getOIds().size() > 0) {
            Objet o = Client.objectmanager.getObject(this.getFirst());
            if(o != null) {
                Client.client.setPrixCourant(o.getPrixDeBase());
                Client.client.setNouveauPrix(this.newPrice());
            }
        }
    }
	
	//ls : afin de n'avoir qu'a un seul endroit le calcul du nouveau prix...
	int newPrice() {
        if(this.getOIds().size() > 0) {
            Objet o = Client.objectmanager.getObject(this.getFirst());
            if(o != null) {
                return (int) (Client.client.getPrixCourant() + 0.1*o.getPrixDeBase());
            }
        }
		return 0;
	}
}
