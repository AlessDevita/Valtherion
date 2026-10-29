package fr.alessdevita.valtherion.client.gui;

/**
 * ServerListEntryLanDetected — entree "serveur LAN detecte" :
 * nom "Server sur <nom d'hote>", ping en cours, MOTD du monde LAN.
 */
public class ServerListEntryLanDetected
{
    /** Nom du serveur detecte (nom du monde LAN broadcast). */
    public final String serverName;
    /** Adresse IP locale (host:port). */
    public final String ipAddress;
    /** MOTD broadcast par le serveur LAN. */
    public final String motd;
    /** true = ping termine avec reponse. */
    public boolean hasPingResponse = false;
    /** Latence en ms (-1 = inconnu). */
    public int pingToServer = -1;

    public ServerListEntryLanDetected(String serverName, String ipAddress, String motd)
    {
        this.serverName = serverName;
        this.ipAddress = ipAddress;
        this.motd = motd;
    }
}