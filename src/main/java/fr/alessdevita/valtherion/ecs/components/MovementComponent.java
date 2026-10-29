package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import org.joml.Vector3f;

/**
 * Composant pour le mouvement d'une entité dans Valtherion.
 * Stocke la vitesse, la direction, et les états de mouvement.
 */
public class MovementComponent implements Component {

    // Vitesse de déplacement
    private float speed;

    // Vitesse de rotation (en degrés par seconde)
    private float rotationSpeed;

    // Direction du mouvement
    private Vector3f movementDirection;

    // Direction de la rotation
    private Vector3f rotationDirection;

    // États de mouvement
    private boolean isMovingForward;
    private boolean isMovingBackward;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isMovingUp;
    private boolean isMovingDown;

    // États de rotation
    private boolean isRotatingLeft;
    private boolean isRotatingRight;
    private boolean isRotatingUp;
    private boolean isRotatingDown;

    // Entité associée
    private Entity entity;

    /**
     * Constructeur du composant de mouvement.
     */
    public MovementComponent() {
        this.speed = 5.0f;
        this.rotationSpeed = 180.0f;
        this.movementDirection = new Vector3f(0, 0, 0);
        this.rotationDirection = new Vector3f(0, 0, 0);
        this.isMovingForward = false;
        this.isMovingBackward = false;
        this.isMovingLeft = false;
        this.isMovingRight = false;
        this.isMovingUp = false;
        this.isMovingDown = false;
        this.isRotatingLeft = false;
        this.isRotatingRight = false;
        this.isRotatingUp = false;
        this.isRotatingDown = false;
    }

    /**
     * Constructeur du composant de mouvement avec une vitesse.
     * @param speed Vitesse de déplacement.
     */
    public MovementComponent(float speed) {
        this();
        this.speed = speed;
    }

    /**
     * Constructeur du composant de mouvement avec une vitesse et une vitesse de rotation.
     * @param speed Vitesse de déplacement.
     * @param rotationSpeed Vitesse de rotation.
     */
    public MovementComponent(float speed, float rotationSpeed) {
        this(speed);
        this.rotationSpeed = rotationSpeed;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    /**
     * Réinitialise les états de mouvement.
     */
    public void resetMovementStates() {
        isMovingForward = false;
        isMovingBackward = false;
        isMovingLeft = false;
        isMovingRight = false;
        isMovingUp = false;
        isMovingDown = false;
        isRotatingLeft = false;
        isRotatingRight = false;
        isRotatingUp = false;
        isRotatingDown = false;
    }

    // --- Méthodes pour définir les états de mouvement ---

    public void setMovingForward(boolean isMovingForward) {
        this.isMovingForward = isMovingForward;
    }

    public void setMovingBackward(boolean isMovingBackward) {
        this.isMovingBackward = isMovingBackward;
    }

    public void setMovingLeft(boolean isMovingLeft) {
        this.isMovingLeft = isMovingLeft;
    }

    public void setMovingRight(boolean isMovingRight) {
        this.isMovingRight = isMovingRight;
    }

    public void setMovingUp(boolean isMovingUp) {
        this.isMovingUp = isMovingUp;
    }

    public void setMovingDown(boolean isMovingDown) {
        this.isMovingDown = isMovingDown;
    }

    public void setRotatingLeft(boolean isRotatingLeft) {
        this.isRotatingLeft = isRotatingLeft;
    }

    public void setRotatingRight(boolean isRotatingRight) {
        this.isRotatingRight = isRotatingRight;
    }

    public void setRotatingUp(boolean isRotatingUp) {
        this.isRotatingUp = isRotatingUp;
    }

    public void setRotatingDown(boolean isRotatingDown) {
        this.isRotatingDown = isRotatingDown;
    }

    // --- Méthodes pour définir la direction et la vitesse ---

    public void setMovementDirection(Vector3f movementDirection) {
        this.movementDirection = movementDirection;
    }

    public void setRotation(Vector3f rotationDirection) {
        this.rotationDirection = rotationDirection;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    // --- Getters ---

    public float getSpeed() {
        return speed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public Vector3f getMovementDirection() {
        return movementDirection;
    }

    public Vector3f getRotationDirection() {
        return rotationDirection;
    }

    public boolean isMovingForward() {
        return isMovingForward;
    }

    public boolean isMovingBackward() {
        return isMovingBackward;
    }

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public boolean isMovingUp() {
        return isMovingUp;
    }

    public boolean isMovingDown() {
        return isMovingDown;
    }

    public boolean isRotatingLeft() {
        return isRotatingLeft;
    }

    public boolean isRotatingRight() {
        return isRotatingRight;
    }

    public boolean isRotatingUp() {
        return isRotatingUp;
    }

    public boolean isRotatingDown() {
        return isRotatingDown;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "MovementComponent{" +
                "speed=" + speed +
                ", rotationSpeed=" + rotationSpeed +
                ", movementDirection=" + movementDirection +
                '}';
    }
}