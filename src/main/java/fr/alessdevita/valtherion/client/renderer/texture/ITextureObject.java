package fr.alessdevita.valtherion.client.renderer.texture;

import java.io.IOException;

/** Contrat d'une texture chargeable par le gestionnaire graphique. */
public interface ITextureObject {
    int getGlTextureId();
    void loadTexture() throws IOException;
    default boolean isLoaded() { return getGlTextureId() != 0; }
    default void deleteGlTexture() { }
}
