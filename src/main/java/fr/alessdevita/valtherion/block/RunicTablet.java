package fr.alessdevita.valtherion.block;

/**
 * Tablette Runique - bloc porteur de connaissances anciennes.
 */
public class RunicTablet extends Block {
    private String runicText;
    private String translation;
    private boolean isTranslated;
    private int difficulty;

    /**
     * Crée une tablette runique.
     */
    public RunicTablet() {
        super(BlockType.STONE);
        this.runicText = "ᚱᚢᚾᛖ";
        this.translation = "";
        this.isTranslated = false;
        this.difficulty = 5;
    }

    @Override
    public void update(float deltaTime) {
    }

    public String getRunicText() {
        return runicText;
    }

    public void setRunicText(String text) {
        this.runicText = text;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
        this.isTranslated = true;
    }

    public boolean isTranslated() {
        return isTranslated;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
