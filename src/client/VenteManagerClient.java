package trollhammer;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

class VenteManagerClient {

    private Set<Vente> ventes;

    boolean isInVenteEnCours(long date) {
        Vente v = Client.humain.getVente();
        long d = v.getDate();
        if(v != null && d < date) {
            return true;
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

    Vente getVenteEnCours() {
        long min = Long.MAX_VALUE;
        long prevmin = 0;
        Vente encours = null;

        /* d'abord, on prend la vente la plus proche dans le temps */
        for(Vente v : ventes) {
            prevmin = min;
            min = Math.min(min, v.getDate());
            if(min < prevmin) {
                encours = v;
            }
        }

        /* puis on vérifie si elle a démarré ! Si pas,
         * alors on retourne null (ie. aucune vente n'est en cours)
         */
        if(encours.getDate() < Client.client.getDate()) {
            return encours;
        } else {
            return null;
        }

    }

    void detailsVente(Vente v, List<Objet> os) {
        // pour mettre à jour une vente, on enlève
        // sa précédente instance de la liste
        for(Vente vte : ventes) {
            if(vte.getId() == v.getId()) {
                ventes.remove(vte);
            }
        }
        // puis on rajoute la nouvelle.
        ventes.add(v);

        // on pense aussi à mettre à jour les objets qui y sont vendus.
        // le type de la liste est donc de planification...
        // ou bien d'Hôtel des Ventes ? (jr)
        Client.objectmanager.updateListe(Onglet.HôtelDesVentes, new HashSet<Objet>(os));
    }

}
