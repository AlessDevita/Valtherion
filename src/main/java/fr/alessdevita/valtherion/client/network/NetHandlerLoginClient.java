package fr.alessdevita.valtherion.client.network;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.network.core.NetworkManager;

public class NetHandlerLoginClient {
    private final NetworkManager networkManager;
    private final Valtherion client;
    private final Object guiScreen;
    public NetHandlerLoginClient() { this(null, null, null); }
    public NetHandlerLoginClient(NetworkManager networkManager, Valtherion client, Object guiScreen) {
        this.networkManager = networkManager; this.client = client; this.guiScreen = guiScreen;
    }
}
