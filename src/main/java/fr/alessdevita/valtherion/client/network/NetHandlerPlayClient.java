package fr.alessdevita.valtherion.client.network;

import fr.alessdevita.valtherion.network.play.client.C16PacketClientStatus;

public class NetHandlerPlayClient {
    private boolean closed;
    public void addToSendQueue(Object packet) { if (closed) throw new IllegalStateException("Handler fermé"); }
    public void cleanup() { closed = true; }
    public boolean isClosed() { return closed; }
}
