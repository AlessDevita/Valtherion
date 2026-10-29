package fr.alessdevita.valtherion.client.entity;

import fr.alessdevita.valtherion.world.World;
import org.joml.Vector3f;

/**
 * Classe pour le joueur en mode solo (Single Player) dans Valtherion.
 * Étend AbstractClientPlayer pour ajouter des fonctionnalités spécifiques au mode solo.
 */
public class EntityPlayerSP extends AbstractClientPlayer {

    // Indique si le joueur est en mode débogage (noclip, etc.)
    private boolean isDebugMode;

    // Indique si le joueur peut voler (mode créatif)
    private boolean canFly;

    // Vitesse de vol
    private float flySpeed;

    /**
     * Constructeur du joueur en mode solo.
     * @param id ID de l'entité.
     * @param name Nom de l'entité.
     * @param world Monde dans lequel le joueur évolue.
     */
    public EntityPlayerSP(String id, String name, World world) {
        super(id, name, world);
        this.isDebugMode = false;
        this.canFly = false;
        this.flySpeed = 10.0f;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Si le joueur est en mode débogage, appliquer le vol
        if (isDebugMode && canFly) {
            applyFlyMovement(deltaTime);
        }
    }

    /**
     * Applique le mouvement de vol (mode débogage ou créatif).
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    private void applyFlyMovement(float deltaTime) {
        // Dans un jeu réel, tu utiliserais les entrées clavier/souris pour contrôler le vol
        // Exemple simplifié : le joueur monte lentement
        velocity.y = flySpeed * 0.5f;
    }

    /**
     * Déplace le joueur en mode solo.
     * @param direction Direction du déplacement.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    @Override
    public void move(Vector3f direction, float deltaTime) {
        if (isDebugMode && canFly) {
            // Déplacement libre en mode vol
            super.move(direction, deltaTime);
        } else {
            // Déplacement normal
            super.move(direction, deltaTime);
        }
    }

    /**
     * Active ou désactive le mode débogage.
     * @param debugMode true pour activer le mode débogage.
     */
    public void setDebugMode(boolean debugMode) {
        this.isDebugMode = debugMode;
    }

    /**
     * Active ou désactive le vol.
     * @param canFly true pour activer le vol.
     */
    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    /**
     * Définit la vitesse de vol.
     * @param flySpeed Vitesse de vol.
     */
    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    // --- Getters ---

    public boolean isDebugMode() {
        return isDebugMode;
    }

    public boolean canFly() {
        return canFly;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    /**
     * Retourne une représentation textuelle du joueur en mode solo.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "EntityPlayerSP{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", health=" + getHealth() +
                ", position=" + getPosition() +
                ", isDebugMode=" + isDebugMode +
                ", canFly=" + canFly +
                '}';
    }
}
