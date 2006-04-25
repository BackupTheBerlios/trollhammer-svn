package trollhammer;
import java.util.Set;

class ObjectManagerClient {

    private Set<Objet> objets;
    private Set<Objet> vendu;
    private Set<Objet> acheté;
    private Set<Objet> propositions;

    Objet getObject(int i) {
        for(Objet o : objets) {
            if(o.getId() == i) {
                return o;
            }
        }
        return null;
    }

    void listeObjets(Onglet t, Set<Objet> ol) {
        this.updateListe(t, ol);
        Onglet mode = Client.client.getMode();
        if(mode == t) {
            Client.hi.affichageListeObjets(ol);
        }
    }

    private void update(Set<Objet> liste, Objet o) {

        // pour mettre à jour un objet, on enlève
        // sa précédente instance de la liste
        for(Objet obj : liste) {
            if(obj.getId() == o.getId()) {
                liste.remove(obj);
            }
        }
        // puis on rajoute la nouvelle.
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
                    this.update(acheté, o); break;
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
            if(obj.getId() == i) {
                return obj;
            }
        }
        return null;
    }
}
