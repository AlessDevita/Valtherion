package fr.alessdevita.valtherion.entity;

/**
 * Énumération des types d'entités dans Valtherion.
 */
public enum EntityType {
    // Entités vivantes
    PLAYER,          // Joueur
    MOB,            // Mob (monstre)
    NPC,            // PNJ (Personnage Non-Joueur)
    BOSS,           // Boss

    // Entités non vivantes
    ITEM,           // Objet (tombé au sol)
    PROJECTILE,     // Projectile (flèche, boule de feu, etc.)

    // Autres types (optionnels)
    VEHICLE,        // Véhicule (char, bateau, etc.)
    BLOCK_ENTITY,    // Entité de bloc (coffre, four, etc.)
    AMBIENT,        // Entité ambiante (particules, effets visuels, etc.)
    UNKNOWN         // Type inconnu
}