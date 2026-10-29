package fr.alessdevita.valtherion.rendering;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Classe pour gérer les caméras dans Valtherion.
 * Supporte plusieurs modes de caméra : FPS, orbitale, libre.
 */
public class Camera {

    // Position de la caméra
    private Vector3f position;

    // Rotation de la caméra (en degrés)
    private Vector3f rotation;

    // Matrice de vue
    private Matrix4f viewMatrix;

    // Matrice de projection
    private Matrix4f projectionMatrix;

    // Mode de la caméra (FPS, ORBITAL, FREE)
    private CameraMode mode;

    // Cible de la caméra (pour le mode orbital)
    private Vector3f target;

    // Distance de la caméra à la cible (pour le mode orbital)
    private float distance;

    // Champ de vision (FOV) en degrés
    private float fov;

    // Ratio d'aspect (width / height)
    private float aspectRatio;

    // Plan proche (near plane)
    private float nearPlane;

    // Plan éloigné (far plane)
    private float farPlane;

    // Vitesse de déplacement de la caméra
    private float moveSpeed;

    // Vitesse de rotation de la caméra
    private float rotationSpeed;

    // Vitesse de zoom de la caméra
    private float zoomSpeed;

    public void update() {

    }

    // Énumération des modes de caméra
    public enum CameraMode {
        FPS,       // Caméra FPS (First-Person Shooter)
        ORBITAL,   // Caméra orbitale (tourne autour d'une cible)
        FREE       // Caméra libre (déplacement libre dans l'espace)
    }

    /**
     * Constructeur de la caméra.
     */
    public Camera() {
        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        this.mode = CameraMode.FPS;
        this.target = new Vector3f(0.0f, 0.0f, 0.0f);
        this.distance = 5.0f;
        this.fov = 70.0f;
        this.aspectRatio = 16.0f / 9.0f;
        this.nearPlane = 0.1f;
        this.farPlane = 1000.0f;
        this.moveSpeed = 5.0f;
        this.rotationSpeed = 0.5f;
        this.zoomSpeed = 10.0f;

        updateViewMatrix();
        updateProjectionMatrix();
    }

    /**
     * Constructeur de la caméra avec une position et une rotation.
     * @param position Position de la caméra.
     * @param rotation Rotation de la caméra (en degrés).
     */
    public Camera(Vector3f position, Vector3f rotation) {
        this();
        this.position = position;
        this.rotation = rotation;
        updateViewMatrix();
    }

    /**
     * Constructeur de la caméra avec un mode.
     * @param mode Mode de la caméra.
     */
    public Camera(CameraMode mode) {
        this();
        this.mode = mode;
        if (mode == CameraMode.ORBITAL) {
            this.distance = 10.0f;
        }
    }

    /**
     * Met à jour la matrice de vue.
     */
    public void updateViewMatrix() {
        viewMatrix = new Matrix4f();

        switch (mode) {
            case FPS:
                // Caméra FPS : rotation puis translation
                viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
                viewMatrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
                viewMatrix.translate(-position.x, -position.y, -position.z);
                break;

            case ORBITAL:
                // Caméra orbitale : calculer la position en fonction de la cible et de la distance
                float theta = (float) Math.toRadians(rotation.y);
                float phi = (float) Math.toRadians(rotation.x);

                float x = (float) (distance * Math.sin(phi) * Math.cos(theta));
                float y = (float) (distance * Math.sin(phi) * Math.sin(theta));
                float z = (float) (distance * Math.cos(phi));

                position = new Vector3f(target.x + x, target.y + y, target.z + z);
                viewMatrix.lookAt(position, target, new Vector3f(0, 1, 0));
                break;

            case FREE:
                // Caméra libre : rotation puis translation
                viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
                viewMatrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
                viewMatrix.translate(-position.x, -position.y, -position.z);
                break;
        }
    }

