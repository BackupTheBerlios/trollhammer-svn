package trollhammer;

enum Action { Ench�rir, D�connecter };
enum ActionModo { CoupDeMassePAF };
enum Onglet { Achat, Vente, H�telDesVentes, Planification, Validation, GestionUtilisateurs };
enum StatutObjet { Vendu, Propos�, Accept�, Refus�, EnVente };
enum StatutLogin { Connect�_Utilisateur, Connect�_Mod�rateur, D�connect�, Invalide, Banni };
enum StatutEdition { R�ussi, ExisteD�j�, NonTrouv�, D�j�Effectu� };
enum Edition { Cr�er, Modifier, Supprimer }
enum Ev�nement { CoupDeMassePAF1, CoupDeMassePAF2, Adjug�, VenteAutomatique }
enum Notification { D�butVente, VenteEnCours, FinVente, LogOut, D�connexion, Kick� }
enum Erreur { D�connect�, Invalide, Banni, ExisteD�j�, NonTrouv�, D�j�Effectu� }
enum Mode { Automatique, Manuel }
