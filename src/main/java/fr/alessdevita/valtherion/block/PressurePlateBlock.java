package fr.alessdevita.valtherion.block;

import java.util.ArrayList;
import java.util.List;

/**
 * Bloc de Plaque de Pression - bloc activé par le poids.
 */
public class PressurePlateBlock extends Block {
    private boolean isPressedDown;
    private float weight;
    private float cooldown;
    private List<Runnable> listeners;

    /**
     * Crée une plaque de pression.
     */
    public PressurePlateBlock() {
        super(BlockType.OBSIDIAN);
        this.isPressedDown = false;
        this.weight = 0.0f;
        this.cooldown = 0.0f;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        if (cooldown > 0.0f) {
            cooldown -= deltaTime;
        }
        weight = Math.max(0.0f, weight - deltaTime * 0.5f);
        if (weight <= 0.1f && isPressedDown) {
            isPressedDown = false;
            notifyListeners();
        }
    }

    public void addWeight(float amount) {
        weight += amount;
        if (weight > 0.5f && !isPressedDown) {
            isPressedDown = true;
            cooldown = 0.2f;
            notifyListeners();
        }
    }

    public boolean isPressedDown() {
        return isPressedDown;
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public float getWeight() {
        return weight;
    }
}
