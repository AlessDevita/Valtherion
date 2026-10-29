package fr.alessdevita.valtherion.client.network;

import fr.alessdevita.valtherion.network.EnumConnectionState;
import fr.alessdevita.valtherion.network.core.NetworkManager;
import fr.alessdevita.valtherion.network.handshake.client.C00Handshake;
import fr.alessdevita.valtherion.network.login.client.C00PacketLoginStart;

/** Handshake client pour une connexion locale en mémoire. */
public class NetHandlerHandshakeMemory {
    public enum State { NEW, HANDSHAKE_SENT, LOGIN_SENT, COMPLETE, FAILED }

    private final NetworkManager networkManager;
    private volatile State state = State.NEW;
    private volatile String failureReason = "";

    public NetHandlerHandshakeMemory() { this(null); }

    public NetHandlerHandshakeMemory(NetworkManager networkManager) {
        this.networkManager = networkManager;
        if (networkManager != null) networkManager.setNetHandler(this);
    }

    /** Envoie la séquence initiale utilisée par le client intégré. */
    public synchronized void begin(String address, int port, String playerId) {
        if (this.state != State.NEW) throw new IllegalStateException("Le handshake a déjà commencé.");
        if (this.networkManager == null) {
            fail("Gestionnaire réseau absent.");
            return;
        }
        try {
            this.networkManager.scheduleOutboundPacket(new C00Handshake(
                    1, address == null ? "" : address, port, EnumConnectionState.LOGIN));
            this.state = State.HANDSHAKE_SENT;
            this.networkManager.scheduleOutboundPacket(new C00PacketLoginStart(playerId));
            this.state = State.LOGIN_SENT;
        } catch (RuntimeException exception) {
            fail(exception.getMessage());
        }
    }

    public synchronized void markComplete() {
        if (this.state == State.FAILED) return;
        this.state = State.COMPLETE;
    }

    public synchronized void fail(String reason) {
        this.failureReason = reason == null || reason.isBlank() ? "Connexion interrompue." : reason;
        this.state = State.FAILED;
    }

    public State getState() { return this.state; }
    public String getFailureReason() { return this.failureReason; }
    public boolean isComplete() { return this.state == State.COMPLETE; }
    public boolean isFailed() { return this.state == State.FAILED; }
}
