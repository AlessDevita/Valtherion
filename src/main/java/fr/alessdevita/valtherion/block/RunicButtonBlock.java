package fr.alessdevita.valtherion.block;

import java.util.ArrayList;
import java.util.List;

/**
 * Bloc de Bouton Runique - bouton magique d'activation.
 */
public class RunicButtonBlock extends Block {
    private boolean isPressed;
    private float cooldown;
    private int runeType;
    private List<Runnable> listeners;

    /**
     * Crée un bouton runique.
     */
    public RunicButtonBlock() {
        super(BlockType.OBSIDIAN);
        this.isPressed = false;
        this.cooldown = 0.0f;
        this.runeType = 0;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        if (cooldown > 0.0f) {
            cooldown -= deltaTime;
            if (cooldown <= 0.0f && isPressed) {
                isPressed = false;
            }
        }
    }

    public void press() {
        if (cooldown <= 0.0f) {
            isPressed = true;
            cooldown = 1.0f;
            notifyListeners();
        }
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public int getRuneType() {
        return runeType;
    }

    public void setRuneType(int type) {
        this.runeType = type;
    }
}
