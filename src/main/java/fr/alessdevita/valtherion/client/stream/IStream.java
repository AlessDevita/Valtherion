package fr.alessdevita.valtherion.client.stream;

/**
 * Interface pour un service de streaming / broadcasting en direct.
 * <p>
 * Implémentations : {@link TwitchStream} (Twitch), {@link NullStream} (no-op).
 */
public interface IStream {

    /** Nom du service (ex: "twitch", "null"). */
    String getName();

    /** Démarre la diffusion du flux vidéo. */
    void startStream();

    /** Arrête la diffusion. */
    void stopStream();

    /** {@code true} si le stream est actif. */
    boolean isStreaming();

    /** Envoie une métadonnée au service (ex: changement de titre, achievement). */
    void sendMetadata(Metadata metadata);

    /** Met à jour le statut du chat. */
    void setChatEnabled(boolean enabled);

    /** {@code true} si le chat est activé. */
    boolean isChatEnabled();

    /** Ferme et libère les ressources du stream. */
    void shutdown();
}
