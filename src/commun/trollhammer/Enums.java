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
 * Contient toutes les énumerations utilisée par Trollhammer.
 */

enum Action { Encherir, Deconnecter };
enum ActionModo { CoupDeMassePAF };
enum Onglet { Achat, Vente, HotelDesVentes, Planification, Validation, GestionUtilisateurs };
enum StatutObjet { Vendu, Propose, Accepte, Refuse, EnVente };
enum StatutLogin { Connecte_Utilisateur, Connecte_Moderateur, Deconnecte, Invalide, Banni, Deja_Connecte};
enum StatutEdition { Reussi, ExisteDeja, NonTrouve, DejaEffectue };
enum Edition { Creer, Modifier, Supprimer }
enum Evenement { CoupDeMassePAF1, CoupDeMassePAF2, Adjuge, VenteAutomatique }
enum Notification { DebutVente, VenteEnCours, FinVente, LogOut, Deconnexion, Kicke }
enum Erreur { Deconnecte, Invalide, Banni, ExisteDeja, NonTrouve, DejaEffectue }
enum Mode { Automatique, Manuel }

/** <p>Enum non présent dans le Design ; il est utilisé pour déterminer quel déplacement un Objet doit effectuer dans la liste d'une Vente.</p>
 * <p>@author Julien Ruffin</p>
 */
enum TypeDeplacement {UP, DOWN, TOP, BOTTOM};
