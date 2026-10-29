package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de cristal suspendu dans Valtherion.
 * Émet une lumière colorée et peut être utilisé pour des décorations ou des pièges.
 */
public class HangingCrystalBlock extends GlowingBlock {

    // Couleur du cristal
    private CrystalColor crystalColor;

    // Longueur de la chaîne (en blocs)
    private int chainLength;

    // Indique si le cristal peut tomber
    private boolean canFall;

    // Énumération des couleurs de cristal
    public enum CrystalColor {
        CLEAR("Clair", new Vector3f(1.0f, 1.0f, 1.0f)),
        RED("Rouge", new Vector3f(1.0f, 0.0f, 0.0f)),
        BLUE("Bleu", new Vector3f(0.0f, 0.0f, 1.0f)),
        GREEN("Vert", new Vector3f(0.0f, 1.0f, 0.0f)),
        PURPLE("Violet", new Vector3f(0.5f, 0.0f, 0.5f)),
        YELLOW("Jaune", new Vector3f(1.0f, 1.0f, 0.0f)),
        PINK("Rose", new Vector3f(1.0f, 0.5f, 0.8f));

        private final String displayName;
        private final Vector3f color;

        CrystalColor(String displayName, Vector3f color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Vector3f getColor() {
            return color;
        }
    }

    /**
     * Constructeur du bloc de cristal suspendu.
     */
    public HangingCrystalBlock() {
        this(CrystalColor.CLEAR, 2);
    }

    /**
     * Constructeur du bloc de cristal suspendu avec une couleur et une longueur de chaîne.
     * @param crystalColor Couleur du cristal.
     * @param chainLength Longueur de la chaîne.
     */
    public HangingCrystalBlock(CrystalColor crystalColor, int chainLength) {
        super(
                BlockType.HANGING_CRYSTAL,
                0.9f, // Niveau de lumière
                crystalColor.getColor(), // Couleur de la lumière
                10 // Rayon de la lumière
        );
        this.crystalColor = crystalColor;
        this.chainLength = chainLength;
        this.canFall = true;
    }

    /**
     * Fait tomber le cristal.
     */
    public void fall() {
        if (canFall) {
            // Logique pour faire tomber le cristal
            // Exemple : Détruire le bloc et créer une entité de cristal tombant
            System.out.println("Le cristal est tombé !");
        }
    }

    /**
     * Active le clignotement du cristal.
     */
    public void enableSparkling() {
        setFlickering(true);
        setFlickerFrequency(0.3f + (float) Math.random() * 0.4f); // Clignotement rapide
    }

    // --- Getters et Setters ---

    public CrystalColor getCrystalColor() {
        return crystalColor;
    }

    public void setCrystalColor(CrystalColor crystalColor) {
        this.crystalColor = crystalColor;
        setLightColor(crystalColor.getColor());
    }

    public int getChainLength() {
        return chainLength;
    }

    public void setChainLength(int chainLength) {
        this.chainLength = Math.max(0, chainLength);
    }

    public boolean canFall() {
        return canFall;
    }

    public void setCanFall(boolean canFall) {
        this.canFall = canFall;
    }

    /**
     * Retourne une représentation textuelle du bloc de cristal suspendu.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "HangingCrystalBlock{" +
                "crystalColor=" + crystalColor.getDisplayName() +
                ", chainLength=" + chainLength +
                ", canFall=" + canFall +
                ", lightLevel=" + getLightLevel() +
                '}';
    }
}