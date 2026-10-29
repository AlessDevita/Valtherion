package fr.alessdevita.valtherion.client.resources.data;

/**
 * Métadonnées décrivant un pack de ressources (description, format).
 */
public final class PackMetadataSection implements IMetadataSection {
    public static final String SECTION_NAME = "pack";

    private final String description;
    private final int packFormat;

    public PackMetadataSection(String description, int packFormat) {
        this.description = description;
        this.packFormat = packFormat;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPackFormat() {
        return this.packFormat;
    }
}
