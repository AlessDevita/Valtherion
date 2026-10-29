package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de pierre violette lumineuse dans Valtherion.
 * Émet une lumière violette et peut être utilisé pour des rituels ou des constructions magiques.
 */
public class PurpleGlowingStoneBlock extends GlowingBlock {

    // Niveau de puissance magique de la pierre
    private float magicPower;

    // Indique si la pierre peut être utilisée pour des sorts
    private boolean canBeUsedForSpells;

    // Type de pierre violette
    private PurpleStoneType stoneType;

    // Énumération des types de pierre violette
    public enum PurpleStoneType {
        AMETHYST("Améthyste", new Vector3f(0.6f, 0.2f, 0.8f), 0.8f),
        QUARTZ("Quartz Violet", new Vector3f(0.7f, 0.5f, 0.9f), 0.7f),
        OBSIDIAN("Obsidienne Violette", new Vector3f(0.3f, 0.1f, 0.5f), 0.6f);

        private final String displayName;
        private final Vector3f lightColor;
        private final float baseLightLevel;

        PurpleStoneType(String displayName, Vector3f lightColor, float baseLightLevel) {
            this.displayName = displayName;
            this.lightColor = lightColor;
            this.baseLightLevel = baseLightLevel;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Vector3f getLightColor() {
            return lightColor;
        }

        public float getBaseLightLevel() {
            return baseLightLevel;
        }
    }

    /**
     * Constructeur du bloc de pierre violette lumineuse.
     */
    public PurpleGlowingStoneBlock() {
        this(PurpleStoneType.AMETHYST);
    }

    /**
     * Constructeur du bloc de pierre violette lumineuse avec un type spécifique.
     * @param stoneType Type de pierre violette.
     */
    public PurpleGlowingStoneBlock(PurpleStoneType stoneType) {
        super(
                BlockType.PURPLE_GLOWING_STONE,
                stoneType.getBaseLightLevel(), // Niveau de lumière
                stoneType.getLightColor(), // Couleur de la lumière
                7 // Rayon de la lumière
        );
        this.stoneType = stoneType;
        this.magicPower = 0.8f;
        this.canBeUsedForSpells = true;
    }

    /**
     * Utilise la pierre pour un sort.
     * @param spellPower Puissance du sort.
     * @return true si le sort a réussi, false sinon.
     */
    public boolean useForSpell(float spellPower) {
        if (!canBeUsedForSpells) {
            return false;
        }

        // Logique pour utiliser la pierre pour un sort
        if (magicPower < spellPower) {
            return false;
        }

        magicPower -= spellPower;
        if (magicPower < 0) {
            magicPower = 0;
        }

        // Mettre à jour le niveau de lumière en fonction de la puissance magique
        setLightLevel(stoneType.getBaseLightLevel() * (magicPower / 0.8f));

        return true;
    }

    /**
     * Recharge la puissance magique de la pierre.
     * @param amount Montant de puissance magique à recharger.
     */
    public void rechargeMagic(float amount) {
        magicPower = Math.min(1.0f, magicPower + amount);
        setLightLevel(stoneType.getBaseLightLevel() * (magicPower / 0.8f));
    }

    /**
     * Active le clignotement de la pierre violette.
     */
    public void enablePulsing() {
        setFlickering(true);
        setFlickerFrequency(0.8f + (float) Math.random() * 0.4f); // Clignotement entre 0.8 et 1.2 secondes
    }

    // --- Getters et Setters ---

    public PurpleStoneType getStoneType() {
        return stoneType;
    }

    public void setStoneType(PurpleStoneType stoneType) {
        this.stoneType = stoneType;
        setLightColor(stoneType.getLightColor());
        setLightLevel(stoneType.getBaseLightLevel() * (magicPower / 0.8f));
    }

    public float getMagicPower() {
        return magicPower;
    }

    public void setMagicPower(float magicPower) {
        this.magicPower = Math.max(0.0f, Math.min(1.0f, magicPower));
        setLightLevel(stoneType.getBaseLightLevel() * (magicPower / 0.8f));
    }

    public boolean canBeUsedForSpells() {
        return canBeUsedForSpells;
    }

    public void setCanBeUsedForSpells(boolean canBeUsedForSpells) {
        this.canBeUsedForSpells = canBeUsedForSpells;
    }

    /**
     * Retourne une représentation textuelle du bloc de pierre violette lumineuse.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "PurpleGlowingStoneBlock{" +
                "stoneType=" + stoneType.getDisplayName() +
                ", magicPower=" + magicPower +
                ", canBeUsedForSpells=" + canBeUsedForSpells +
                ", lightLevel=" + getLightLevel() +
                '}';
    }
}