package fr.alessdevita.valtherion.block;

import java.util.*;

/**
 * Bloc d'Unité de Déclenchement - déclenche une chaîne d'événements.
 */
public class TriggerUnitBlock extends Block {
    private boolean hasTriggered;
    private float triggerCooldown;
    private List<Runnable> triggerListeners;

    /**
     * Crée une unité de déclenchement.
     */
    public TriggerUnitBlock() {
        super(BlockType.OBSIDIAN);
        this.hasTriggered = false;
        this.triggerCooldown = 0.0f;
        this.triggerListeners = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        if (triggerCooldown > 0.0f) {
            triggerCooldown -= deltaTime;
        }
    }

    public void trigger() {
        if (triggerCooldown <= 0.0f) {
            hasTriggered = true;
            triggerCooldown = 1.0f;
            notifyTriggerListeners();
        }
    }

    public boolean hasTriggered() {
        return hasTriggered;
    }

    public void resetTrigger() {
        hasTriggered = false;
    }

    public void addTriggerListener(Runnable listener) {
        triggerListeners.add(listener);
    }

    private void notifyTriggerListeners() {
        for (Runnable listener : triggerListeners) {
            listener.run();
        }
    }

    public float getTriggerCooldown() {
        return triggerCooldown;
    }
}
