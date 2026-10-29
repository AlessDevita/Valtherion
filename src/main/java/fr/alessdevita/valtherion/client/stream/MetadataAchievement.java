package fr.alessdevita.valtherion.client.stream;

/**
 * Métadonnée spécifique à un achievement débloqué pendant le stream.
 * <p>
 * Envoyée au service pour afficher une notification sur la diffusion.
 */
public final class MetadataAchievement extends Metadata {
    private final String achievementId;
    private final String achievementName;

    /**
     * Construit une métadonnée d'achievement.
     *
     * @param achievementId   l'identifiant de l'achievement
     * @param achievementName le nom d'affichage
     */
    public MetadataAchievement(String achievementId, String achievementName) {
        super("achievement",
              achievementName != null ? achievementName : "Achievement",
              achievementId != null ? achievementId : "");
        this.achievementId = achievementId == null ? "" : achievementId;
        this.achievementName = achievementName == null ? "" : achievementName;
    }

    public String getAchievementId() {
        return this.achievementId;
    }

    public String getAchievementName() {
        return this.achievementName;
    }

    @Override
    public String toString() {
        return "MetadataAchievement[id=" + this.achievementId
                + ", name=" + this.achievementName + "]";
    }
}
