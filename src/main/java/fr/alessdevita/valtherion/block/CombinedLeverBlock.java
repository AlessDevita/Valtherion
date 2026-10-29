package fr.alessdevita.valtherion.block;

import java.util.ArrayList;
import java.util.List;

/**
 * Bloc de Levier Combiné - levier nécessitant plusieurs activations.
 */
public class CombinedLeverBlock extends Block {
    private int activationCount;
    private int requiredActivations;
    private float cooldown;
    private List<Runnable> listeners;

    /**
     * Crée un levier combiné.
     */
    public CombinedLeverBlock() {
        super(BlockType.OBSIDIAN);
        this.activationCount = 0;
        this.requiredActivations = 3;
        this.cooldown = 0.0f;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        if (cooldown > 0.0f) {
            cooldown -= deltaTime;
        }
    }

    public void activate() {
        if (cooldown <= 0.0f) {
            activationCount++;
            cooldown = 0.3f;
            if (activationCount >= requiredActivations) {
                trigger();
                activationCount = 0;
            }
        }
    }

    private void trigger() {
        notifyListeners();
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public int getActivationCount() {
        return activationCount;
    }

    public int getRequiredActivations() {
        return requiredActivations;
    }

    public void setRequiredActivations(int count) {
        this.requiredActivations = count;
    }
}
