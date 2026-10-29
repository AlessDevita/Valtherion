package fr.alessdevita.valtherion.block;

import java.util.*;

/**
 * Bloc de Minuteur - génère un signal après un délai défini.
 */
public class TimerBlock extends Block {
    private float timeRemaining;
    private float totalTime;
    private boolean isRunning;
    private List<Runnable> listeners;

    /**
     * Crée un minuteur.
     */
    public TimerBlock() {
        super(BlockType.OBSIDIAN);
        this.timeRemaining = 5.0f;
        this.totalTime = 5.0f;
        this.isRunning = false;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        if (isRunning) {
            timeRemaining -= deltaTime;
            if (timeRemaining <= 0.0f) {
                trigger();
                timeRemaining = totalTime;
            }
        }
    }

    public void start() {
        isRunning = true;
        timeRemaining = totalTime;
    }

    public void stop() {
        isRunning = false;
    }

    public void reset() {
        timeRemaining = totalTime;
    }

    public float getTimeRemaining() {
        return Math.max(0.0f, timeRemaining);
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float time) {
        this.totalTime = Math.max(0.1f, time);
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
}
