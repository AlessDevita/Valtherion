package fr.alessdevita.valtherion.client.gui;

/**
 * GuiResourcePackSelected — un pack ACTIF (colonne droite de l'ecran
 * de resource packs). Ordre = priorite (le premier ecrase les autres).
 */
public class GuiResourcePackSelected
{
    public final String resourcePackName;
    public final String description;
    public final String resourcePackFilePath;
    public final int packFormat;

    public GuiResourcePackSelected(String name, String desc, String filePath, int format)
    {
        this.resourcePackName = name;
        this.description = desc;
        this.resourcePackFilePath = filePath;
        this.packFormat = format;
    }
}