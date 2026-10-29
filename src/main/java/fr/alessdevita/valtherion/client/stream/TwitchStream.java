package fr.alessdevita.valtherion.client.stream;

import fr.alessdevita.valtherion.client.Valtherion;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implémentation {@link IStream} pour Twitch.
 * <p>
 * Gère le cycle de vie de la diffusion (start/stop) et l'envoi de métadonnées.
 * L'authentification se fait via un token d'accès Twitch.
 */
public final class TwitchStream implements IStream {
    private static final String TWITCH_INGEST_BASE = "rtmp://live.twitch.tv/app/";
    private static final String TWITCH_API_BASE = "https://api.twitch.tv/helix";

    private final Valtherion valtherion;
    private final String accessToken;
    private final AtomicBoolean streaming = new AtomicBoolean(false);
    private final AtomicBoolean chatEnabled = new AtomicBoolean(true);
    private final ChatController chatController;
    private final BrocastController brocastController;

    private String streamKey;
    private String channelName;

    /**
     * Construit un TwitchStream.
     *
     * @param valtherion       l'instance du client
     * @param twitchAccessToken le token d'accès Twitch
     */
    public TwitchStream(Valtherion valtherion, String twitchAccessToken) {
        this.valtherion = valtherion;
        this.accessToken = twitchAccessToken;
        this.chatController = new ChatController();
        this.brocastController = new BrocastController(this);
    }

    @Override
    public String getName() {
        return "twitch";
    }

    /**
     * Configure la clé de stream et le nom de chaîne.
     *
     * @param streamKey   la clé de stream RTMP
     * @param channelName le nom de la chaîne
     */
    public void configure(String streamKey, String channelName) {
        this.streamKey = streamKey;
        this.channelName = channelName;
    }

    /** L'URL RTMP d'ingestion. */
    public String getIngestUrl() {
        return TWITCH_INGEST_BASE + (this.streamKey != null ? this.streamKey : "");
    }

    @Override
    public void startStream() {
        if (this.streaming.get()) return;
        if (this.streamKey == null || this.streamKey.isBlank()) {
            System.err.println("TwitchStream: clé de stream manquante");
            return;
        }
        this.streaming.set(true);
        this.brocastController.startBroadcast();
        if (this.chatEnabled.get()) {
            this.chatController.connect(this.channelName);
        }
    }

    @Override
    public void stopStream() {
        if (!this.streaming.get()) return;
        this.streaming.set(false);
        this.brocastController.stopBroadcast();
        this.chatController.disconnect();
    }

    @Override
    public boolean isStreaming() {
        return this.streaming.get();
    }

    @Override
    public void sendMetadata(Metadata metadata) {
        if (metadata == null || !this.streaming.get()) return;
        this.brocastController.sendMetadata(metadata);
    }

    @Override
    public void setChatEnabled(boolean enabled) {
        this.chatEnabled.set(enabled);
        if (enabled && this.streaming.get()) {
            this.chatController.connect(this.channelName);
        } else {
            this.chatController.disconnect();
        }
    }

    @Override
    public boolean isChatEnabled() {
        return this.chatEnabled.get();
    }

    @Override
    public void shutdown() {
        stopStream();
        this.chatController.shutdown();
        this.brocastController.shutdown();
    }

    /** @return le contrôleur de chat. */
    public ChatController getChatController() {
        return this.chatController;
    }

    /** @return le contrôleur de broadcast. */
    public BrocastController getBrocastController() {
        return this.brocastController;
    }

    /** @return le token d'accès (pour usage interne). */
    String getAccessToken() {
        return this.accessToken;
    }

    /** @return le nom de la chaîne. */
    public String getChannelName() {
        return this.channelName;
    }

    @Override
    public String toString() {
        return "TwitchStream[channel=" + this.channelName
                + ", streaming=" + this.streaming.get() + "]";
    }
}
