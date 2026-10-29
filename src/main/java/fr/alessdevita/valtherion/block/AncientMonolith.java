package fr.alessdevita.valtherion.block;

/**
 * Monolithe Ancien - bloc massif représentant un monument ancien et énigmatique.
 */
public class AncientMonolith extends Block {
    private String purpose;
    private String inscription;
    private float age;
    private boolean isPowerfulArtifact;

    /**
     * Crée un monolithe ancien.
     */
    public AncientMonolith() {
        super(BlockType.STONE);
        this.purpose = "Inconnu";
        this.inscription = "Ancien texte";
        this.age = 100000.0f;
        this.isPowerfulArtifact = false;
    }

    @Override
    public void update(float deltaTime) {
        if (isPowerfulArtifact) {
            age += deltaTime * 0.001f;
        }
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getInscription() {
        return inscription;
    }

    public void setInscription(String inscription) {
        this.inscription = inscription;
    }

    public float getAge() {
        return age;
    }

    public boolean isPowerfulArtifact() {
        return isPowerfulArtifact;
    }

    public void setPowerfulArtifact(boolean powerful) {
        this.isPowerfulArtifact = powerful;
    }
}
