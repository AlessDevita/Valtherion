package fr.alessdevita.valtherion.client.stream;

/**
 * Implémentation no-op de {@link IStream}.
 * <p>
 * Utilisée quand le streaming est indisponible ou a échoué.
 * Toutes les opérations sont des no-ops. La cause de l'échec est conservée.
 */
public final class NullStream implements IStream {
    private final Throwable error;
    private boolean chatEnabled;

    /**
     * Construit un NullStream avec la cause de l'échec.
     *
     * @param error la cause de l'échec du stream, ou {@code null}
     */
    public NullStream(Throwable error) {
        this.error = error;
    }

    @Override
    public String getName() {
        return "null";
    }

    @Override
    public void startStream() { }

    @Override
    public void stopStream() { }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void sendMetadata(Metadata metadata) { }

    @Override
    public void setChatEnabled(boolean enabled) {
        this.chatEnabled = enabled;
    }

    @Override
    public boolean isChatEnabled() {
        return this.chatEnabled;
    }

    @Override
    public void shutdown() { }

    /** @return la cause de l'échec, ou {@code null}. */
    public Throwable getError() {
        return this.error;
    }

    @Override
    public String toString() {
        return "NullStream[error=" + (this.error != null ? this.error.getMessage() : "none") + "]";
    }
}
