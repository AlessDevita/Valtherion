package fr.alessdevita.valtherion.client.gui;

/**
 * ServerListEntryNormal — entree serveur "normale" de la liste :
 * nom, IP, ping, statut, nombre de joueurs, version et MOTD.
 */
public class ServerListEntryNormal
{
    /** Nom affiche. */
    public final String serverName;
    /** Adresse (host:port). */
    public final String serverAddress;
    /** Version du serveur (affichee si incompatible). */
    public String gameVersion = "";
    /** Statut du ping (compatible/incompatible/echec). */
    public String pingStatus = "";
    /** MOTD du serveur. */
    public String motd = "";
    /** Population "joueurs actuels / max" (ex : "3/20"). */
    public String populationInfo = "";
    /** Latence en ms (-1 = pas encore ping). */
    public int pingToServer = -1;
    /** true = le serveur a repondu au ping. */
    public boolean hasPingResponse = false;
    /** Icone personnalisee du serveur (base64, comme MC). */
    public String base64EncodedIconImage = "";
    /** Index de l'entree dans la ServerList sauvegardee. */
    public final int serverIndex;

    public ServerListEntryNormal(String serverName, String serverAddress, int serverIndex)
    {
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.serverIndex = serverIndex;
    }

    /** Représentation des barres de latence (nom MC : getPopulationInfo). */
    public String getLatencyString()
    {
        if (!this.hasPingResponse)
        {
            return "Scan...";
        }

        if (this.pingToServer < 0)
        {
            return "Aucune connexion";
        }
        else if (this.pingToServer < 50)
        {
            return "Excellente (" + this.pingToServer + " ms)";
        }
        else if (this.pingToServer < 150)
        {
            return "Bonne (" + this.pingToServer + " ms)";
        }
        else
        {
            return "Lente (" + this.pingToServer + " ms)";
        }
    }
}