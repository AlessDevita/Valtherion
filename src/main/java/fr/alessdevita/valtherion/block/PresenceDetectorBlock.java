package fr.alessdevita.valtherion.block;

import java.util.ArrayList;
import java.util.List;

/**
 * Bloc de Détecteur de Présence - détecte la présence de joueurs ou d'entités.
 */
public class PresenceDetectorBlock extends Block {
    private boolean hasDetected;
    private float detectionRadius;
    private float cooldown;
    private List<Runnable> listeners;

    /**
     * Crée un détecteur de présence.
     */
    public PresenceDetectorBlock() {
        super(BlockType.OBSIDIAN);
        this.hasDetected = false;
        this.detectionRadius = 10.0f;
        this.cooldown = 0.0f;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        if (cooldown > 0.0f) {
            cooldown -= deltaTime;
        }
    }

    public void detect() {
        if (cooldown <= 0.0f && !hasDetected) {
            hasDetected = true;
            cooldown = 0.5f;
            notifyListeners();
        }
    }

    public void resetDetection() {
        hasDetected = false;
    }

    public boolean hasDetected() {
        return hasDetected;
    }

    public float getDetectionRadius() {
        return detectionRadius;
    }

    public void setDetectionRadius(float radius) {
        this.detectionRadius = radius;
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }
}
