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

/**
 * Classe Manager pour le Serveur.
 * Sert à regrouper et gérer les Participants du Serveur.
 * La liste des Participants est en réalité tirée de la liste d'Utilisateurs
 * de UserManagerServeur.
 *
 * @author Lionel Sambuc
 * @author squelette : Julien Ruffin
 */
class ParticipantManagerServeur {

	/**
	 * <p>Retourne la liste des participants (Utilisateur actuellement connecté).
	 * Réponds au message envoyer par le client pour remplir la liste affichée
	 * dans l'onglet HdV.
	 *
	 * @param sender	IDUtilisateur de l'émetteur de la requête.
	 */
    void obtenirListeParticipants(String sender) {
        UtilisateurServeur s = Serveur.usermanager.getUtilisateur(sender);
		Set<Participant> pl = new HashSet<Participant>();
        Set<UtilisateurServeur> ul = Serveur.usermanager.getUtilisateurs();
        for(UtilisateurServeur u : ul) {
            if(u.getStatut() == StatutLogin.Connecte_Utilisateur
            || u.getStatut() == StatutLogin.Connecte_Moderateur) {
                pl.add(u);
            }
        }
        s.listeParticipants(pl);
	}
	
	/**
	 * <p>Retourne la liste des utilisateurs (sous forme de participant) inscrits.
	 * Réponds au message envoyer par le client pour remplir la liste affichée
	 * dans l'onglet GestionUtilisateurs, on renvoye tous les utilisateurs défini
	 * dans le système, mais sous la forme de participants, afin de ne pas faire
	 * transiter sur le réseau des information sensible inutilement, tel que le MdP.</p>
	 *
	 * @param sender	IDUtilisateur de l'émetteur de la requête.
	 */
    void obtenirListeUtilisateurs(String sender) {
        UtilisateurServeur s = Serveur.usermanager.getUtilisateur(sender);
        Set<Participant> pl = new HashSet<Participant>();
        Set<UtilisateurServeur> ul = Serveur.usermanager.getUtilisateurs();
        for(UtilisateurServeur u : ul) {
			pl.add(u);
        }
        s.listeParticipants(pl);
	}
}
