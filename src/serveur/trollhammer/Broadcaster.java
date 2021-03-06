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

/**
 * <p>La classe se chargeant d'envoyer un message à tous les clients connectés
 * au serveur. Ses méthodes portent le nom des messages qui peuvent être envoyés 
 * en "broadcast".<p>
 *
 * @author jruffin
 */
class Broadcaster {

	// Méthodes du design : START
	/**
	 * <p>Envoie à tous les clients connectés l'état du participant p.</p>
	 *
	 * @param	p	participant
	 */
    public void etatParticipant(Participant p) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.etatParticipant(p);
        }
    }

	/**
	 * <p>Envoie à tous les clients connectés le message de l'utilisateur i.</p>
	 *
	 * @param	m	message
	 * @param	i	identifiant utilisateur
	 */
    public void chat(String m, String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.chat(m, i);
        }
    }

	/**
	 * <p>Envoie à tous les clients connectés l'enchère faite par l'utilisateur
	 * i.</p>
	 *
	 * @param	prix	prix de l'enchère
	 * @param	i		identifiant utilisateur
	 */
    public void enchere(int prix, String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.enchere(prix, i);
        }
    }

	/**
	 * <p>Envoie à tous les clients connectés l'événement e.</p>
	 *
	 * @param	e	événement
	 */
    public void evenement(Evenement e) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.evenement(e);
        }
    }

	/**
	 * <p>Envoie à tous les clients connectés la notification n.</p>
	 *
	 * @param	n	notification
	 */
    public void notification(Notification n) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.notification(n);
        }
    }

	/**
	 * <p>Envoie à tous les clients connectés la liste des ventes l.</p>
	 *
	 * @param	l	ensemble de ventes
	 */
    public void listeVentes(Set<Vente> l) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.listeVentes(l);
        }
    }

	/**
	 * <p>Envoie à tous les clients connectés les détails de la vente v.</p>
	 *
	 * <p>NB: la vente seule ne contient que la liste des identifiants de ses
	 * obj.</p>
	 *
	 * @param	v	une vente
	 * @param	vo	liste d'objets de cette vente
	 */
    public void detailsVente(Vente v, List<Objet> vo) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.detailsVente(v, vo);
        }
    }

	/**
	 * <p>Envoie à tous les clients connectés le fait que l'utilisateur i est un
	 * superviseur.<p>
	 *
	 * @param	i	identifiant utilisateur
	 */
    public void superviseur(String i) {
        Set<UtilisateurServeur> liste = Serveur.usermanager.getConnected();
        for(UtilisateurServeur u : liste) {
            u.superviseur(i);
        }
    }
	// Méthodes du design : END
}
