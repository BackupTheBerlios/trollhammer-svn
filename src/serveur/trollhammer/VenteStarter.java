package trollhammer;

/**
 * <p>Thread de démarrage de Vente. Surveille les dates de démarrage des ventes
 * et démarre celle qui doit démarrer, c'est à dire la vente dont le temps de
 * début est dans le passé, et qui se trouve en tête de la liste des ventes.</p>
 *
 * @author Lionel Sambuc
 * @author squelette : Julien Ruffin
 */
class VenteStarter extends Thread {

    public void run() {
		while (true) {
			Logger.log("VenteStarter", 3, LogType.DBG, "Démarrage de vente!");
			Serveur.ventemanager.demarrerVente();
			Serveur.ventemanager.donnerCoupdeMASSE();
			try {
				Thread.sleep(1000); //dodo pendant une seconde.
			} catch (InterruptedException ie) {
				Logger.log("VenteStarter", 0, LogType.ERR, "[sys] Je peux pas dormir!!! car : " + ie);
			}
		}
    }
}
