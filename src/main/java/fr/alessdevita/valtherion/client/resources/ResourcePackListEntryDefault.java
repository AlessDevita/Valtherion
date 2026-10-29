package fr.alessdevita.valtherion.client.resources;

/**
 * Entrée de pack par défaut (builtin) dans la liste des packs.
 */
public final class ResourcePackListEntryDefault extends ResourcePackListEntry {
    public ResourcePackListEntryDefault(ResourcePackRepository repository,
                                        DefaultResourcePack resourcePack) {
        super(repository, resourcePack);
    }

    @Override
    public String getPackName() {
        return "Default";
    }
}
