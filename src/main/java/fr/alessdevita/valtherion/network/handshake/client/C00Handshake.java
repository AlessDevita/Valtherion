package fr.alessdevita.valtherion.network.handshake.client;

import fr.alessdevita.valtherion.network.EnumConnectionState;

public class C00Handshake {
    public final int protocolVersion;
    public final String address;
    public final int port;
    public final EnumConnectionState requestedState;
    public C00Handshake() { this(0, "", 0, EnumConnectionState.LOGIN); }
    public C00Handshake(int protocolVersion, String address, int port, EnumConnectionState state) {
        this.protocolVersion = protocolVersion; this.address = address; this.port = port; this.requestedState = state;
    }
}
