package fr.alessdevita.valtherion.network.login.client;

public class C00PacketLoginStart {
    private final String playerId;
    public C00PacketLoginStart() { this(""); }
    public C00PacketLoginStart(String playerId) { this.playerId = playerId == null ? "" : playerId; }
    public String getPlayerId() { return playerId; }
}
