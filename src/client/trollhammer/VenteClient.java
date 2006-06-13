/*
 * TrollHammer - Auctions at home over the Internet !
 * Copyright (C) 2006 ZOG Team, contact information on trollhammer.berlios.de
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. *  * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details. *  * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
                // le max(...) est ici pour assurer un incrément minimal de 1 à l'enchère
                return (int) Math.max(Client.client.getPrixCourant() + 1,
                        Client.client.getPrixCourant() + 0.1*o.getPrixDeBase());
            }
        }
		return 0;
	}
}
