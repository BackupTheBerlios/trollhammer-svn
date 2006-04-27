package trollhammer;

enum Action { Enchérir, Déconnecter };
enum ActionModo { CoupDeMassePAF };
enum Onglet { Achat, Vente, HôtelDesVentes, Planification, Validation, GestionUtilisateurs };
enum StatutObjet { Vendu, Proposé, Accepté, Refusé, EnVente };
enum StatutLogin { Connecté_Utilisateur, Connecté_Modérateur, Déconnecté, Invalide, Banni };
enum StatutEdition { Réussi, ExisteDéjà, NonTrouvé, DéjàEffectué };
enum Edition { Créer, Modifier, Supprimer }
enum Evénement { CoupDeMassePAF1, CoupDeMassePAF2, Adjugé, VenteAutomatique }
enum Notification { DébutVente, VenteEnCours, FinVente, LogOut, Déconnexion, Kické }
enum Erreur { Déconnecté, Invalide, Banni, ExisteDéjà, NonTrouvé, DéjàEffectué }
enum Mode { Automatique, Manuel }