    /**
     * Met à jour la matrice de projection.
     */
    public void updateProjectionMatrix() {
        projectionMatrix = new Matrix4f().perspective(
                (float) Math.toRadians(fov),
                aspectRatio,
                nearPlane,
                farPlane
        );
    }

    /**
     * Déplace la caméra.
     * @param direction Direction du déplacement.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void move(CameraDirection direction, float deltaTime) {
        float speed = moveSpeed * deltaTime;

        switch (mode) {
            case FPS:
                // Calculer la direction de déplacement en fonction de la rotation
                Vector3f moveDirection = new Vector3f();

                switch (direction) {
                    case FORWARD:
                        moveDirection.set(
                                (float) Math.sin(Math.toRadians(rotation.y)),
                                0,
                                (float) Math.cos(Math.toRadians(rotation.y))
                        );
                        break;
                    case BACKWARD:
                        moveDirection.set(
                                -(float) Math.sin(Math.toRadians(rotation.y)),
                                0,
                                -(float) Math.cos(Math.toRadians(rotation.y))
                        );
                        break;
                    case LEFT:
                        moveDirection.set(
                                (float) Math.sin(Math.toRadians(rotation.y - 90)),
                                0,
                                (float) Math.cos(Math.toRadians(rotation.y - 90))
                        );
                        break;
                    case RIGHT:
                        moveDirection.set(
                                (float) Math.sin(Math.toRadians(rotation.y + 90)),
                                0,
                                (float) Math.cos(Math.toRadians(rotation.y + 90))
                        );
                        break;
                    case UP:
                        moveDirection.set(0, 1, 0);
                        break;
                    case DOWN:
                        moveDirection.set(0, -1, 0);
                        break;
                }

                position.add(moveDirection.mul(speed));
                break;

            case ORBITAL:
                // Déplacer la cible dans le mode orbital
                moveDirection = new Vector3f();

                switch (direction) {
                    case FORWARD:
                        moveDirection.set(
                                (float) Math.sin(Math.toRadians(rotation.y)),
                                0,
                                (float) Math.cos(Math.toRadians(rotation.y))
                        );
                        break;
                    case BACKWARD:
                        moveDirection.set(
                                -(float) Math.sin(Math.toRadians(rotation.y)),
                                0,
                                -(float) Math.cos(Math.toRadians(rotation.y))
                        );
                        break;
                    case LEFT:
                        moveDirection.set(
                                (float) Math.sin(Math.toRadians(rotation.y - 90)),
                                0,
                                (float) Math.cos(Math.toRadians(rotation.y - 90))
                        );
                        break;
                    case RIGHT:
                        moveDirection.set(
                                (float) Math.sin(Math.toRadians(rotation.y + 90)),
                                0,
                                (float) Math.cos(Math.toRadians(rotation.y + 90))
                        );
                        break;
                    case UP:
                        moveDirection.set(0, 1, 0);
                        break;
                    case DOWN:
                        moveDirection.set(0, -1, 0);
                        break;
                }

                target.add(moveDirection.mul(speed));
                break;

            case FREE:
                // Déplacer la caméra librement
                Vector3f freeMoveDirection = new Vector3f();

                switch (direction) {
                    case FORWARD:
                        freeMoveDirection.set(
                                (float) Math.sin(Math.toRadians(rotation.y)),
                                0,
                                (float) Math.cos(Math.toRadians(rotation.y))
                        );
                        break;
                    case BACKWARD:
                        freeMoveDirection.set(
                                -(float) Math.sin(Math.toRadians(rotation.y)),
                                0,
                                -(float) Math.cos(Math.toRadians(rotation.y))
                        );
                        break;
                    case LEFT:
                        freeMoveDirection.set(
                                (float) Math.sin(Math.toRadians(rotation.y - 90)),
                                0,
                                (float) Math.cos(Math.toRadians(rotation.y - 90))
                        );
                        break;
                    case RIGHT:
                        freeMoveDirection.set(
                                (float) Math.sin(Math.toRadians(rotation.y + 90)),
                                0,
                                (float) Math.cos(Math.toRadians(rotation.y + 90))
                        );
                        break;
                    case UP:
                        freeMoveDirection.set(0, 1, 0);
                        break;
                    case DOWN:
                        freeMoveDirection.set(0, -1, 0);
                        break;
                }

                position.add(freeMoveDirection.mul(speed));
                break;
        }

        updateViewMatrix();
    }

    /**
     * Fait tourner la caméra.
     * @param dx Déplacement horizontal de la souris.
     * @param dy Déplacement vertical de la souris.
     */
    public void rotate(float dx, float dy) {
        switch (mode) {
            case FPS:
                rotation.y += dx * rotationSpeed;
                rotation.x -= dy * rotationSpeed;

                // Limiter la rotation verticale pour éviter de voir à travers le sol
                rotation.x = Math.max(-89.0f, Math.min(89.0f, rotation.x));
                break;

            case ORBITAL:
                rotation.y += dx * rotationSpeed;
                rotation.x -= dy * rotationSpeed;

                // Limiter la rotation verticale pour éviter de voir à travers le sol
                rotation.x = Math.max(-89.0f, Math.min(89.0f, rotation.x));
                break;

            case FREE:
                rotation.y += dx * rotationSpeed;
                rotation.x -= dy * rotationSpeed;
                break;
        }

        updateViewMatrix();
    }

