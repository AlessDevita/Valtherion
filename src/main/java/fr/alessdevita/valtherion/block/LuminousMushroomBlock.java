package fr.alessdevita.valtherion.block;

import org.joml.Vector3f;

/**
 * Bloc de champignon lumineux dans Valtherion.
 * Émet une lumière douce et peut pousser dans des environnements sombres.
 */
public class LuminousMushroomBlock extends GlowingBlock {

    // Type de champignon lumineux
    private MushroomType mushroomType;

    // Âge du champignon (0.0 à 1.0)
    private float age;

    // Vitesse de croissance du champignon
    private float growthSpeed;

    // Indique si le champignon peut se propager
    private boolean canSpread;

    // Énumération des types de champignons lumineux
    public enum MushroomType {
        SMALL("Petit", new Vector3f(0.8f, 0.6f, 0.4f), 0.5f),
        MEDIUM("Moyen", new Vector3f(0.9f, 0.7f, 0.5f), 0.7f),
        LARGE("Grand", new Vector3f(1.0f, 0.8f, 0.6f), 0.9f);

        private final String displayName;
        private final Vector3f lightColor;
        private final float lightLevel;

        MushroomType(String displayName, Vector3f lightColor, float lightLevel) {
            this.displayName = displayName;
            this.lightColor = lightColor;
            this.lightLevel = lightLevel;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Vector3f getLightColor() {
            return lightColor;
        }

        public float getLightLevel() {
            return lightLevel;
        }
    }

    /**
     * Constructeur du bloc de champignon lumineux.
     */
    public LuminousMushroomBlock() {
        this(MushroomType.SMALL);
    }

    /**
     * Constructeur du bloc de champignon lumineux avec un type spécifique.
     * @param mushroomType Type de champignon.
     */
    public LuminousMushroomBlock(MushroomType mushroomType) {
        super(
                BlockType.LUMINOUS_MUSHROOM,
                mushroomType.getLightLevel(), // Niveau de lumière
                mushroomType.getLightColor(), // Couleur de la lumière
                5 // Rayon de la lumière
        );
        this.mushroomType = mushroomType;
        this.age = 0.0f;
        this.growthSpeed = 0.01f;
        this.canSpread = true;
    }

    /**
     * Met à jour le champignon lumineux.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Faire pousser le champignon
        age += growthSpeed * deltaTime;
        if (age > 1.0f) {
            age = 1.0f;
        }

        // Mettre à jour le niveau de lumière en fonction de l'âge
        setLightLevel(mushroomType.getLightLevel() * age);
    }

    /**
     * Fait se propager le champignon à un bloc adjacent.
     * @return true si la propagation a réussi, false sinon.
     */
    public boolean spread() {
        if (!canSpread || age < 0.5f) {
            return false;
        }

        // Logique pour faire se propager le champignon
        // Exemple : Remplacer un bloc adjacent par un nouveau champignon lumineux
        return true;
    }

    /**
     * Récolte le champignon.
     * @return true si la récolte a réussi, false sinon.
     */
    public boolean harvest() {
        if (age < 1.0f) {
            return false;
        }

        // Logique pour récolter le champignon
        // Exemple : Détruire le bloc et donner un item de champignon lumineux
        return true;
    }

    // --- Getters et Setters ---

    public MushroomType getMushroomType() {
        return mushroomType;
    }

    public void setMushroomType(MushroomType mushroomType) {
        this.mushroomType = mushroomType;
        setLightColor(mushroomType.getLightColor());
        setLightLevel(mushroomType.getLightLevel() * age);
    }

    public float getAge() {
        return age;
    }

    public void setAge(float age) {
        this.age = Math.max(0.0f, Math.min(1.0f, age));
        setLightLevel(mushroomType.getLightLevel() * age);
    }

    public float getGrowthSpeed() {
        return growthSpeed;
    }

    public void setGrowthSpeed(float growthSpeed) {
        this.growthSpeed = Math.max(0.0f, growthSpeed);
    }

    public boolean canSpread() {
        return canSpread;
    }

    public void setCanSpread(boolean canSpread) {
        this.canSpread = canSpread;
    }

    /**
     * Retourne une représentation textuelle du bloc de champignon lumineux.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "LuminousMushroomBlock{" +
                "mushroomType=" + mushroomType.getDisplayName() +
                ", age=" + age +
                ", canSpread=" + canSpread +
                ", lightLevel=" + getLightLevel() +
                '}';
    }
}