package fr.alessdevita.valtherion.rendering;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.*;

import java.util.*;

/**
 * Classe pour gérer un système de particules dans Valtherion.
 * Permet de créer des effets comme le feu, la fumée, la pluie, etc.
 */
public class ParticleSystem implements Renderable {

    // Liste des particules
    private final List<Particle> particles;

    // Nombre maximum de particules
    private int maxParticles;

    // Taux d'émission de particules (particules par seconde)
    private float emissionRate;

    // Temps écoulé depuis la dernière émission
    private float emissionTime;

    // Position de l'émetteur
    private Vector3f emitterPosition;

    // Vitesse de l'émetteur
    private Vector3f emitterVelocity;

    // Direction de l'émetteur
    private Vector3f emitterDirection;

    // Étendue de l'émetteur (pour une émission aléatoire)
    private Vector3f emitterSpread;

    // Durée de vie des particules (en secondes)
    private float particleLifetime;

    // Vitesse des particules
    private float particleSpeed;

    // Taille des particules
    private float particleSize;

    // Couleur des particules
    private Vector4f particleColor;

    // Texture des particules
    private Texture particleTexture;

    // Shader des particules
    private Shader particleShader;

    // VAO et VBO pour le rendu des particules
    private int vaoId;
    private int vboId;

    // Matrice de modèle du système de particules
    private Matrix4f modelMatrix;

    // Indique si le système de particules est actif
    private boolean isActive;

    // Indique si le système de particules est visible
    private boolean isVisible;

    // Type de mélange des particules
    private BlendMode blendMode;

    // Énumération des types de mélange
    public enum BlendMode {
        ALPHA,       // Mélange alpha standard
        ADDITIVE,    // Mélange additif (pour les effets lumineux)
        MULTIPLY     // Mélange multiplicatif
    }

    // Classe interne pour les particules
    private static class Particle {
        Vector3f position;
        Vector3f velocity;
        float lifetime;
        float size;
        Vector4f color;

        Particle(Vector3f position, Vector3f velocity, float lifetime, float size, Vector4f color) {
            this.position = position;
            this.velocity = velocity;
            this.lifetime = lifetime;
            this.size = size;
            this.color = color;
        }
    }

    /**
     * Constructeur du système de particules.
     * @param maxParticles Nombre maximum de particules.
     * @param emissionRate Taux d'émission de particules.
     * @param particleLifetime Durée de vie des particules.
     */
    public ParticleSystem(int maxParticles, float emissionRate, float particleLifetime) {
        this.particles = new ArrayList<>();
        this.maxParticles = maxParticles;
        this.emissionRate = emissionRate;
        this.particleLifetime = particleLifetime;
        this.emitterPosition = new Vector3f(0.0f, 0.0f, 0.0f);
        this.emitterVelocity = new Vector3f(0.0f, 0.0f, 0.0f);
        this.emitterDirection = new Vector3f(0.0f, 1.0f, 0.0f);
        this.emitterSpread = new Vector3f(1.0f, 1.0f, 1.0f);
        this.particleSpeed = 1.0f;
        this.particleSize = 0.1f;
        this.particleColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.modelMatrix = new Matrix4f();
        this.isActive = true;
        this.isVisible = true;
        this.blendMode = BlendMode.ALPHA;

        try {
            // Charger le shader des particules
            this.particleShader = new Shader(
                    "shaders/particle_vertex.glsl",
                    "shaders/particle_fragment.glsl"
            );

            // Créer le VAO et le VBO pour les particules
            createParticleMesh();
        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer le système de particules : " + e.getMessage(), e);
        }
    }

    /**
     * Crée le maillage pour les particules (un quad 2D).
     */
    private void createParticleMesh() {
        // Vertices d'un quad 2D (pour les particules)
        float[] vertices = {
                -0.5f, -0.5f, 0.0f,  0.0f, 0.0f,
                0.5f, -0.5f, 0.0f,  1.0f, 0.0f,
                0.5f,  0.5f, 0.0f,  1.0f, 1.0f,
                -0.5f,  0.5f, 0.0f,  0.0f, 1.0f
        };

        // Indices pour les triangles
        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        // Créer le VAO
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Créer le VBO
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        // Créer l'EBO
        int eboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        // Définir les attributs des vertices
        // Position (location = 0)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        // Coordonnées de texture (location = 1)
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        // Délier le VAO
        GL30.glBindVertexArray(0);
    }

