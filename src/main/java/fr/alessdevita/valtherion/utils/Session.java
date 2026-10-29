package fr.alessdevita.valtherion.utils;

public class Session {
    private final String username;
    private final String playerId;
    public Session(String player, String uuid) {
        this.username = player == null || player.isBlank() ? "Player" : player;
        this.playerId = uuid == null || uuid.isBlank() ? "00000000-0000-0000-0000-000000000000" : uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getSessionID() {
        return playerId;
    }

    public String getPlayerID() { return playerId; }
}
