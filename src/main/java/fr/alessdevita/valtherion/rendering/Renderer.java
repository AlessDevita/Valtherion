package fr.alessdevita.valtherion.rendering;

import fr.alessdevita.valtherion.gui.GUI;
import fr.alessdevita.valtherion.world.World;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

import java.util.*;

/**
 * Classe principale pour le rendu dans Valtherion.
 * Gère l'initialisation d'OpenGL, le rendu des objets, et les passes de rendu.
 */
public class Renderer {

    // Largeur de la fenêtre
    private int width;

    // Hauteur de la fenêtre
    private int height;

    // Couleur de fond
    private Vector3f clearColor;

    // Liste des objets rendables
    private final List<Renderable> renderables;

    // Liste des lumières
    private final List<Light> lights;

    // Caméra active
    private Camera camera;

    // Shader par défaut
    private Shader defaultShader;

    // Skybox
    private Skybox skybox;

    // Indique si le rendu est initialisé
    private boolean isInitialized;

    // Indique si le rendu est en mode wireframe
    private boolean wireframeMode;

    // Indique si le rendu est en mode culling
    private boolean cullingMode;

    /**
     * Constructeur du rendu.
     */
    public Renderer() {
        this.width = 800;
        this.height = 600;
        this.clearColor = new Vector3f(0.1f, 0.1f, 0.1f);
        this.renderables = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.isInitialized = false;
        this.wireframeMode = false;
        this.cullingMode = true;
    }

    /**
     * Initialise le rendu.
     * @throws Exception Si une erreur se produit lors de l'initialisation.
     */
    public void init() throws Exception {
        if (isInitialized) {
            return;
        }

        // Initialiser OpenGL
        GL.createCapabilities();

        // Activer le depth test
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LESS);

        // Activer le blending (pour la transparence)
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Activer le culling (pour ne pas rendre les faces arrière)
        if (cullingMode) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glCullFace(GL11.GL_BACK);
        }

        // Charger le shader par défaut
        this.defaultShader = new Shader(
                "shaders/vertex.glsl",
                "shaders/fragment.glsl"
        );

        // Initialiser le skybox
        this.skybox = new Skybox("textures/skybox/");

        isInitialized = true;
    }

    /**
     * Rend la scène.
     */
    public void render() {

    }

    /**
     * Ajoute un objet rendable.
     * @param renderable Objet rendable à ajouter.
     */
    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
    }

    /**
     * Ajoute une lumière.
     * @param light Lumière à ajouter.
     */
    public void addLight(Light light) {
        lights.add(light);
    }

    /**
     * Retire une lumière.
     * @param light Lumière à retirer.
     */
    public void removeLight(Light light) {
        lights.remove(light);
    }

    /**
     * Définit la caméra active.
     * @param camera Caméra à utiliser.
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
        if (camera != null) {
            camera.setAspectRatio((float) width / height);
        }
    }

    /**
     * Redimensionne la fenêtre.
     * @param width Nouvelle largeur.
     * @param height Nouvelle hauteur.
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        // Mettre à jour le viewport
        GL11.glViewport(0, 0, width, height);

        // Mettre à jour le ratio d'aspect de la caméra
        if (camera != null) {
            camera.setAspectRatio((float) width / height);
        }
    }

    /**
     * Définit la couleur de fond.
     * @param clearColor Couleur de fond.
     */
    public void setClearColor(Vector3f clearColor) {
        this.clearColor = clearColor;
    }

    /**
     * Active ou désactive le mode wireframe.
     * @param wireframeMode true pour activer le mode wireframe.
     */
    public void setWireframeMode(boolean wireframeMode) {
        this.wireframeMode = wireframeMode;
    }

    /**
     * Active ou désactive le mode culling.
     * @param cullingMode true pour activer le mode culling.
     */
    public void setCullingMode(boolean cullingMode) {
        this.cullingMode = cullingMode;
        if (cullingMode) {
            GL11.glEnable(GL11.GL_CULL_FACE);
        } else {
            GL11.glDisable(GL11.GL_CULL_FACE);
        }
    }

    /**
     * Définit le shader par défaut.
     * @param shader Shader à utiliser.
     */
    public void setDefaultShader(Shader shader) {
        this.defaultShader = shader;
    }

    /**
     * Définit le skybox.
     * @param skybox Skybox à utiliser.
     */
    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    /**
     * Libère les ressources du rendu.
     */
    public void cleanup() {
        if (defaultShader != null) {
            defaultShader.cleanup();
        }
        if (skybox != null) {
            skybox.cleanup();
        }
        for (Renderable renderable : renderables) {
            if (renderable instanceof Mesh) {
                ((Mesh) renderable).cleanup();
            }
        }
        renderables.clear();
        lights.clear();
        isInitialized = false;
    }

    // --- Getters ---

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector3f getClearColor() {
        return clearColor;
    }

    public List<Renderable> getRenderables() {
        return renderables;
    }

    public List<Light> getLights() {
        return lights;
    }

    public Camera getCamera() {
        return camera;
    }

    public Shader getDefaultShader() {
        return defaultShader;
    }

    public Skybox getSkybox() {
        return skybox;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public boolean isWireframeMode() {
        return wireframeMode;
    }

    public boolean isCullingMode() {
        return cullingMode;
    }

    /**
     * Retourne une représentation textuelle du rendu.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Renderer{" +
                "width=" + width +
                ", height=" + height +
                ", renderables=" + renderables.size() +
                ", lights=" + lights.size() +
                ", isInitialized=" + isInitialized +
                '}';
    }

    public void render(World world, GUI currentScreen) {
    }
}