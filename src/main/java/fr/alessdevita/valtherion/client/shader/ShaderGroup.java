package fr.alessdevita.valtherion.client.shader;

import java.util.ArrayList;
import java.util.List;

/**
 * Chaîne de passes de post-processing.
 * <p>
 * Un {@link ShaderGroup} applique successivement plusieurs shaders
 * sur des {@link Framebuffer} intermédiaires (ping-pong).
 */
public final class ShaderGroup {
    private final List<ShaderPass> passes = new ArrayList<>();
    private Framebuffer inputFramebuffer;
    private Framebuffer tempFramebuffer;
    private final int width;
    private final int height;

    /**
     * Construit un groupe de shaders de la taille donnée.
     *
     * @param width  largeur des framebuffers intermédiaires
     * @param height hauteur des framebuffers intermédiaires
     */
    public ShaderGroup(int width, int height) {
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
    }

    /**
     * Ajoute une passe de shader à la chaîne.
     *
     * @param shader le shader à appliquer
     */
    public void addPass(Shader shader) {
        if (shader != null) {
            this.passes.add(new ShaderPass(shader));
        }
    }

    /**
     * Ajoute une passe de shader avec un nom d'uniform de texture.
     *
     * @param shader       le shader à appliquer
     * @param textureUniform le nom de l'uniform sampler2D
     */
    public void addPass(Shader shader, String textureUniform) {
        if (shader != null) {
            this.passes.add(new ShaderPass(shader, textureUniform));
        }
    }

    /**
     * Initialise les framebuffers intermédiaires.
     */
    public void createFramebuffers() {
        this.inputFramebuffer = new Framebuffer(this.width, this.height);
        this.tempFramebuffer = new Framebuffer(this.width, this.height);
    }

    /**
     * Redimensionne les framebuffers.
     *
     * @param width  nouvelle largeur
     * @param height nouvelle hauteur
     */
    public void resize(int width, int height) {
        if (this.inputFramebuffer != null) {
            this.inputFramebuffer.resize(width, height);
        }
        if (this.tempFramebuffer != null) {
            this.tempFramebuffer.resize(width, height);
        }
    }

    /**
     * Applique la chaîne de passes : le framebuffer d'entrée est rendu
     * successivement dans le framebuffer temporaire (et inversement)
     * pour chaque passe.
     *
     * @param source le framebuffer source à traiter
     * @param target le framebuffer de destination final
     */
    public void render(Framebuffer source, Framebuffer target) {
        if (this.passes.isEmpty() || source == null) {
            if (target != null) {
                target.bindFramebuffer(false);
                source.framebufferRender(this.width, this.height);
                target.unbindFramebuffer();
            }
            return;
        }

        if (this.tempFramebuffer == null) {
            createFramebuffers();
        }

        Framebuffer read = source;
        Framebuffer write = this.tempFramebuffer;

        for (int i = 0; i < this.passes.size(); i++) {
            ShaderPass pass = this.passes.get(i);
            write.bindFramebuffer(false);
            write.framebufferClear();

            pass.shader.use();
            // Lier la texture du framebuffer de lecture
            org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D,
                    read.getColorTextureId());
            if (pass.textureUniform != null) {
                ShaderUniform uniform = pass.shader.getUniform(pass.textureUniform);
                uniform.set(0);
            }

            read.framebufferRender(this.width, this.height);
            pass.shader.end();

            // Swap read/write pour la prochaine passe
            Framebuffer swap = read;
            read = write;
            write = (i + 1 < this.passes.size()) ? swap : target;
        }

        // Rendu final vers le target
        if (target != null) {
            target.bindFramebuffer(false);
            org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D,
                    read.getColorTextureId());
            read.framebufferRender(this.width, this.height);
            target.unbindFramebuffer();
        }
    }

    /** @return la liste des passes. */
    public List<ShaderPass> getPasses() {
        return java.util.Collections.unmodifiableList(this.passes);
    }

    /** Supprime les framebuffers intermédiaires. */
    public void delete() {
        if (this.inputFramebuffer != null) {
            this.inputFramebuffer.deleteFramebuffer();
            this.inputFramebuffer = null;
        }
        if (this.tempFramebuffer != null) {
            this.tempFramebuffer.deleteFramebuffer();
            this.tempFramebuffer = null;
        }
    }

    /**
     * Une passe de shader dans la chaîne de post-processing.
     */
    public static final class ShaderPass {
        final Shader shader;
        final String textureUniform;

        public ShaderPass(Shader shader) {
            this(shader, "texture");
        }

        public ShaderPass(Shader shader, String textureUniform) {
            this.shader = shader;
            this.textureUniform = textureUniform;
        }

        public Shader getShader() {
            return this.shader;
        }

        public String getTextureUniform() {
            return this.textureUniform;
        }
    }
}