    /**
     * Effectue un zoom sur la caméra.
     * @param amount Quantité de zoom (positive pour zoomer, négative pour dézoomer).
     */
    public void zoom(float amount) {
        if (mode == CameraMode.ORBITAL) {
            distance -= amount * zoomSpeed;
            distance = Math.max(1.0f, distance); // Limiter la distance minimale
            updateViewMatrix();
        }
    }

    /**
     * Définit la cible de la caméra (pour le mode orbital).
     * @param target Cible de la caméra.
     */
    public void setTarget(Vector3f target) {
        this.target = target;
        updateViewMatrix();
    }

    /**
     * Définit la distance de la caméra à la cible (pour le mode orbital).
     * @param distance Distance de la caméra à la cible.
     */
    public void setDistance(float distance) {
        this.distance = distance;
        updateViewMatrix();
    }

    /**
     * Définit le champ de vision (FOV).
     * @param fov Champ de vision en degrés.
     */
    public void setFov(float fov) {
        this.fov = fov;
        updateProjectionMatrix();
    }

    /**
     * Définit le ratio d'aspect.
     * @param aspectRatio Ratio d'aspect (width / height).
     */
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        updateProjectionMatrix();
    }

    /**
     * Définit les plans proche et éloigné.
     * @param nearPlane Plan proche.
     * @param farPlane Plan éloigné.
     */
    public void setClippingPlanes(float nearPlane, float farPlane) {
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
        updateProjectionMatrix();
    }

    // --- Énumération des directions de la caméra ---

    public enum CameraDirection {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    // --- Getters et Setters ---

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        updateViewMatrix();
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
        updateViewMatrix();
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public CameraMode getMode() {
        return mode;
    }

    public void setMode(CameraMode mode) {
        this.mode = mode;
        updateViewMatrix();
    }

    public Vector3f getTarget() {
        return target;
    }

    public float getDistance() {
        return distance;
    }

    public float getFov() {
        return fov;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public float getNearPlane() {
        return nearPlane;
    }

    public float getFarPlane() {
        return farPlane;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getZoomSpeed() {
        return zoomSpeed;
    }

    public void setZoomSpeed(float zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
    }

    /**
     * Retourne une représentation textuelle de la caméra.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Camera{" +
                "position=" + position +
                ", rotation=" + rotation +
                ", mode=" + mode +
                ", fov=" + fov +
                ", aspectRatio=" + aspectRatio +
                '}';
    }
}