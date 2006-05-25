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
