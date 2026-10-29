package fr.alessdevita.valtherion.block;

/**
 * Bloc de Cristal de Mana - cristal contenant de l'énergie magique.
 */
public class ManaCrystalBlock extends Block {
    private float manaContent;
    private float manaRechargeRate;
    private int xpDropped;

    /**
     * Crée un bloc de cristal de mana.
     */
    public ManaCrystalBlock() {
        super(BlockType.OBSIDIAN);
        this.manaContent = 1.0f;
        this.manaRechargeRate = 0.1f;
        this.xpDropped = 30;
    }

    @Override
    public void update(float deltaTime) {
        manaContent = Math.min(1.0f, manaContent + manaRechargeRate * deltaTime);
    }

    public float getManaContent() {
        return manaContent;
    }

    public void setManaContent(float mana) {
        this.manaContent = Math.max(0.0f, Math.min(1.0f, mana));
    }

    public float getManaRechargeRate() {
        return manaRechargeRate;
    }

    public void setManaRechargeRate(float rate) {
        this.manaRechargeRate = rate;
    }

    public int getXpDropped() {
        return xpDropped;
    }

    public void extractMana(float amount) {
        manaContent = Math.max(0.0f, manaContent - amount);
    }
}
