package fr.alessdevita.valtherion.client.multiplayer;

import java.util.Objects;

/** Adresse réseau d'un serveur Valtherion, au format hôte[:port]. */
public final class ServerAddress {
    public static final int DEFAULT_PORT = 25565;

    private final String ip;
    private final int port;

    private ServerAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static ServerAddress parseString(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse du serveur est vide.");
        }

        String value = address.trim();
        String host;
        int port = DEFAULT_PORT;

        if (value.startsWith("[")) {
            int closingBracket = value.indexOf(']');
            if (closingBracket < 0) throw new IllegalArgumentException("Adresse IPv6 invalide : " + value);
            host = value.substring(1, closingBracket);
            String suffix = value.substring(closingBracket + 1);
            if (!suffix.isEmpty()) {
                if (!suffix.startsWith(":")) throw new IllegalArgumentException("Adresse invalide : " + value);
                port = parsePort(suffix.substring(1));
            }
        } else {
            int firstColon = value.indexOf(':');
            int lastColon = value.lastIndexOf(':');
            if (firstColon >= 0 && firstColon == lastColon) {
                host = value.substring(0, firstColon);
                port = parsePort(value.substring(firstColon + 1));
            } else {
                // Une adresse IPv6 sans crochets ne contient pas de port explicite.
                host = value;
            }
        }

        if (host.trim().isEmpty()) throw new IllegalArgumentException("Le nom d'hôte est vide.");
        return new ServerAddress(host, port);
    }

    private static int parsePort(String value) {
        try {
            int port = Integer.parseInt(value);
            if (port < 1 || port > 65535) throw new IllegalArgumentException("Le port doit être entre 1 et 65535.");
            return port;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Port invalide : " + value, exception);
        }
    }

    public String getIP() { return this.ip; }
    public String getHost() { return this.ip; }
    public int getPort() { return this.port; }

    @Override
    public String toString() {
        return (this.ip.contains(":") ? "[" + this.ip + "]" : this.ip) + ":" + this.port;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof ServerAddress)) return false;
        ServerAddress that = (ServerAddress) other;
        return this.port == that.port && this.ip.equalsIgnoreCase(that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ip.toLowerCase(java.util.Locale.ROOT), this.port);
    }
}
