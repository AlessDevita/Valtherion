package fr.alessdevita.valtherion.client.multiplayer;

/** Informations sauvegardées et état du dernier ping d'un serveur. */
public class ServerData {
    public enum ServerState { UNKNOWN, PINGING, ONLINE, OFFLINE }

    private String serverName;
    private String serverIP;
    private String motd = "";
    private String gameVersion = "";
    private int populationOnline;
    private int populationMax;
    private long pingToServer = -1L;
    private ServerState state = ServerState.UNKNOWN;
    private boolean lan;

    public ServerData(String serverName, String serverIP) {
        setServerName(serverName);
        setServerIP(serverIP);
    }

    public ServerData(String serverName, String serverIP, boolean lan) {
        this(serverName, serverIP);
        this.lan = lan;
    }

    public String getServerName() { return this.serverName; }
    public void setServerName(String serverName) {
        if (serverName == null || serverName.trim().isEmpty()) throw new IllegalArgumentException("Le nom du serveur est vide.");
        this.serverName = serverName.trim();
    }

    public String getServerIP() { return this.serverIP; }
    public void setServerIP(String serverIP) {
        ServerAddress parsed = ServerAddress.parseString(serverIP);
        this.serverIP = parsed.toString();
    }

    public String getMOTD() { return this.motd; }
    public void setMOTD(String motd) { this.motd = motd == null ? "" : motd; }
    public String getGameVersion() { return this.gameVersion; }
    public void setGameVersion(String gameVersion) { this.gameVersion = gameVersion == null ? "" : gameVersion; }
    public int getPopulationOnline() { return this.populationOnline; }
    public int getPopulationMax() { return this.populationMax; }
    public void setPopulationInfo(int online, int max) {
        if (online < 0 || max < 0 || online > max) throw new IllegalArgumentException("Population serveur invalide.");
        this.populationOnline = online;
        this.populationMax = max;
    }
    public long getPingToServer() { return this.pingToServer; }
    public void setPingToServer(long pingToServer) { this.pingToServer = pingToServer < 0 ? -1 : pingToServer; }
    public ServerState getState() { return this.state; }
    public void setState(ServerState state) { this.state = state == null ? ServerState.UNKNOWN : state; }
    public boolean isOnLAN() { return this.lan; }
    public void setOnLAN(boolean lan) { this.lan = lan; }

    public void copyFrom(ServerData other) {
        if (other == null) return;
        setServerName(other.serverName);
        setServerIP(other.serverIP);
        this.motd = other.motd;
        this.gameVersion = other.gameVersion;
        this.populationOnline = other.populationOnline;
        this.populationMax = other.populationMax;
        this.pingToServer = other.pingToServer;
        this.state = other.state;
        this.lan = other.lan;
    }
}
