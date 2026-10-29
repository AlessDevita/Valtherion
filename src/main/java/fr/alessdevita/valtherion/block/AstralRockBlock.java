package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de roche astrale dans Valtherion.
 * Émet une lumière bleutée et peut avoir des propriétés magiques.
 */
public class AstralRockBlock extends GlowingBlock {

    // Niveau de magie de la roche astrale (0.0 à 1.0)
    private float magicLevel;

    // Indique si la roche astrale peut être utilisée pour des rituels
    private boolean canBeUsedForRituals;

    /**
     * Constructeur du bloc de roche astrale.
     */
    public AstralRockBlock() {
        super(
                BlockType.ASTRAL_ROCK,
                0.8f, // Niveau de lumière
                new Vector3f(0.5f, 0.7f, 1.0f), // Couleur de la lumière (bleutée)
                8 // Rayon de la lumière
        );
        this.magicLevel = 0.7f;
        this.canBeUsedForRituals = true;
    }

    /**
     * Active le clignotement de la roche astrale.
     */
    public void enableFlickering() {
        setFlickering(true);
        setFlickerFrequency(0.5f + (float) Math.random() * 1.0f); // Fréquence aléatoire entre 0.5 et 1.5 secondes
    }

    /**
     * Utilise la roche astrale pour un rituel.
     * @return true si le rituel a réussi, false sinon.
     */
    public boolean useForRitual() {
        if (!canBeUsedForRituals) {
            return false;
        }

        // Logique pour utiliser la roche astrale dans un rituel
        // Exemple : Consommer de la magie
        magicLevel -= 0.1f;
        if (magicLevel < 0) {
            magicLevel = 0;
        }

        return true;
    }

    /**
     * Recharge la magie de la roche astrale.
     * @param amount Montant de magie à recharger.
     */
    public void rechargeMagic(float amount) {
        magicLevel = Math.min(1.0f, magicLevel + amount);
    }

    // --- Getters et Setters ---

    public float getMagicLevel() {
        return magicLevel;
    }

    public void setMagicLevel(float magicLevel) {
        this.magicLevel = Math.max(0.0f, Math.min(1.0f, magicLevel));
    }

    public boolean canBeUsedForRituals() {
        return canBeUsedForRituals;
    }

    public void setCanBeUsedForRituals(boolean canBeUsedForRituals) {
        this.canBeUsedForRituals = canBeUsedForRituals;
    }

    /**
     * Retourne une représentation textuelle du bloc de roche astrale.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "AstralRockBlock{" +
                "magicLevel=" + magicLevel +
                ", canBeUsedForRituals=" + canBeUsedForRituals +
                ", lightLevel=" + getLightLevel() +
                '}';
    }
}