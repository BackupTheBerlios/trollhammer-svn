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
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

class VenteManagerClient {

    private Set<Vente> ventes;

    VenteManagerClient() {
        ventes = new HashSet<Vente>();
    }

    boolean isInVenteEnCours(long date) {
        Vente v = Client.humain.getVente();
        if(v != null) {
            long d = v.getDate();
            if(d < date) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    void listeVentes(Set<Vente> vl) {
        this.ventes = vl;
        Onglet m = Client.client.getMode();
        if(m == Onglet.Planification) {
            Client.hi.affichageListeVentes(vl);
        }
    }

    /** Cherche une vente par son identifiant et la retourne,
     * ou null si non trouvée.
     */
    Vente getVente(int i) {
        for(Vente v : ventes) {
            if(v.getId() == i) {
                return v;
            }
        }
        return null;
    }

    VenteClient getVenteEnCours() {
        long min = Long.MAX_VALUE;
        long prevmin = Long.MAX_VALUE;
        Vente encours = null;

        /* d'abord, on prend la vente la plus proche dans le temps */
        Logger.log("VenteManagerClient", 2, LogType.DBG, "Tente de trouver Vente en cours dans liste de taille "+ventes.size());
        for(Vente v : ventes) {
            min = Math.min(min, v.getDate());
            if(min < prevmin) {
                encours = v;
            }
            prevmin = min;
        }
        Logger.log("VenteManagerClient", 2, LogType.DBG, "Vente potentiellement démarrée : "+encours);

        if(encours != null) {
            /* puis on vérifie si elle a démarré ! Si pas,
             * alors on retourne null (ie. aucune vente n'est en cours)
             */
            if(encours.getDate() < Client.client.getDate()) {
                Logger.log("VenteManagerClient", 2, LogType.DBG, "Trouvé vente en cours: "+encours);
                return new VenteClient(encours.getId(), encours.getNom(), encours.getDescription(), encours.getDate(), encours.getMode(), encours.getSuperviseur(), encours.getOIds());
            } else {
                Logger.log("VenteManagerClient", 2, LogType.DBG, "Vente non démarrée : "+encours);
            }
        }

        return null; // rien trouvé
    }

    void detailsVente(Vente v, List<Objet> os) {
        // pour mettre à jour une vente, on enlève
        // sa précédente instance de la liste
        Set<Vente> a_enlever = new HashSet<Vente>();

        for(Vente vte : ventes) {
            if(vte.getId() == v.getId()) {
                a_enlever.add(vte);
            }
        }

        for(Vente doublon : a_enlever) {
            ventes.remove(doublon);
        }

        // puis on rajoute la nouvelle.
        ventes.add(v);

        // on pense aussi à mettre à jour les objets qui y sont vendus.
        // le type de la liste est donc de planification...
        // ou bien d'Hôtel des Ventes ? (jr)
        Client.objectmanager.updateListe(Onglet.HotelDesVentes, new HashSet<Objet>(os));
        // jr : ajout p.r. Design : affichage de la vente
        // mis à jour ! *ding dong* !
        Client.hi.affichageVente(v);
    }

    /** <p>Place l'Objet désigné par le paramètre <i>oid</i> à la position
     * donnée par <i>td</i> dans la liste d'Objets de la Vente
     * désignée par le paramètre <i>vid</i>.</p>
     * <p>En pratique, cette méthode renvoie une édition de la vente avec
     * la liste d'objets modifiée (où le déplacement a été fait).</p>
     */
    void moveObjet(int vid, int oid, TypeDeplacement td) {
        Vente v = getVente(vid);
        Objet o = Client.objectmanager.getObject(oid);

        if(v != null && o != null) {
            v.moveObjet(oid, td);

            // jr : et on envoie l'update. En faisant un bypass de la FSM.
            // C'est affreux, mais je n'arrive pas trouver le bug !
            Client.hi.editerVente(Edition.Modifier, v);
            //Client.session.vente(Edition.Modifier, v);
            //Client.hi.affichageVente(v);
        }
    }

}
