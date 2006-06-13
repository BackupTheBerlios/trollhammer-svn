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

import javax.swing.*;

/**
 * <p>Classe qui modélise un objet mis en vente.</p>
 */
class Objet implements java.io.Serializable {
	private int id;
	private String nom;
	private String description;
	private String moderateur;
	private int prix_de_base;
	private int prix_de_vente;
	private StatutObjet statut;
	private String acheteur;
	private String vendeur;
	private ImageIcon image;

	// Constructeurs : START
	//ls : présent afin que le code qui utilise le constructeur par défaut
	//     continue de fonctionner...
	Objet() {
		this.id = 0;
		this.nom = null;
		this.description = null;
		this.moderateur = null;
		this.prix_de_base = 0;
		this.prix_de_vente = 0;
		this.statut = null;
		this.acheteur = null;
		this.vendeur = null;
		this.image = null;
	}
	
	Objet(int id, String nom, String description, String moderateur, int prix_de_base, int prix_de_vente, StatutObjet statut, String acheteur, String vendeur, ImageIcon image) {
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.moderateur = moderateur;
		this.prix_de_base = prix_de_base;
		this.prix_de_vente = prix_de_vente;
		this.statut = statut;
		this.acheteur = acheteur;
		this.vendeur = vendeur;
		this.image = image;
	}
	// Constructeurs : END

	// Méthodes du design : START
	// Méthodes du design : END
	
	// Setters & Getters : START
    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public String getDescription() {
        return this.description;
    }

    public String getModerateur() {
        return this.moderateur;
    }

    public int getPrixDeBase() {
        return this.prix_de_base;
    }

    public int getPrixDeVente() {
        return this.prix_de_vente;
    }

    public StatutObjet getStatut() {
        return this.statut;
    }

    public String getAcheteur() {
        return this.acheteur;
    }

    public String getVendeur() {
        return this.vendeur;
    }
	public ImageIcon getImage() {
		return this.image;
	}

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModerateur(String moderateur) {
        this.moderateur = moderateur;
    }

    public void setPrixDeBase(int prix_de_base) {
        this.prix_de_base = prix_de_base;
    }

    public void setPrixDeVente(int prix_de_vente) {
        this.prix_de_vente = prix_de_vente;
    }

    public void setStatut(StatutObjet statut) {
        this.statut = statut;
    }

    public void setAcheteur(String acheteur) {
        this.acheteur = acheteur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }
	
	public void setImage(ImageIcon image) {
		this.image = image;
	}
   	// Setters & Getters : END
}
