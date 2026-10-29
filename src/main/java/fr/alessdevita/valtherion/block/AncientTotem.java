package fr.alessdevita.valtherion.block;

/**
 * Totem Ancien - bloc narratif représentant une ancienne civilisation.
 */
public class AncientTotem extends Block {
    private String civilization;
    private String message;
    private int age;
    private boolean isHollow;

    /**
     * Crée un totem ancien.
     */
    public AncientTotem() {
        super(BlockType.STONE);
        this.civilization = "Civilisation Inconnue";
        this.message = "Nous avons oublié le passé...";
        this.age = 10000;
        this.isHollow = false;
    }

    @Override
    public void update(float deltaTime) {
        age++;
    }

    public String getCivilization() {
        return civilization;
    }

    public void setCivilization(String civilization) {
        this.civilization = civilization;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAge() {
        return age;
    }

    public boolean isHollow() {
        return isHollow;
    }

    public void setHollow(boolean hollow) {
        this.isHollow = hollow;
    }
}
