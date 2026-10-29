package fr.alessdevita.valtherion.client.gui;

import java.util.List;

import com.google.common.collect.Lists;

import fr.alessdevita.valtherion.client.Valtherion;

/**
 * ServerSelectionList — la liste scrollable des serveurs dans
 * GuiMultiplayer : entrees normales + serveurs LAN detectes
 * + entree de scan LAN en bas.
 */
public class ServerSelectionList extends GuiSlot
{
    /** Le parent GuiMultiplayer. */
    private final GuiMultiplayer parent;
    /** Entrees de la liste (Normal / LanDetected / LanScan). */
    private final List<Object> serverEntries = Lists.newArrayList();
    /** Index selectionne (-1 = rien). */
    private int selectedIndex = -1;

    /** Slots reserve a l'entree de scan LAN. */
    private static final int LAN_SCAN_SLOT = -2;

    public ServerSelectionList(GuiMultiplayer parent, Valtherion mc,
                               int width, int height, int top, int bottom)
    {
        super(mc, width, height, top, bottom, width / 2 - 110, width / 2 + 110, 36);
        this.parent = parent;
    }

    @Override
    protected int getSize()
    {
        return this.serverEntries.size();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY)
    {
        this.selectedIndex = slotIndex;
        this.parent.setSelectedServerIndex(slotIndex);

        if (doubleClick && slotIndex >= 0 && slotIndex < this.serverEntries.size())
        {
            Object entry = this.serverEntries.get(slotIndex);

            if (entry instanceof ServerListEntryNormal)
            {
                // Double-clic : connexion directe
                this.parent.connectToServer((ServerListEntryNormal) entry);
            }
        }
    }

    @Override
    protected boolean isSelected(int slotIndex)
    {
        return slotIndex == this.selectedIndex;
    }

    @Override
    protected void drawElement(int slotIndex, int x, int y, int l, int m, int mouseX, int mouseY)
    {
        Object entry = this.serverEntries.get(slotIndex);

        if (entry instanceof ServerListEntryNormal)
        {
            ServerListEntryNormal normal = (ServerListEntryNormal) entry;
            // Nom + latence
            this.valtherion.fontRenderer.drawStringWithShadow(normal.serverName, x + 2, y + 1, 0xFFFFFF);
            this.valtherion.fontRenderer.drawStringWithShadow(normal.getLatencyString(),
                    x + 2, y + 12, 0x808080);
            // MOTD + population sur la 2e ligne
            this.valtherion.fontRenderer.drawStringWithShadow(
                    normal.motd.length() > 45 ? normal.motd.substring(0, 45) + "..." : normal.motd,
                    x + 2, y + 22, 0x808080);

            if (normal.populationInfo != null && !normal.populationInfo.isEmpty())
            {
                int popWidth = this.valtherion.fontRenderer.getStringWidth(normal.populationInfo);
                this.valtherion.fontRenderer.drawStringWithShadow(normal.populationInfo,
                        x + l - popWidth - 2, y + 12, 0x808080);
            }
        }
        else if (entry instanceof ServerListEntryLanDetected)
        {
            ServerListEntryLanDetected lan = (ServerListEntryLanDetected) entry;
            this.valtherion.fontRenderer.drawStringWithShadow(lan.serverName + " (LAN)",
                    x + 2, y + 1, 0x55FFFF);
            this.valtherion.fontRenderer.drawStringWithShadow(lan.motd, x + 2, y + 12, 0x808080);
            this.valtherion.fontRenderer.drawStringWithShadow(lan.ipAddress, x + 2, y + 22, 0x404040);
        }
        else if (entry instanceof ServerListEntryLanScan)
        {
            ServerListEntryLanScan scan = (ServerListEntryLanScan) entry;
            scan.updateCounter();
            this.valtherion.fontRenderer.drawStringWithShadow(scan.getDisplayString(),
                    x + 2, y + 12, 0x808080);
        }
    }

    /** Rafraichit la liste depuis la ServerList sauvegardee + detection LAN. */
    public void refreshList(List<ServerListEntryNormal> normalServers,
                            List<ServerListEntryLanDetected> lanServers, boolean scanning)
    {
        this.serverEntries.clear();
        this.serverEntries.addAll(normalServers);

        if (!lanServers.isEmpty())
        {
            this.serverEntries.add(new ServerListEntryLanScan());
            this.serverEntries.addAll(lanServers);
        }
        else if (scanning)
        {
            this.serverEntries.add(new ServerListEntryLanScan());
        }
    }

    /** Retourne l'entree selectionnee (null si aucune). */
    public Object getSelectedEntry()
    {
        return this.selectedIndex >= 0 && this.selectedIndex < this.serverEntries.size()
                ? this.serverEntries.get(this.selectedIndex)
                : null;
    }
}