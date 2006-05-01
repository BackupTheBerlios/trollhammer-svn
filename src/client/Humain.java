package trollhammer.client;
import trollhammer.commun;

class Humain {

    private Vente vente;

    Vente getVente() {
        return vente;
    }

    void setVente(Vente vente) {
        this.vente = vente;
        if(vente != null) {
            //vente.setPrices();
        }
    }

}
