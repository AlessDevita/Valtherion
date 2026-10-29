package fr.alessdevita.valtherion.block;

import java.util.*;

/**
 * Bloc de Divergence - divise un signal en plusieurs branches.
 */
public class DiverterBlock extends Block {
    private boolean inputSignal;
    private List<Boolean> outputSignals;
    private List<List<Runnable>> outputListeners;

    /**
     * Crée un bloc de divergence.
     */
    public DiverterBlock() {
        super(BlockType.OBSIDIAN);
        this.inputSignal = false;
        this.outputSignals = new ArrayList<>();
        this.outputListeners = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            outputSignals.add(false);
            outputListeners.add(new ArrayList<>());
        }
    }

    @Override
    public void update(float deltaTime) {
        for (int i = 0; i < outputSignals.size(); i++) {
            if (outputSignals.get(i) != inputSignal) {
                outputSignals.set(i, inputSignal);
                notifyOutputListeners(i);
            }
        }
    }

    public void setInputSignal(boolean signal) {
        this.inputSignal = signal;
    }

    public boolean getOutputSignal(int channel) {
        if (channel >= 0 && channel < outputSignals.size()) {
            return outputSignals.get(channel);
        }
        return false;
    }

    public void addOutputListener(int channel, Runnable listener) {
        if (channel >= 0 && channel < outputListeners.size()) {
            outputListeners.get(channel).add(listener);
        }
    }

    private void notifyOutputListeners(int channel) {
        if (channel >= 0 && channel < outputListeners.size()) {
            for (Runnable listener : outputListeners.get(channel)) {
                listener.run();
            }
        }
    }
}
