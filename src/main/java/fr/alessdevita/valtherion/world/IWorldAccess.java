package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.entity.Entity;

import java.util.List;

/**
 * Interface pour accéder aux données du monde en lecture seule.
 * Permet aux entités et blocs d'interroger le monde sans le modifier.
 */
public interface IWorldAccess extends IBlockAccess {

    /**
     * Récupère une entité par son ID.
     * @param entityId ID de l'entité
     * @return Entité ou null si non trouvée
     */
    Entity getEntity(int entityId);

    /**
     * Récupère toutes les entités dans le monde.
     * @return Liste des entités
     */
    List<Entity> getEntities();

    /**
     * Retourne la hauteur du terrain à une position XZ.
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return Hauteur Y du terrain
     */
    int getHeightAt(int x, int z);

    /**
     * Retourne le temps actuel du monde.
     * @return Temps en secondes
     */
    float getWorldTime();

    /**
     * Retourne l'état actuel du monde (jour/nuit, météo).
     * @return État du monde
     */
    World.WorldState getState();
}
