package fr.alessdevita.valtherion.world;

import fr.alessdevita.valtherion.entity.Entity;

import org.joml.Vector3f;

/**
 * Gère la téléportation des entités entre dimensions ou positions.
 * Vérifie la sécurité de la destination avant de téléporter.
 */
public class Teleporter {

    private final World world;

    /**
     * Crée un téléporteur pour un monde.
     * @param world Monde source
     */
    public Teleporter(World world) {
        this.world = world;
    }

    /**
     * Téléporte une entité à une position donnée.
     * @param entity Entité à téléporter
     * @param targetPosition Position cible
     * @return true si la téléportation a réussi
     */
    public boolean teleport(Entity entity, Vector3f targetPosition) {
        if (entity == null || targetPosition == null) {
            return false;
        }

        // Vérifier que la destination est sûre
        if (!isSafePosition(targetPosition)) {
            // Chercher une position sûre à proximité
            Vector3f safePos = findSafePosition(targetPosition);
            if (safePos == null) {
                return false;
            }
            targetPosition = safePos;
        }

        // Appliquer la téléportation
        entity.setPosition(targetPosition.x, targetPosition.y, targetPosition.z);

        return true;
    }

    /**
     * Vérifie si une position est sûre pour la téléportation.
     * @param position Position à vérifier
     * @return true si la position est sûre
     */
    public boolean isSafePosition(Vector3f position) {
        int x = (int) position.x;
        int y = (int) position.y;
        int z = (int) position.z;

        // La position doit être dans les limites du monde
        if (y < 0 || y >= 256) {
            return false;
        }

        // Le bloc à la position doit être de l'air
        if (world.isBlockSolid(x, y, z)) {
            return false;
        }

        // Le bloc en dessous doit être solide
        if (!world.isBlockSolid(x, y - 1, z)) {
            return false;
        }

        // Le bloc au-dessus doit être de l'air (pour la tête)
        if (world.isBlockSolid(x, y + 1, z)) {
            return false;
        }

        return true;
    }

    /**
     * Cherche une position sûre autour d'une position cible.
     * @param targetPosition Position cible
     * @return Position sûre ou null si aucune trouvée
     */
    public Vector3f findSafePosition(Vector3f targetPosition) {
        // Essayer d'abord la position cible
        if (isSafePosition(targetPosition)) {
            return targetPosition;
        }

        // Chercher vers le haut
        for (int y = (int) targetPosition.y + 1; y < 256; y++) {
            Vector3f pos = new Vector3f(targetPosition.x, y, targetPosition.z);
            if (isSafePosition(pos)) {
                return pos;
            }
        }

        // Chercher dans un rayon autour de la position
        int radius = 10;
        for (int r = 1; r <= radius; r++) {
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++) {
                    Vector3f pos = new Vector3f(
                            targetPosition.x + x,
                            targetPosition.y,
                            targetPosition.z + z
                    );
                    if (isSafePosition(pos)) {
                        return pos;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Téléporte une entité à la surface du terrain.
     * @param entity Entité à téléporter
     * @param x Coordonnée X
     * @param z Coordonnée Z
     * @return true si la téléportation a réussi
     */
    public boolean teleportToSurface(Entity entity, int x, int z) {
        int height = world.getHeightAt(x, z);
        Vector3f position = new Vector3f(x, height + 1, z);
        return teleport(entity, position);
    }

    public World getWorld() {
        return world;
    }
}