    /**
     * Émet de nouvelles particules.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void emit(float deltaTime) {
        if (!isActive) {
            return;
        }

        emissionTime += deltaTime;
        float particlesToEmit = emissionRate * deltaTime;

        for (int i = 0; i < particlesToEmit && particles.size() < maxParticles; i++) {
            // Calculer une position aléatoire autour de l'émetteur
            Vector3f position = new Vector3f(
                    emitterPosition.x + (float) (Math.random() * 2 - 1) * emitterSpread.x,
                    emitterPosition.y + (float) (Math.random() * 2 - 1) * emitterSpread.y,
                    emitterPosition.z + (float) (Math.random() * 2 - 1) * emitterSpread.z
            );

            // Calculer une vitesse aléatoire
            Vector3f velocity = new Vector3f(
                    emitterDirection.x * particleSpeed + (float) (Math.random() * 2 - 1) * 0.1f,
                    emitterDirection.y * particleSpeed + (float) (Math.random() * 2 - 1) * 0.1f,
                    emitterDirection.z * particleSpeed + (float) (Math.random() * 2 - 1) * 0.1f
            );

            // Créer une nouvelle particule
            Particle particle = new Particle(
                    position,
                    velocity,
                    particleLifetime,
                    particleSize * (0.5f + (float) Math.random() * 0.5f), // Taille aléatoire
                    particleColor
            );

            particles.add(particle);
        }
    }

    /**
     * Met à jour les particules.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void updateParticles(float deltaTime) {
        if (!isActive) {
            return;
        }

        // Émettre de nouvelles particules
        emit(deltaTime);

        // Mettre à jour les particules existantes
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle particle = particles.get(i);
            particle.lifetime -= deltaTime;

            if (particle.lifetime <= 0) {
                particles.remove(i);
                continue;
            }

            // Mettre à jour la position de la particule
            particle.position.add(particle.velocity.mul(deltaTime, new Vector3f()));

            // Appliquer la gravité (optionnel)
            particle.velocity.y -= 9.8f * deltaTime * 0.1f; // Gravité réduite pour les particules
        }
    }

    @Override
    public void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
        if (!isVisible || particles.isEmpty()) {
            return;
        }

        // Utiliser le shader des particules
        particleShader.bind();

        // Définir les matrices dans le shader
        particleShader.setUniform("projection", projectionMatrix);
        particleShader.setUniform("view", viewMatrix);
        particleShader.setUniform("model", this.modelMatrix.mul(modelMatrix, new Matrix4f()));

        // Définir le type de mélange
        switch (blendMode) {
            case ALPHA:
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                break;
            case ADDITIVE:
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                break;
            case MULTIPLY:
                GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);
                break;
        }

        // Lier la texture des particules
        if (particleTexture != null) {
            particleTexture.bind(GL13.GL_TEXTURE0);
            particleShader.setUniform("particleTexture", 0);
        }

        // Lier le VAO
        GL30.glBindVertexArray(vaoId);

        // Dessiner chaque particule
        for (Particle particle : particles) {
            // Définir la position, la taille et la couleur de la particule dans le shader
            particleShader.setUniform("particlePosition", particle.position);
            particleShader.setUniform("particleSize", particle.size);
            particleShader.setUniform("particleColor", particle.color);

            // Dessiner le quad de la particule
            GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    6,
                    GL11.GL_UNSIGNED_INT,
                    0
            );
        }

        // Délier le VAO
        GL30.glBindVertexArray(0);

        // Désactiver le shader
        particleShader.unbind();
    }

    @Override
    public void render(Shader shader, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        Renderable.super.render(shader, projectionMatrix, viewMatrix);
    }

    @Override
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    @Override
    public void setModelMatrix(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public void render(Camera camera) {

        // Le rendu est géré par la méthode render(Shader, Matrix4f, Matrix4f, Matrix4f)
    }

    // --- Getters et Setters ---

    public List<Particle> getParticles() {
        return particles;
    }

    public int getMaxParticles() {
        return maxParticles;
    }

    public void setMaxParticles(int maxParticles) {
        this.maxParticles = Math.max(1, maxParticles);
    }

    public float getEmissionRate() {
        return emissionRate;
    }

    public void setEmissionRate(float emissionRate) {
        this.emissionRate = Math.max(0.0f, emissionRate);
    }

    public Vector3f getEmitterPosition() {
        return emitterPosition;
    }

    public void setEmitterPosition(Vector3f emitterPosition) {
        this.emitterPosition = emitterPosition;
    }

    public Vector3f getEmitterVelocity() {
        return emitterVelocity;
    }

    public void setEmitterVelocity(Vector3f emitterVelocity) {
        this.emitterVelocity = emitterVelocity;
    }

    public Vector3f getEmitterDirection() {
        return emitterDirection;
    }

    public void setEmitterDirection(Vector3f emitterDirection) {
        this.emitterDirection = emitterDirection.normalize();
    }

    public Vector3f getEmitterSpread() {
        return emitterSpread;
    }

    public void setEmitterSpread(Vector3f emitterSpread) {
        this.emitterSpread = emitterSpread;
    }

    public float getParticleLifetime() {
        return particleLifetime;
    }

    public void setParticleLifetime(float particleLifetime) {
        this.particleLifetime = Math.max(0.0f, particleLifetime);
    }

    public float getParticleSpeed() {
        return particleSpeed;
    }

    public void setParticleSpeed(float particleSpeed) {
        this.particleSpeed = Math.max(0.0f, particleSpeed);
    }

    public float getParticleSize() {
        return particleSize;
    }

    public void setParticleSize(float particleSize) {
        this.particleSize = Math.max(0.0f, particleSize);
    }

    public Vector4f getParticleColor() {
        return particleColor;
    }

    public void setParticleColor(Vector4f particleColor) {
        this.particleColor = particleColor;
    }

    public Texture getParticleTexture() {
        return particleTexture;
    }

    public void setParticleTexture(Texture particleTexture) {
        this.particleTexture = particleTexture;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public BlendMode getBlendMode() {
        return blendMode;
    }

    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    /**
     * Met à jour le système de particules.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    @Override
    public void update(float deltaTime) {
        updateParticles(deltaTime);
    }

    /**
     * Libère les ressources du système de particules.
     */
    public void cleanup() {
        GL30.glDeleteVertexArrays(vaoId);
        GL15.glDeleteBuffers(vboId);
        if (particleShader != null) {
            particleShader.cleanup();
        }
        particles.clear();
    }

    /**
     * Réinitialise le système de particules.
     */
    public void reset() {
        particles.clear();
        emissionTime = 0.0f;
    }

    /**
     * Retourne une représentation textuelle du système de particules.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ParticleSystem{" +
                "particles=" + particles.size() +
                ", maxParticles=" + maxParticles +
                ", emissionRate=" + emissionRate +
                ", isActive=" + isActive +
                '}';
    }
}