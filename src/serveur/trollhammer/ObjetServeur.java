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

/**
 * <p>Adaptateur Serveur pour la classe Objet. Encapsule un Objet, en ajoutant
 * les méthodes Objet spécifiques au Serveur. Permet d'utiliser les mêmes
 * getters/setters qu'une instance d'Objet.<p>
 *
 * @author Lionel Sambuc
 * @author squelette : Julien Ruffin
 */
class ObjetServeur extends Objet {
	// Constructeurs : START
    public ObjetServeur(Objet o) {
		super(o.getId(), o.getNom(), o.getDescription(), o.getModerateur(), o.getPrixDeBase(), o.getPrixDeVente(), o.getStatut(), o.getAcheteur(), o.getVendeur(), o.getImage());
    }
	// Constructeurs : END
	
	// Méthodes du design : START
	//ls : modif on garde tout de même trace de qui a refusé l'objet...
    boolean invalider(String i) {
		if (this.getStatut() == StatutObjet.Propose) {
			this.setStatut(StatutObjet.Refuse);
			this.setModerateur(i);
			return true;
		}
		else {
			return false;
		}
    }

    boolean valider(String i) {
		if (this.getStatut() == StatutObjet.Propose) {
			this.setStatut(StatutObjet.Accepte);
			this.setModerateur(i);
			return true;
		}
		else {
			return false;
		}
    }

    /**
     * "Vend" l'objet, c-à-d affecte son prix de vente, l'acheteur, et change 
     * son statut.
     *
     * @param	i		acheteur
     * @param	prix	prix
     */
    void sell(String i, int prix) {
		this.setPrixDeVente(prix);
		this.setAcheteur(i);
		this.setStatut(StatutObjet.Vendu);
	}
	// Méthodes du design : END

	// Setters & Getters : START
   	// Setters & Getters : END
}
