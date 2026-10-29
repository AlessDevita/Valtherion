package fr.alessdevita.valtherion.utils;

public class Timer {
    public float renderPartialTicks;
    public int elapsedTicks;
    private final float ticksPerSecond;
    private long lastSyncSysClock;
    private float elapsedPartialTicks;

    public Timer(float v) {
        this.ticksPerSecond = v > 0.0F ? v : 20.0F;
        this.lastSyncSysClock = System.nanoTime();
    }

    public void updateTimer() {
        long now = System.nanoTime();
        float delta = (now - this.lastSyncSysClock) / 1_000_000_000.0F;
        this.lastSyncSysClock = now;
        if (delta < 0.0F || delta > 1.0F) {
            delta = 0.0F;
        }
        this.elapsedPartialTicks += delta * this.ticksPerSecond;
        this.elapsedTicks = (int) this.elapsedPartialTicks;
        this.elapsedPartialTicks -= this.elapsedTicks;
        this.renderPartialTicks = this.elapsedPartialTicks;
    }
}
