package fr.alessdevita.valtherion.client.network;

import fr.alessdevita.valtherion.client.gui.ServerListEntryNormal;
import fr.alessdevita.valtherion.client.multiplayer.ServerAddress;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/** Vérifie en arrière-plan si le port TCP d'un serveur est accessible. */
public class OldServerPinger implements Closeable {
    private static final int CONNECT_TIMEOUT_MILLIS = 2500;

    private final ExecutorService executor;
    private final Set<Socket> pendingSockets = ConcurrentHashMap.newKeySet();
    private final Set<Future<?>> pendingPings = ConcurrentHashMap.newKeySet();
    private volatile boolean closed;

    public OldServerPinger() {
        ThreadFactory factory = task -> {
            Thread thread = new Thread(task, "valtherion-server-ping");
            thread.setDaemon(true);
            return thread;
        };
        this.executor = Executors.newFixedThreadPool(4, factory);
    }

    /** Lance un ping TCP et met à jour l'entrée lorsqu'il se termine. */
    public Future<?> ping(ServerListEntryNormal entry) {
        if (entry == null) throw new IllegalArgumentException("L'entrée serveur est requise.");
        if (this.closed) throw new IllegalStateException("Le pinger est fermé.");

        entry.hasPingResponse = false;
        entry.pingToServer = -1;
        entry.pingStatus = "Connexion…";
        Future<?> future = this.executor.submit(() -> pingEntry(entry));
        this.pendingPings.add(future);
        return future;
    }

    private void pingEntry(ServerListEntryNormal entry) {
        long startedAt = System.nanoTime();
        Socket socket = new Socket();
        this.pendingSockets.add(socket);
        try {
            ServerAddress address = ServerAddress.parseString(entry.serverAddress);
            socket.connect(new InetSocketAddress(address.getHost(), address.getPort()), CONNECT_TIMEOUT_MILLIS);
            long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
            entry.pingToServer = (int) Math.min(Integer.MAX_VALUE, elapsedMillis);
            entry.pingStatus = "Port accessible";
        } catch (IOException | IllegalArgumentException exception) {
            entry.pingToServer = -1;
            entry.pingStatus = "Serveur inaccessible";
        } finally {
            entry.hasPingResponse = true;
            this.pendingSockets.remove(socket);
            try {
                socket.close();
            } catch (IOException ignored) {
                // Le ping est terminé ; la fermeture est une opération de nettoyage.
            }
        }
    }

    /** Annule les pings encore en attente et ferme leurs sockets. */
    public void clearPendingNetworks() {
        for (Future<?> future : this.pendingPings) future.cancel(true);
        this.pendingPings.clear();
        for (Socket socket : this.pendingSockets) {
            try {
                socket.close();
            } catch (IOException ignored) {
                // La socket peut déjà être fermée par le worker.
            }
        }
        this.pendingSockets.clear();
    }

    /** Retire de la liste les tâches de ping déjà terminées. */
    public void pingPendingNetworks() {
        this.pendingPings.removeIf(Future::isDone);
    }

    public boolean isPinging() {
        return this.pendingPings.stream().anyMatch(future -> !future.isDone());
    }

    @Override
    public void close() {
        if (this.closed) return;
        this.closed = true;
        clearPendingNetworks();
        this.executor.shutdownNow();
    }
}
