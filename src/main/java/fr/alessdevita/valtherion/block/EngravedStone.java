package fr.alessdevita.valtherion.block;

/**
 * Pierre Gravée - bloc qui porte des inscriptions et des gravures.
 */
public class EngravedStone extends Block {
    private String inscription;
    private int age;
    private String language;
    private boolean isDeciphered;

    /**
     * Crée une pierre gravée.
     */
    public EngravedStone() {
        super(BlockType.STONE);
        this.inscription = "";
        this.age = 0;
        this.language = "Ancien";
        this.isDeciphered = false;
    }

    @Override
    public void update(float deltaTime) {
        age++;
    }

    public String getInscription() {
        return inscription;
    }

    public void setInscription(String inscription) {
        this.inscription = inscription;
    }

    public int getAge() {
        return age;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isDeciphered() {
        return isDeciphered;
    }

    public void decipher() {
        isDeciphered = true;
    }
}
