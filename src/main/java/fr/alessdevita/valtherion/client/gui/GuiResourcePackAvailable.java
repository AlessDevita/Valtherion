package fr.alessdevita.valtherion.client.gui;

/**
 * GuiResourcePackAvailable — une entree dans la liste des packs
 * DISPONIBLES (colonne gauche de l'ecran de resource packs).
 */
public class GuiResourcePackAvailable
{
    /** Nom du pack. */
    public final String resourcePackName;
    /** Description (2 lignes max). */
    public final String description;
    /** Chemin du dossier/zip du pack. */
    public final String resourcePackFilePath;
    /** Format du pack (version). */
    public final int packFormat;

    public GuiResourcePackAvailable(String name, String desc, String filePath, int format)
    {
        this.resourcePackName = name;
        this.description = desc;
        this.resourcePackFilePath = filePath;
        this.packFormat = format;
    }
}