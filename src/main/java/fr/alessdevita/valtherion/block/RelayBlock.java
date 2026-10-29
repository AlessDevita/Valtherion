package fr.alessdevita.valtherion.block;

import java.util.*;

/**
 * Bloc de Relais - relaie un signal d'une entrée à une sortie.
 */
public class RelayBlock extends Block {
    private boolean inputSignal;
    private boolean outputSignal;
    private List<Runnable> outputListeners;
    private float delay;

    /**
     * Crée un bloc de relais.
     */
    public RelayBlock() {
        super(BlockType.OBSIDIAN);
        this.inputSignal = false;
        this.outputSignal = false;
        this.outputListeners = new ArrayList<>();
        this.delay = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        if (delay > 0.0f) {
            delay -= deltaTime;
        } else if (inputSignal != outputSignal) {
            outputSignal = inputSignal;
            notifyOutputListeners();
        }
    }

    public void setInputSignal(boolean signal) {
        this.inputSignal = signal;
    }

    public boolean getOutputSignal() {
        return outputSignal;
    }

    public void addOutputListener(Runnable listener) {
        outputListeners.add(listener);
    }

    private void notifyOutputListeners() {
        for (Runnable listener : outputListeners) {
            listener.run();
        }
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }
}
