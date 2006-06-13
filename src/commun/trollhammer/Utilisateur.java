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

/**
 * <p>Objet qui stocke les données d'un utilisateur.</p>
 *
 * @author Julien Ruffin
 */
class Utilisateur extends Participant {
//ls : pour cceux qui pourrai être choqué par les protected, je me permet de 
//     faire référence à http://java.sun.com/docs/books/tutorial/java/javaOO/accesscontrol.html    
    protected String mot_de_passe;

	// Constructeurs : START
	/**
	 * <p>Crée un Utilisateur, avec les valeurs assignées. Le statut est affecté
	 * à <code>Deconnecte</code></p>
	 *
	 * @param	login			Nom d'utilisateur, doit être unique, car c'est 
	 *							l'identifiant système (un String).
	 * @param	nom				Nom de l'utilisateur (un String)
	 * @param	prenom			Prenom de l'utilisateur (un String)
	 * @param	mot_de_passe	Mot de passe de l'utilisateur (un String)
	 */
    Utilisateur(String login, String nom, String prenom, String mot_de_passe) {
        super(login, nom, prenom);
        this.mot_de_passe = mot_de_passe;
    }
	/**
	 * <p>Crée un Utilisateur, avec les valeurs assignées.</p>
	 *
	 * @param	login			Nom d'utilisateur, doit être unique, car c'est 
	 *							l'identifiant système (un String).
	 * @param	nom				Nom de l'utilisateur (un String)
	 * @param	prenom			Prenom de l'utilisateur (un String)
	 * @param	statut			Statut du participant (Enumération StatutLogin)
	 * @param	mot_de_passe	Mot de passe de l'utilisateur (un String)
	 */
    Utilisateur(String login, String nom, String prenom, StatutLogin statut, String mot_de_passe) {
        super(login, nom, prenom, statut);
        this.mot_de_passe = mot_de_passe;
    }
	// Constructeurs : END
	
	// Méthodes du design : START
	// Méthodes du design : END
	
	// Setters & Getters : START
    public String getMotDePasse() {
        return this.mot_de_passe;
    }

    public void setMotDePasse(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }
   	// Setters & Getters : END
}

/**
 * <p>Objet qui stocke les données d'un modérateur.</p>
 *
 * @author Julien Ruffin
 */
class Moderateur extends Utilisateur {

	// Constructeurs : START
	/**
	 * <p>Crée un Modérateur, avec les valeurs assignées. Le statut est affecté
	 * à <code>Deconnecte</code></p>
	 *
	 * @param	login			Nom d'utilisateur, doit être unique, car c'est 
	 *							l'identifiant système (un String).
	 * @param	nom				Nom du modérateur (un String)
	 * @param	prenom			Prenom du modérateur (un String)
	 * @param	mot_de_passe	Mot de passe du modérateur (un String)
	 */
    Moderateur(String login, String nom, String prenom, String mot_de_passe) {
        super(login, nom, prenom, mot_de_passe);
    }
	/**
	 * <p>Crée un Modérateur, avec les valeurs assignées.</p>
	 *
	 * @param	login			Nom d'utilisateur, doit être unique, car c'est 
	 *							l'identifiant système (un String).
	 * @param	nom				Nom du modérateur (un String)
	 * @param	prenom			Prenom du modérateur (un String)
	 * @param	statut			Statut du participant (Enumération StatutLogin)
	 * @param	mot_de_passe	Mot de passe du modérateur (un String)
	 */
    Moderateur(String login, String nom, String prenom, StatutLogin statut, String mot_de_passe) {
        super(login, nom, prenom, statut, mot_de_passe);
    }
	// Constructeurs : END

	// Méthodes du design : START
	// Méthodes du design : END
	
	// Setters & Getters : START
   	// Setters & Getters : END
}
