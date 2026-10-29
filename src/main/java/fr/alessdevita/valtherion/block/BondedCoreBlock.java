package fr.alessdevita.valtherion.block;

/**
 * Bloc de Cœur Lié - bloc qui lie les âmes et les énergies ensemble.
 */
public class BondedCoreBlock extends Block {
    private float bondStrength;
    private int linkedBlockCount;
    private boolean isStable;
    private float energyTransfer;

    /**
     * Crée un bloc de cœur lié.
     */
    public BondedCoreBlock() {
        super(BlockType.OBSIDIAN);
        this.bondStrength = 0.5f;
        this.linkedBlockCount = 0;
        this.isStable = true;
        this.energyTransfer = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        if (!isStable) {
            bondStrength = Math.max(0.0f, bondStrength - deltaTime * 0.1f);
            if (bondStrength <= 0.0f) {
                break_();
            }
        }
        energyTransfer = (float) (0.5f + 0.5f * Math.sin(System.currentTimeMillis() * 0.002f));
    }

    public float getBondStrength() {
        return bondStrength;
    }

    public void setBondStrength(float strength) {
        this.bondStrength = Math.max(0.0f, Math.min(1.0f, strength));
    }

    public int getLinkedBlockCount() {
        return linkedBlockCount;
    }

    public void linkBlock() {
        linkedBlockCount++;
        bondStrength += 0.1f;
    }

    public void unlinkBlock() {
        linkedBlockCount = Math.max(0, linkedBlockCount - 1);
        bondStrength -= 0.05f;
    }

    public boolean isStable() {
        return isStable;
    }

    public void setStable(boolean stable) {
        this.isStable = stable;
    }

    public float getEnergyTransfer() {
        return energyTransfer;
    }

    private void break_() {
        bondStrength = 0.0f;
        linkedBlockCount = 0;
    }
}
