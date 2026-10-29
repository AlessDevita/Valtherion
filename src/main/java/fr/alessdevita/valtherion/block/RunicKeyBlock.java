package fr.alessdevita.valtherion.block;

/**
 * Bloc de Clé Runique - clé pour déverrouiller les portes runiques.
 */
public class RunicKeyBlock extends Block {
    private int keyCode;
    private boolean isUsed;
    private String doorId;

    /**
     * Crée une clé runique.
     */
    public RunicKeyBlock() {
        super(BlockType.OBSIDIAN);
        this.keyCode = 0;
        this.isUsed = false;
        this.doorId = "";
    }

    @Override
    public void update(float deltaTime) {
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int code) {
        this.keyCode = code;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void use() {
        this.isUsed = true;
    }

    public void reset() {
        this.isUsed = false;
    }

    public String getDoorId() {
        return doorId;
    }

    public void setDoorId(String id) {
        this.doorId = id;
    }

    public boolean canUnlockDoor(String targetDoorId) {
        return doorId.equals(targetDoorId) && !isUsed;
    }
}
