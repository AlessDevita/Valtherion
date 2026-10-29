package fr.alessdevita.valtherion.block;

/**
 * Cristal de Mémoire - bloc qui stocke les souvenirs et les histoires.
 */
public class MemoryCrystal extends Block {
    private String memory;
    private float clarity;
    private boolean isActive;
    private long timestamp;

    /**
     * Crée un cristal de mémoire.
     */
    public MemoryCrystal() {
        super(BlockType.OBSIDIAN);
        this.memory = "";
        this.clarity = 1.0f;
        this.isActive = true;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void update(float deltaTime) {
        if (isActive) {
            clarity = Math.max(0.0f, clarity - deltaTime * 0.05f);
        }
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
        this.clarity = 1.0f;
    }

    public float getClarity() {
        return clarity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void refresh() {
        clarity = 1.0f;
    }
}
