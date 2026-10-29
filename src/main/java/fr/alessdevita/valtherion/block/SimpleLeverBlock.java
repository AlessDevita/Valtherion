package fr.alessdevita.valtherion.block;

import java.util.ArrayList;
import java.util.List;

/**
 * Bloc de Levier Simple - bloc d'activation basique.
 */
public class SimpleLeverBlock extends Block {
    private boolean isActivated;
    private float cooldown;
    private List<Runnable> listeners;

    /**
     * Crée un levier simple.
     */
    public SimpleLeverBlock() {
        super(BlockType.OBSIDIAN);
        this.isActivated = false;
        this.cooldown = 0.0f;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        if (cooldown > 0.0f) {
            cooldown -= deltaTime;
        }
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void toggle() {
        if (cooldown <= 0.0f) {
            isActivated = !isActivated;
            cooldown = 0.5f;
            notifyListeners();
        }
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public float getCooldown() {
        return cooldown;
    }
}
