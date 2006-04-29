package trollhammer;

enum Action { Encherir, Deconnecter };
enum ActionModo { CoupDeMassePAF };
enum Onglet { Achat, Vente, HôtelDesVentes, Planification, Validation, GestionUtilisateurs };
enum StatutObjet { Vendu, Propose, Accepte, Refuse, EnVente };
enum StatutLogin { Connecte_Utilisateur, Connecte_Moderateur, Deconnecte, Invalide, Banni };
enum StatutEdition { Reussi, ExisteDejà, NonTrouve, DejàEffectue };
enum Edition { Creer, Modifier, Supprimer }
enum Evenement { CoupDeMassePAF1, CoupDeMassePAF2, Adjuge, VenteAutomatique }
enum Notification { DebutVente, VenteEnCours, FinVente, LogOut, Deconnexion, Kicke }
enum Erreur { Deconnecte, Invalide, Banni, ExisteDejà, NonTrouve, DejàEffectue }
enum Mode { Automatique, Manuel }
