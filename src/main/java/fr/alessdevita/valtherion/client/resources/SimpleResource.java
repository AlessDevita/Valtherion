package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.resources.data.IMetadataSection;
import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implémentation par défaut d'une {@link IResource} : wrappe un
 * {@link IResourcePack} et un {@link ResourceLocation}.
 */
public final class SimpleResource implements IResource {
    private final ResourceLocation resourceLocation;
    private final IResourcePack resourcePack;

    public SimpleResource(ResourceLocation resourceLocation, IResourcePack resourcePack) {
        this.resourceLocation = resourceLocation;
        this.resourcePack = resourcePack;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    @Override
    public InputStream getInputStream() {
        if (this.resourcePack == null) return null;
        try {
            return this.resourcePack.getInputStream(this.resourceLocation);
        } catch (IOException e) {
            throw new ResourcePackFileNotFoundException(
                    this.resourceLocation == null ? "?" : this.resourceLocation.toString());
        }
    }

    @Override
    public String getPackName() {
        return this.resourcePack == null ? "unknown" : this.resourcePack.getPackName();
    }

    @Override
    public <T extends IMetadataSection> T getMetadata(Class<T> sectionClass) {
        if (this.resourcePack == null) return null;
        try {
            return this.resourcePack.getPackMetadata(sectionClass);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.resourceLocation + " (from " + getPackName() + ")";
    }
}
