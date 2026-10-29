package fr.alessdevita.valtherion.block;

import java.util.*;

/**
 * Bloc de Mémoire - stocke un état et peut le rappeler.
 */
public class MemoryBlock extends Block {
    private boolean storedState;
    private boolean currentState;
    private List<Runnable> listeners;

    /**
     * Crée un bloc de mémoire.
     */
    public MemoryBlock() {
        super(BlockType.OBSIDIAN);
        this.storedState = false;
        this.currentState = false;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
    }

    public void store() {
        storedState = currentState;
    }

    public void recall() {
        currentState = storedState;
        notifyListeners();
    }

    public void setState(boolean state) {
        this.currentState = state;
    }

    public boolean getState() {
        return currentState;
    }

    public boolean getStoredState() {
        return storedState;
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
