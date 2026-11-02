package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.block.Block;

/**
 * Interface pour accéder aux blocs du monde en lecture seule.
 * Utilisée par les blocs qui doivent interroger leur environnement sans modifier le monde.
 */
public interface IBlockAccess {

    /**
     * Récupère un bloc à une position donnée.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @return Bloc à la position ou null s'il n'existe pas
     */
    Block getBlock(int x, int y, int z);

    /**
     * Vérifie si un bloc est solide à une position donnée.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @return true si le bloc est solide
     */
    boolean isBlockSolid(int x, int y, int z);
}
