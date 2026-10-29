package fr.alessdevita.valtherion.entity;

import fr.alessdevita.valtherion.rendering.Camera;
import fr.alessdevita.valtherion.rendering.Model;
import fr.alessdevita.valtherion.rendering.Shader;
import fr.alessdevita.valtherion.rendering.Texture;
import fr.alessdevita.valtherion.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EntityRenderer {

    // Monde dans lequel les entités existent
    private final World world;

    // Caméra pour le rendu
    private Camera camera;

    // Shader utilisé pour le rendu des entités
    private Shader entityShader;

    // Map des modèles des entités (type d'entité -> modèle)
    private final Map<String, Model> entityModels;

    // Map des textures des entités (type d'entité -> texture)
    private final Map<String, Texture> entityTextures;

    // Map des animations des entités (type d'entité -> animation)
    private final Map<String, EntityAnimation> entityAnimations;

    // Matrice de projection
    private Matrix4f projectionMatrix;

    // Éclairage ambiant
    private Vector3f ambientLight;

    // Direction de la lumière
    private Vector3f lightDirection;

    // Couleur de la lumière
    private Vector3f lightColor;

    /**
     * Constructeur du rendu des entités.
     * @param world Monde dans lequel les entités existent.
     * @param camera Caméra pour le rendu.
     */
    public EntityRenderer(World world, Camera camera) throws IOException {
        this.world = world;
        this.camera = camera;
        this.entityModels = new HashMap<>();
        this.entityTextures = new HashMap<>();
        this.entityAnimations = new HashMap<>();
        this.projectionMatrix = new Matrix4f();
        this.ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);
        this.lightDirection = new Vector3f(0.0f, -1.0f, 0.0f);
        this.lightColor = new Vector3f(1.0f, 1.0f, 1.0f);

        // Initialiser le shader par défaut pour les entités
        this.entityShader = new Shader("entity_vertex.glsl", "entity_fragment.glsl");
    }

    /**
     * Initialise le rendu des entités.
     */
    public void init() throws IOException {
        // Charger les modèles, textures et animations par défaut
        loadDefaultResources();
    }

    /**
     * Charge les ressources par défaut (modèles, textures, animations).
     */
    private void loadDefaultResources() throws IOException {
        // Exemple : Charger un modèle et une texture pour un joueur
        Model playerModel = new Model("player.obj");
        Texture playerTexture = new Texture("player.png");
        entityModels.put("PLAYER", playerModel);
        entityTextures.put("PLAYER", playerTexture);

        // Exemple : Charger un modèle et une texture pour un zombie
        Model zombieModel = new Model("zombie.obj");
        Texture zombieTexture = new Texture("zombie.png");
        entityModels.put("ZOMBIE", zombieModel);
        entityTextures.put("ZOMBIE", zombieTexture);

        // Ajouter des animations pour les entités
        entityAnimations.put("PLAYER", new EntityAnimation("player_idle", 10));
        entityAnimations.put("ZOMBIE", new EntityAnimation("zombie_walk", 15));
    }

    /**
     * Rend une entité.
     * @param entity Entité à rendre.
     */
    public void render(Entity entity) {
        if (entity == null || !entity.isActive()) {
            return;
        }

        // Récupérer le modèle et la texture de l'entité
        Model model = entityModels.get(entity.getType().name());
        Texture texture = entityTextures.get(entity.getType().name());

        if (model == null || texture == null) {
            // Utiliser un modèle et une texture par défaut si non trouvés
            model = entityModels.get("DEFAULT");
            texture = entityTextures.get("DEFAULT");
        }

        // Activer le shader
        entityShader.bind();

        // Configurer les matrices de vue et de projection
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f modelMatrix = createModelMatrix(entity);

        entityShader.setMatrix4("projection", projectionMatrix);
        entityShader.setMatrix4("view", viewMatrix);
        entityShader.setMatrix4("model", modelMatrix);

        // Configurer l'éclairage
        entityShader.setVector3f("ambientLight", ambientLight);
        entityShader.setVector3f("lightDirection", lightDirection);
        entityShader.setVector3f("lightColor", lightColor);

        // Configurer la texture
        texture.bind();

        // Rendre le modèle
        model.render();

        // Désactiver la texture et le shader
        texture.unbind();
        entityShader.unbind();
    }

    /**
     * Rend toutes les entités du monde.
     */
    public void renderAll() {
        if (world == null) {
            return;
        }

        // Rendre toutes les entités du monde
        for (Entity entity : world.getEntities()) {
            render(entity);
        }
    }

    /**
     * Crée la matrice de modèle pour une entité.
     * @param entity Entité.
     * @return Matrice de modèle.
     */
    private Matrix4f createModelMatrix(Entity entity) {
        Matrix4f modelMatrix = new Matrix4f();

        // Appliquer la translation
        modelMatrix.translate(
                (float) entity.getX(),
                (float) entity.getY(),
                (float) entity.getZ()
        );

        // Appliquer la rotation (si nécessaire)
        // Exemple : modelMatrix.rotate((float) Math.toRadians(entity.getRotationY()), 0, 1, 0);

        // Appliquer la mise à l'échelle (si nécessaire)
        // Exemple : modelMatrix.scale(1.0f, 1.0f, 1.0f);

        return modelMatrix;
    }

    /**
     * Définit la matrice de projection.
     * @param projectionMatrix Matrice de projection.
     */
    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    /**
     * Définit la caméra.
     * @param camera Caméra.
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Ajoute un modèle pour un type d'entité.
     * @param entityType Type d'entité.
     * @param model Modèle à ajouter.
     */
    public void addModel(String entityType, Model model) {
        entityModels.put(entityType, model);
    }

    /**
     * Ajoute une texture pour un type d'entité.
     * @param entityType Type d'entité.
     * @param texture Texture à ajouter.
     */
    public void addTexture(String entityType, Texture texture) {
        entityTextures.put(entityType, texture);
    }

    /**
     * Ajoute une animation pour un type d'entité.
     * @param entityType Type d'entité.
     * @param animation Animation à ajouter.
     */
    public void addAnimation(String entityType, EntityAnimation animation) {
        entityAnimations.put(entityType, animation);
    }

    /**
     * Définit l'éclairage ambiant.
     * @param ambientLight Éclairage ambiant.
     */
    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    /**
     * Définit la direction de la lumière.
     * @param lightDirection Direction de la lumière.
     */
    public void setLightDirection(Vector3f lightDirection) {
        this.lightDirection = lightDirection;
    }

    /**
     * Définit la couleur de la lumière.
     * @param lightColor Couleur de la lumière.
     */
    public void setLightColor(Vector3f lightColor) {
        this.lightColor = lightColor;
    }

    /**
     * Nettoie les ressources du rendu.
     */
    public void cleanup() {
        // Nettoyer les modèles, textures et animations
        entityModels.values().forEach(Model::cleanup);
        entityTextures.values().forEach(Texture::cleanup);
        entityShader.cleanup();
    }

    // --- Classe interne pour les animations des entités ---

    /**
     * Classe pour les animations des entités.
     */
    public static class EntityAnimation {
        private final String name;
        private final int frameCount;
        private int currentFrame;
        private int frameDelay;
        private int currentDelay;

        public EntityAnimation(String name, int frameDelay) {
            this.name = name;
            this.frameCount = 1; // Par défaut, une seule frame
            this.currentFrame = 0;
            this.frameDelay = frameDelay;
            this.currentDelay = 0;
        }

        public EntityAnimation(String name, int frameCount, int frameDelay) {
            this.name = name;
            this.frameCount = frameCount;
            this.currentFrame = 0;
            this.frameDelay = frameDelay;
            this.currentDelay = 0;
        }

        /**
         * Met à jour l'animation.
         */
        public void update() {
            currentDelay++;
            if (currentDelay >= frameDelay) {
                currentFrame = (currentFrame + 1) % frameCount;
                currentDelay = 0;
            }
        }

        public String getName() {
            return name;
        }

        public int getCurrentFrame() {
            return currentFrame;
        }

        public int getFrameCount() {
            return frameCount;
        }
    }
}
