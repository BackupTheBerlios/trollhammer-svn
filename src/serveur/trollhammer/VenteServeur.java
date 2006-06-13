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
import java.util.ArrayList;

/**
 * <p>Sous-classe Adaptateur côté Serveur pour Vente. Permet d'exécuter les 
 * opérations sur la vente spécifiques au Serveur.</p>
 */
 
class VenteServeur extends Vente {

	// Constructeurs : START
	VenteServeur(int id, String nom, String description, long date, Mode mode, String superviseur) {
		super(id, nom, description, date, mode, superviseur);
	}

	VenteServeur(int id, String nom, String description, long date, Mode mode, String superviseur, List<Integer> objets) {
		super(id, nom, description, date, mode, superviseur, objets);
	}
	// Constructeurs : END

	// Méthodes du design : START
	/**
	 * <p>Utilisé lors de la déconnection d'un modérateur. Le VenteManager nous
	 * indique qu'un modérateur se déconnecte de la <b>vente en cours</b>.</p>
	 *
	 * @param 	i	id modérateur qui se déconnecte de la vente en cours
	 */
    public void modoLeaving(String i) {
		// si le modérateur qui se déconnecte de la vente en cours est le
		// superviseur ...
		if (this.getSuperviseur().equals(i)) {
			this.setSuperviseur(null);
			this.setMode(Mode.Automatique);
	        Serveur.broadcaster.evenement(Evenement.VenteAutomatique);
		}
    }

	/**
	 * <p>Vérifie si un utilisateur est un superviseur de la vente ou pas.</p>
	 *
	 * @param	s	identifiant utilisateur
	 * @return	True si l'utilisateur est un superviseur de la vente, False 
	 * 			sinon.
	 */
    public boolean isSuperviseur(String s) {
		return this.getSuperviseur() != null ? this.getSuperviseur().equals(s) : false;
    }
	
	/**
	 * <p>Vente de l'objet si ce n'est pas le superviseur qui est le dernier
	 * enchérisseur.</p>
	 *
	 * @param	i		identifiant du dernier enchérisseur (donc acheteur)
	 * @param	prix	prix courant
	 */
    public void sellObject(String i, int prix) {
        ObjetServeur o = this.removeHead();
        o.sell(i, prix);
    }

	/**
	 * <p>Coupe la tête de la liste des objets d'une vente.</p>
	 *
	 * @return	l'objet qui était en tête de la liste
	 */
    public ObjetServeur removeHead() {
        return Serveur.objectmanager.getObjet(this.removeFirst());
    }

	/**
	 * <p>Ajoute un objet (id) dans la liste des objets (ids) d'une vente. Si la
	 * position d'insertion vaut -1, cela signifie insertion à la fin de la
	 * liste.</p>
	 *
	 * @param	oid		identifiant de l'objet à insérer
	 * @param	p		position dans la liste
	 * @param	u		identifiant du modérateur qui veut insérer l'objet ???
	 *					pas utilisé ici, à voir, je sais pas ce qu'on avait bu
	 *					quand on faisait les schémas ...
	 * @param	date	date de l'insertion (dateCourante dans 
	 *					insérerObjetVente) ??? pas utilisé ici, à voir
	 */
    public void insertObject(int oid, int p, String u, long date) {
		if (-1 < p && p < this.getOIds().size()) {
			this.addOId(p, oid);
		} else {
			// à la place d'avoir un index out of bound, on insère implicitement
			// à la fin de la liste et ça gère donc le comportement spécifié 
			// pour p == -1
			this.addOId(oid);
		}
		// l'insertion ne peut pas échouer, on modifier des propriétés de
		// l'objet, mais pour l'instant ça n'existe pas ...
		// ObjetServeur o = Serveur.objectmanager.getObjet(oid);
		// if (o != null) {
		// 	o.setDateInsertion = date;
		// 	o.setModérateurQuiAAjoutéL'ObjetALaVente = u;
		// }
    }
	
	//ls : Semble ne plus être utilisée... a virer?
	/**
	 * <p>Enlève de la liste des objets d'une vente un objet.Celui-ci est identifié par son 
	 * identifiant et non pas sa position.</p>
	 *
	 * @param	oid		identifiant de l'objet
	 * @param	u		identifiant de l'utilisateur ??? pas utilisé ici, à voir
	 */
    public void removeObject(int oid, String u) {
		this.removeOId(oid);
    }
	// Méthodes du design : END
	
	// Setters & Getters : START
    /**
     * <p>Retourne la liste des objets de la vente, qui sont au niveau de la vente
     * stockés sous forme d'une liste d'identifiants.</p>
     *
     * @return	La liste des objets de la vente.
     */
	public List<Objet> getObjets() {
		List<Objet> r = new ArrayList<Objet>();
		for (Integer oid : this.getOIds()) {
			r.add(Serveur.objectmanager.getObjet(oid));
		}
		return r;
	}

    /**
     * <p>Retourne une copie du contenu de l'objet, sous la forme d'un objet
     * de classe Vente.</p>
	 *
     * <p>Utilisé pour la sérialisation : un simple cast, même explicite,
     * de VenteServeur vers Vente pour l'expédier au Client ne marche
     * pas (provoque une ClassNotFoundException, car le Client
     * essaie de récupérer un objet VenteServeur 'planqué', casté en Vente)</p>
     */
    public Vente copieVente() {
        Vente v = new Vente(this.getId(), this.getNom(), this.getDescription(),
        	this.getDate(), this.getMode(), this.getSuperviseur(),
        	this.getOIds());
        return v;
    }
   	// Setters & Getters : END
}
