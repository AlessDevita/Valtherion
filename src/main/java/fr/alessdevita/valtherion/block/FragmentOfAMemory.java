package fr.alessdevita.valtherion.block;

/**
 * Fragment de Mémoire - bloc qui contient un fragment d'une grande mémoire.
 */
public class FragmentOfAMemory extends Block {
    private String fragment;
    private float completeness;
    private String originStory;

    /**
     * Crée un fragment de mémoire.
     */
    public FragmentOfAMemory() {
        super(BlockType.OBSIDIAN);
        this.fragment = "";
        this.completeness = 0.3f;
        this.originStory = "Inconnu";
    }

    @Override
    public void update(float deltaTime) {
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public float getCompleteness() {
        return completeness;
    }

    public void setCompleteness(float completeness) {
        this.completeness = Math.max(0.0f, Math.min(1.0f, completeness));
    }

    public String getOriginStory() {
        return originStory;
    }

    public void setOriginStory(String story) {
        this.originStory = story;
    }

    public void addToCompleteness(float amount) {
        completeness = Math.min(1.0f, completeness + amount);
    }
}
