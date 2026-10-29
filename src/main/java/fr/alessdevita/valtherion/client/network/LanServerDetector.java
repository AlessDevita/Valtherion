package fr.alessdevita.valtherion.client.network;

import fr.alessdevita.valtherion.client.multiplayer.ServerData;
import fr.alessdevita.valtherion.client.multiplayer.TreadLanPing;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Collecte les annonces LAN reçues par {@link TreadLanPing}. */
public class LanServerDetector implements Closeable {
    private final Map<String, ServerData> servers = new LinkedHashMap<>();
    private TreadLanPing listener;

    public synchronized void start() {
        if (this.listener != null && this.listener.isRunning()) return;
        this.listener = new TreadLanPing(this::addServer);
        this.listener.start();
    }

    private synchronized void addServer(ServerData server) {
        if (server != null) this.servers.put(server.getServerIP(), server);
    }

    public synchronized List<ServerData> getLanServers() {
        return new ArrayList<>(this.servers.values());
    }

    public synchronized void clear() {
        this.servers.clear();
    }

    public synchronized boolean isScanning() {
        return this.listener != null && this.listener.isRunning();
    }

    @Override
    public synchronized void close() {
        if (this.listener != null) {
            this.listener.close();
            this.listener = null;
        }
    }
}
