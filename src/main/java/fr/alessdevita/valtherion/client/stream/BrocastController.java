package fr.alessdevita.valtherion.client.stream;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Contrôleur de broadcast : gère le cycle de vie de la diffusion RTMP
 * et la file de métadonnées à envoyer.
 * <p>
 * Associé à un {@link IStream} (typiquement {@link TwitchStream}).
 */
public final class BrocastController {
    private final IStream stream;
    private final AtomicBoolean broadcasting = new AtomicBoolean(false);
    private final Queue<Metadata> metadataQueue = new ConcurrentLinkedQueue<>();

    /**
     * Construit un contrôleur pour le stream donné.
     *
     * @param stream le stream associé
     */
    public BrocastController(IStream stream) {
        this.stream = stream;
    }

    /** Démarre la diffusion. */
    public void startBroadcast() {
        if (this.broadcasting.compareAndSet(false, true)) {
            // Le démarrage réel de l'encodeur RTMP serait branché ici.
        }
    }

    /** Arrête la diffusion. */
    public void stopBroadcast() {
        if (this.broadcasting.compareAndSet(true, false)) {
            this.metadataQueue.clear();
        }
    }

    /** {@code true} si la diffusion est active. */
    public boolean isBroadcasting() {
        return this.broadcasting.get();
    }

    /**
     * Enqueue une métadonnée à envoyer pendant la diffusion.
     *
     * @param metadata la métadonnée
     */
    public void sendMetadata(Metadata metadata) {
        if (metadata == null || !this.broadcasting.get()) return;
        this.metadataQueue.add(metadata);
    }

    /**
     * Consomme et retourne la prochaine métadonnée en attente.
     *
     * @return la prochaine métadonnée, ou {@code null}
     */
    public Metadata pollMetadata() {
        return this.metadataQueue.poll();
    }

    /** Nombre de métadonnées en attente. */
    public int getPendingMetadataCount() {
        return this.metadataQueue.size();
    }

    /** Libère les ressources. */
    public void shutdown() {
        stopBroadcast();
    }

    /** @return le stream associé. */
    public IStream getStream() {
        return this.stream;
    }

    @Override
    public String toString() {
        return "BrocastController[broadcasting=" + this.broadcasting.get()
                + ", pending=" + this.metadataQueue.size() + "]";
    }
}
