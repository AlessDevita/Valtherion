package fr.alessdevita.valtherion.client.shader;

import fr.alessdevita.valtherion.client.resources.IResourceManager;
import fr.alessdevita.valtherion.utils.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestionnaire de shaders : compile, met en cache et fournit les
 * {@link Shader} du client. Charge les sources depuis un
 * {@link IResourceManager}.
 */
public final class ShaderManager {
    private final IResourceManager resourceManager;
    private final Map<String, Shader> shaderCache = new ConcurrentHashMap<>();

    public ShaderManager() {
        this(null);
    }

    public ShaderManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * Charge un shader depuis le gestionnaire de ressources.
     *
     * @param name     le nom logique du shader (ex: "color")
     * @param location la {@link ResourceLocation} de base (sans extension)
     * @return le {@link Shader} compilé et lié
     * @throws IOException si les sources ne peuvent être lues
     * @throws RuntimeException si la compilation ou le linkage échoue
     */
    public Shader loadShader(String name, ResourceLocation location) throws IOException {
        Shader cached = this.shaderCache.get(name);
        if (cached != null) return cached;

        if (this.resourceManager == null) {
            throw new IOException("Aucun ResourceManager configuré");
        }

        String vertexSource = readResource(this.resourceManager,
                new ResourceLocation(location.getNamespace(), location.getPath() + ".vsh"));
        String fragmentSource = readResource(this.resourceManager,
                new ResourceLocation(location.getNamespace(), location.getPath() + ".fsh"));

        int vertexId = ShaderLoader.loadVertexShader(vertexSource);
        int fragmentId = ShaderLoader.loadFragmentShader(fragmentSource);
        int programId = ShaderLinkHelper.linkProgram(vertexId, fragmentId);

        Shader shader = new Shader(name, programId, vertexId, fragmentId);
        this.shaderCache.put(name, shader);
        return shader;
    }

    /**
     * Crée un shader directement depuis des sources GLSL.
     *
     * @param name           le nom logique
     * @param vertexSource   le code source du vertex shader
     * @param fragmentSource le code source du fragment shader
     * @return le {@link Shader}
     */
    public Shader createShader(String name, String vertexSource, String fragmentSource) {
        Shader cached = this.shaderCache.get(name);
        if (cached != null) return cached;

        int vertexId = ShaderLoader.loadVertexShader(vertexSource);
        int fragmentId = ShaderLoader.loadFragmentShader(fragmentSource);
        int programId = ShaderLinkHelper.linkProgram(vertexId, fragmentId);

        Shader shader = new Shader(name, programId, vertexId, fragmentId);
        this.shaderCache.put(name, shader);
        return shader;
    }

    /** @return le shader en cache, ou {@code null}. */
    public Shader getShader(String name) {
        return this.shaderCache.get(name);
    }

    /** Active un shader par son nom. */
    public void useShader(String name) {
        Shader shader = this.shaderCache.get(name);
        if (shader != null) {
            shader.use();
        } else {
            GL20UseNone();
        }
    }

    /** Désactive tout shader actif. */
    public void endShader() {
        org.lwjgl.opengl.GL20.glUseProgram(0);
    }

    /** Supprime un shader du cache et libère ses ressources GL. */
    public void deleteShader(String name) {
        Shader shader = this.shaderCache.remove(name);
        if (shader != null) shader.delete();
    }

    /** Supprime tous les shaders en cache. */
    public void deleteShaders() {
        for (Shader shader : this.shaderCache.values()) {
            shader.delete();
        }
        this.shaderCache.clear();
    }

    private static void GL20UseNone() {
        org.lwjgl.opengl.GL20.glUseProgram(0);
    }

    private static String readResource(IResourceManager manager, ResourceLocation location) throws IOException {
        fr.alessdevita.valtherion.client.resources.IResource resource = manager.getResource(location);
        if (resource == null) {
            throw new IOException("Ressource shader introuvable: " + location);
        }
        try (InputStream stream = resource.getInputStream()) {
            if (stream == null) {
                throw new IOException("Flux null pour: " + location);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public IResourceManager getResourceManager() {
        return this.resourceManager;
    }
}
