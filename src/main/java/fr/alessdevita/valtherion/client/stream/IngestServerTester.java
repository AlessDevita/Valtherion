package fr.alessdevita.valtherion.client.stream;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Teste la latence et la disponibilité des serveurs d'ingestion
 * pour choisir le meilleur avant de démarrer une diffusion.
 */
public final class IngestServerTester {
    private final List<IngestServer> servers = new CopyOnWriteArrayList<>();
    private final AtomicBoolean testing = new AtomicBoolean(false);

    /**
     * Représente un serveur d'ingestion.
     */
    public static final class IngestServer {
        private final String name;
        private final String url;
        private volatile long latency = -1;
        private volatile boolean available;

        public IngestServer(String name, String url) {
            this.name = name == null ? "?" : name;
            this.url = url == null ? "" : url;
        }

        public String getName() { return this.name; }
        public String getUrl() { return this.url; }
        public long getLatency() { return this.latency; }
        public boolean isAvailable() { return this.available; }

        void setLatency(long latency) { this.latency = latency; }
        void setAvailable(boolean available) { this.available = available; }

        @Override
        public String toString() {
            return "IngestServer[" + this.name
                    + ", url=" + this.url
                    + ", latency=" + this.latency + "ms"
                    + ", available=" + this.available + "]";
        }
    }

    /**
     * Ajoute un serveur à tester.
     *
     * @param name le nom du serveur
     * @param url  l'URL d'ingestion
     */
    public void addServer(String name, String url) {
        this.servers.add(new IngestServer(name, url));
    }

    /**
     * Teste tous les serveurs enregistrés en parallèle et mesure leur latence.
     */
    public void testServers() {
        if (!this.testing.compareAndSet(false, true)) return;
        java.util.concurrent.ExecutorService executor =
                java.util.concurrent.Executors.newCachedThreadPool(r -> {
                    Thread thread = new Thread(r, "IngestServerTester");
                    thread.setDaemon(true);
                    return thread;
                });
        for (IngestServer server : this.servers) {
            executor.submit(() -> testServer(server));
        }
        executor.shutdown();
        this.testing.set(false);
    }

    /**
     * Teste un serveur individuel.
     *
     * @param server le serveur à tester
     */
    private void testServer(IngestServer server) {
        long start = System.currentTimeMillis();
        try {
            java.net.URL url = new java.net.URL(server.getUrl());
            java.net.HttpURLConnection connection =
                    (java.net.HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("HEAD");
            int code = connection.getResponseCode();
            long elapsed = System.currentTimeMillis() - start;
            server.setLatency(elapsed);
            server.setAvailable(code >= 200 && code < 400);
        } catch (Exception e) {
            server.setLatency(-1);
            server.setAvailable(false);
        }
    }

    /**
     * Retourne le serveur avec la latence la plus faible parmi ceux disponibles.
     *
     * @return le meilleur serveur, ou {@code null} si aucun disponible
     */
    public IngestServer getBestServer() {
        IngestServer best = null;
        for (IngestServer server : this.servers) {
            if (!server.isAvailable() || server.getLatency() < 0) continue;
            if (best == null || server.getLatency() < best.getLatency()) {
                best = server;
            }
        }
        return best;
    }

    /** @return la liste des serveurs enregistrés. */
    public List<IngestServer> getServers() {
        return java.util.Collections.unmodifiableList(this.servers);
    }

    /** {@code true} si un test est en cours. */
    public boolean isTesting() {
        return this.testing.get();
    }

    /** Ajoute les serveurs d'ingestion Twitch par défaut. */
    public void addDefaultTwitchServers() {
        addServer("Twitch Primary", "https://live.twitch.tv");
        addServer("Twitch EU - Amsterdam", "https://ams.contribute.live-video.net");
        addServer("Twitch EU - Frankfurt", "https://fra.contribute.live-video.net");
        addServer("Twitch US East", "https://iad.contribute.live-video.net");
        addServer("Twitch US West", "https://lax.contribute.live-video.net");
    }

    @Override
    public String toString() {
        return "IngestServerTester[servers=" + this.servers.size()
                + ", testing=" + this.testing.get() + "]";
    }
}
