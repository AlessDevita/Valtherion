package fr.alessdevita.valtherion.block;

/**
 * Bloc de Porte Magiquement Verrouillée - porte verrouillée par magie.
 */
public class MagicallyLockedDoorBlock extends Block {
    private boolean isOpen;
    private float magicalLockStrength;
    private boolean requiresSpell;
    private String spellRequired;

    /**
     * Crée une porte magiquement verrouillée.
     */
    public MagicallyLockedDoorBlock() {
        super(BlockType.OBSIDIAN);
        this.isOpen = false;
        this.magicalLockStrength = 1.0f;
        this.requiresSpell = true;
        this.spellRequired = "Unlock";
    }

    @Override
    public void update(float deltaTime) {
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        if (magicalLockStrength <= 0.0f) {
            isOpen = true;
        }
    }

    public void close() {
        isOpen = false;
        magicalLockStrength = 1.0f;
    }

    public float getMagicalLockStrength() {
        return magicalLockStrength;
    }

    public void castSpell(String spell) {
        if (spell.equals(spellRequired)) {
            magicalLockStrength -= 0.5f;
            if (magicalLockStrength <= 0.0f) {
                open();
            }
        }
    }

    public String getSpellRequired() {
        return spellRequired;
    }

    public void setSpellRequired(String spell) {
        this.spellRequired = spell;
    }

    public boolean isLocked() {
        return magicalLockStrength > 0.0f;
    }
}
