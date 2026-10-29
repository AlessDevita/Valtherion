package fr.alessdevita.valtherion.client.entity;

import fr.alessdevita.valtherion.world.World;
import org.joml.Vector3f;

/**
 * Classe pour les autres joueurs en mode multijoueur dans Valtherion.
 * Représente les joueurs distants (non locaux) côté client.
 */
public class EntityOtherPlayerMP extends AbstractClientPlayer {

    // Nom du joueur (affiché au-dessus de l'entité)
    private String displayName;

    // Skin du joueur (chemin vers la texture)
    private String skinPath;

    // Temps écoulé depuis la dernière mise à jour (en secondes)
    private float timeSinceLastUpdate;

    // Indique si le joueur est visible
    private boolean isVisible;

    // Indique si le joueur est en train de parler (chat)
    private boolean isTalking;

    // Temps restant pour l'affichage du chat (en secondes)
    private float chatDisplayTime;

    /**
     * Constructeur des autres joueurs en mode multijoueur.
     * @param id ID de l'entité.
     * @param name Nom de l'entité.
     * @param world Monde dans lequel le joueur évolue.
     */
    public EntityOtherPlayerMP(String id, String name, World world) {
        super(id, name, world);
        this.displayName = name;
        this.skinPath = "textures/skins/default.png";
        this.timeSinceLastUpdate = 0.0f;
        this.isVisible = true;
        this.isTalking = false;
        this.chatDisplayTime = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Mettre à jour le temps écoulé depuis la dernière mise à jour
        timeSinceLastUpdate += deltaTime;

        // Mettre à jour le temps restant pour l'affichage du chat
        if (isTalking) {
            chatDisplayTime -= deltaTime;
            if (chatDisplayTime <= 0.0f) {
                isTalking = false;
            }
        }
    }

    /**
     * Met à jour la position du joueur depuis le serveur.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public void updatePositionFromServer(float x, float y, float z) {
        // Interpolation linéaire pour un mouvement fluide
        Vector3f currentPosition = getPosition();
        Vector3f targetPosition = new Vector3f(x, y, z);

        // Calculer l'interpolation (lerp) pour un mouvement fluide
        float alpha = Math.min(1.0f, timeSinceLastUpdate * 10.0f); // 10 = vitesse d'interpolation
        Vector3f newPosition = new Vector3f(
                currentPosition.x + (targetPosition.x - currentPosition.x) * alpha,
                currentPosition.y + (targetPosition.y - currentPosition.y) * alpha,
                currentPosition.z + (targetPosition.z - currentPosition.z) * alpha
        );

        setPosition(newPosition);
        timeSinceLastUpdate = 0.0f;
    }

    /**
     * Met à jour la rotation du joueur depuis le serveur.
     * @param x Rotation X.
     * @param y Rotation Y.
     * @param z Rotation Z.
     */
    public void updateRotationFromServer(float x, float y, float z) {
        // Interpolation linéaire pour une rotation fluide
        Vector3f currentRotation = getRotation();
        Vector3f targetRotation = new Vector3f(x, y, z);

        // Calculer l'interpolation (lerp) pour une rotation fluide
        float alpha = Math.min(1.0f, timeSinceLastUpdate * 10.0f);
        Vector3f newRotation = new Vector3f(
                currentRotation.x + (targetRotation.x - currentRotation.x) * alpha,
                currentRotation.y + (targetRotation.y - currentRotation.y) * alpha,
                currentRotation.z + (targetRotation.z - currentRotation.z) * alpha
        );

        setRotation(newRotation);
    }

    private void setRotation(Vector3f newRotation) {
    }

    private Vector3f getRotation() {
        // Implémentation à remplacer selon la manière dont la rotation est stockée
        return new Vector3f(0, 0, 0);
    }

    /**
     * Met à jour la santé du joueur depuis le serveur.
     * @param health Santé du joueur.
     */
    public void updateHealthFromServer(float health) {
        setHealth(health);
    }

    /**
     * Affiche le chat du joueur.
     * @param message Message du chat.
     */
    public void displayChat(String message) {
        isTalking = true;
        chatDisplayTime = 5.0f; // Afficher pendant 5 secondes
        System.out.println("[" + displayName + "]: " + message);
    }

    // --- Getters et Setters ---

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSkinPath() {
        return skinPath;
    }

    public void setSkinPath(String skinPath) {
        this.skinPath = skinPath;
    }

    public float getTimeSinceLastUpdate() {
        return timeSinceLastUpdate;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean isTalking() {
        return isTalking;
    }

    public float getChatDisplayTime() {
        return chatDisplayTime;
    }

    /**
     * Retourne une représentation textuelle des autres joueurs en mode multijoueur.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "EntityOtherPlayerMP{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", displayName='" + displayName + '\'' +
                ", health=" + getHealth() +
                ", position=" + getPosition() +
                ", skinPath='" + skinPath + '\'' +
                ", isTalking=" + isTalking +
                '}';
    }
}
