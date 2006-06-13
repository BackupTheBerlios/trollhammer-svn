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
 * <p>Objet qui stocke les données de base d'un utilisateur. Ne contient que les
 * données nécessaires afin de l'afficher dans une vente. Permet aussi de ne 
 * transmettre les données sensibles d'un utilisateur que lorsque c'est vraiment
 * nécessaire.</p>
 *
 * @author Lionel Sambuc
 * @author Julien Ruffin
 */
class Participant implements java.io.Serializable {
//ls : pour cceux qui pourrai être choqué par les protected, je me permet de 
//     faire référence à http://java.sun.com/docs/books/tutorial/java/javaOO/accesscontrol.html    
    protected String login;
    protected String nom;
    protected String prenom;
    protected StatutLogin statut;

	// Constructeurs : START
	/**
	 * <p>Crée un participant, avec les valeurs assignées. Le statut est affecté
	 * à <code>Deconnecte</code></p>
	 *
	 * @param	login	Nom d'utilisateur, doit être unique, car c'est 
	 *					l'identifiant système (un String).
	 * @param	nom		Nom du particpant (un String)
	 * @param	prenom	Prenom du participant (un String)
	 */
    Participant(String login, String nom, String prenom) {
		this(login, nom, prenom, StatutLogin.Deconnecte);
    }
	/**
	 * <p>Crée un participant, avec les valeurs assignées.</p>
	 *
	 * @param	login	Nom d'utilisateur, doit être unique, car c'est 
	 *					l'identifiant système (un String).
	 * @param	nom		Nom du particpant (un String)
	 * @param	prenom	Prenom du participant (un String)
	 * @param	statut	Statut du participant (Enumération StatutLogin)
	 */
    Participant(String login, String nom, String prenom, StatutLogin statut) {
		this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.statut = statut;
    }
	// Constructeurs : END
	
	// Méthodes du design : START
	// Méthodes du design : END
	
	// Setters & Getters : START
    public String getLogin() {
        return this.login;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public StatutLogin getStatut() {
        return this.statut;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setStatut(StatutLogin statut) {
        this.statut = statut;
    }
	// Setters & Getters : END
}
