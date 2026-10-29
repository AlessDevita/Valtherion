package fr.alessdevita.valtherion.block;

/**
 * Bloc de Pierre de Rune - pierre magique contenant des runes.
 */
public class RuneStoneBlock extends Block {
    private String runeText;
    private float magicPower;
    private int xpDropped;

    /**
     * Crée un bloc de pierre de rune.
     */
    public RuneStoneBlock() {
        super(BlockType.STONE);
        this.runeText = "ᚱᚢᚾᛖ";
        this.magicPower = 0.7f;
        this.xpDropped = 25;
    }

    @Override
    public void update(float deltaTime) {
    }

    public String getRuneText() {
        return runeText;
    }

    public void setRuneText(String runeText) {
        this.runeText = runeText;
    }

    public float getMagicPower() {
        return magicPower;
    }

    public void setMagicPower(float power) {
        this.magicPower = Math.max(0.0f, Math.min(1.0f, power));
    }

    public int getXpDropped() {
        return xpDropped;
    }
}
