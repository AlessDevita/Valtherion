package fr.alessdevita.valtherion.utils;

/**
 * Identifie une ressource par son namespace et son chemin.
 * Format canonique : {@code namespace:path}.
 */
public final class ResourceLocation {
    private final String namespace;
    private final String path;

    public ResourceLocation(String resourceName) {
        if (resourceName == null || resourceName.isEmpty()) {
            this.namespace = "valtherion";
            this.path = "";
            return;
        }
        int colon = resourceName.indexOf(':');
        if (colon >= 0) {
            this.namespace = resourceName.substring(0, colon);
            this.path = resourceName.substring(colon + 1);
        } else {
            this.namespace = "valtherion";
            this.path = resourceName;
        }
    }

    public ResourceLocation(String namespace, String path) {
        this.namespace = (namespace == null || namespace.isEmpty()) ? "valtherion" : namespace;
        this.path = path == null ? "" : path;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return this.namespace + ':' + this.path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceLocation other)) return false;
        return this.namespace.equals(other.namespace) && this.path.equals(other.path);
    }

    @Override
    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }
}
