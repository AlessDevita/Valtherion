package fr.alessdevita.valtherion.client.stream;

/**
 * Métadonnée envoyée au service de streaming pendant la diffusion.
 * <p>
 * Exemples : changement de titre, de jeu, notification d'achievement.
 */
public class Metadata {
    private final String type;
    private final String title;
    private final String description;
    private final long timestamp;

    /**
     * Construit une métadonnée.
     *
     * @param type        le type (ex: "title", "game", "achievement")
     * @param title       le titre
     * @param description la description
     */
    public Metadata(String type, String title, String description) {
        this.type = type == null ? "generic" : type;
        this.title = title == null ? "" : title;
        this.description = description == null ? "" : description;
        this.timestamp = System.currentTimeMillis();
    }

    public String getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return "Metadata[type=" + this.type
                + ", title=" + this.title
                + ", desc=" + this.description
                + ", time=" + this.timestamp + "]";
    }
}
