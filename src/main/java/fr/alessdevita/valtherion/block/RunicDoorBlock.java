package fr.alessdevita.valtherion.block;

/**
 * Bloc de Porte Runique - porte magique activée par des runes.
 */
public class RunicDoorBlock extends Block {
    private boolean isOpen;
    private int runeCode;
    private int currentCode;
    private boolean isLocked;

    /**
     * Crée une porte runique.
     */
    public RunicDoorBlock() {
        super(BlockType.OBSIDIAN);
        this.isOpen = false;
        this.runeCode = 12345;
        this.currentCode = 0;
        this.isLocked = true;
    }

    @Override
    public void update(float deltaTime) {
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        if (!isLocked) {
            isOpen = true;
        }
    }

    public void close() {
        isOpen = false;
    }

    public int getRuneCode() {
        return runeCode;
    }

    public void setRuneCode(int code) {
        this.runeCode = code;
    }

    public int getCurrentCode() {
        return currentCode;
    }

    public void inputRune(int rune) {
        currentCode = currentCode * 10 + rune;
        if (currentCode == runeCode) {
            isLocked = false;
            open();
        }
    }

    public void resetCode() {
        currentCode = 0;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void lock() {
        this.isLocked = true;
        close();
    }
}
