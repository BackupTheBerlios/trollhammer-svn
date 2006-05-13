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
