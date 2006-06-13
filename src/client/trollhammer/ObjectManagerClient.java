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

import java.util.Set;
import java.util.HashSet;
import java.util.Vector;

class ObjectManagerClient {

    private Set<Objet> objets;
    private Set<Objet> vendu;
    private Set<Objet> achete;
    private Set<Objet> propositions;

    ObjectManagerClient() {
        objets = new HashSet<Objet>();
        vendu = new HashSet<Objet>();
        achete = new HashSet<Objet>();
        propositions = new HashSet<Objet>();
    }

    Objet getObject(int i) {
        for(Objet o : objets) {
            if (o.getId() == i) {
                return o;
            }
        }
        return null;
    }

    void listeObjets(Onglet t, Set<Objet> ol) {
        this.updateListe(t, ol);
        if (Client.client.getMode() == t) {
            Client.hi.affichageListeObjets(ol);
        }
    }

    private void update(Set<Objet> liste, Objet o) {

        // pour mettre à jour un objet, on enlève
        // sa précédente instance de la liste

        Vector <Objet> a_enlever = new Vector<Objet>();

        // on planifie d'abord le retrait
        for(Objet obj : liste) {
            if (obj.getId() == o.getId()) {
                a_enlever.add(obj);
            }
        }
        
        // ensuite on effectue le retrait

        for(Objet doublon : a_enlever) {
            liste.remove(doublon);
        }

		//ls : et pourquoi ne pas faire comme ca?
        //jr : pour les mêmes raisons que dans ParticipantManagerClient.
        /*for(Objet obj : liste) {
            if (obj.getId() == o.getId()) {
                liste.remove(obj);
            }
        }*/

        // puis on rajoute la nouvelle instance.
        liste.add(o);
    }

    // sauf mention contraire, c'est la liste des objets qu'on modifie.
    void update(Objet o) {
        this.update(objets, o);
    }

    void updateListe(Onglet type, Set<Objet> ol) {
        for(Objet o : ol) {
            this.update(o);

            switch(type) {
                case Achat:
                    this.update(achete, o); break;
                case Vente:
                    this.update(vendu, o); break;
                case Validation:
                    this.update(propositions, o); break;
            }
        }
    }

    /** Retourne l'objet d'identifiant i, null sinon. */
    Objet getObjet(int i) {
        // ceci est une boucle foreach !!!
        for(Objet obj : objets) {
            if (obj.getId() == i) {
                return obj;
            }
        }
        return null;
    }
}
