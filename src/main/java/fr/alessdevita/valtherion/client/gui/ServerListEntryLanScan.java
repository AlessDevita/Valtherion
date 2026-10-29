package fr.alessdevita.valtherion.client.gui;

/**
 * ServerListEntryLanScan — entree "Scan du reseau local..." affichee
 * en bas de la liste des serveurs pendant la detection LAN (UDP broadcast).
 */
public class ServerListEntryLanScan
{
    /** Texte affiche pendant le scan. */
    public static final String SCANNING = "Scan du reseau local...";

    /** Compteur d'animation des points pendant le scan. */
    private int animCounter;

    public void updateCounter()
    {
        ++this.animCounter;
    }

    public String getDisplayString()
    {
        int dots = this.animCounter / 10 % 4;
        StringBuilder sb = new StringBuilder(SCANNING);

        for (int i = 0; i < dots; ++i)
        {
            sb.append(".");
        }

        return sb.toString();
    }
}