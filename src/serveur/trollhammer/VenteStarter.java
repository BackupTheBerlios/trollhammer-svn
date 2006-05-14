package trollhammer;

/**
 * <p>Thread de démarrage de Vente. Surveille les dates de démarrage des ventes
 * et démarre celle qui doit démarrer, c'est à dire la vente dont le temps de
 * début est dans le passé. Il ne doit y en avoir qu'une (d'après spécification),
 * et elle est démarrée.</p>
 *
 * @author Lionel Sambuc : C'est pas beau, mais c'est déjà ça...
 * @author squelette : Julien Ruffin
 */
class VenteStarter extends Thread {

    // check périodique pour voir si une vente démarre
	// ls : modif : on envoye pas démarrerVente sur ObjectManager, mais sur
	// VenteManager directement...
    public void run() {
		//VenteServeur vs = null;
		while (true) {
			//if ((vs = Serveur.ventemanager.getVenteEnCours()) != null) {
            // jr : correction, parce que demarrerVente() effectue ses
            // propres checks et le check ci-dessus est devenu invalide
            // depuis une restructuration - que je ne comprends pas moi-même -
            // qui fait que getVenteEnCours() renvoie le contenu d'un champ
            // au lieu d'aller itérer sur sa liste.
           
				Serveur.ventemanager.demarrerVente();
				//Logger.log("VenteStarter", 2, LogType.DBG, "Démarrage de vente!");
			//}
			try {
				Thread.sleep(1000); //dodo pendant une seconde.
			} catch (InterruptedException ie) {
				Logger.log("VenteStarter", 0, LogType.ERR, "[sys] Je peux pas dormir!!! car : " + ie);
			}
		}
    }
}
