package fr.alessdevita.valtherion.rendering.utils;

import fr.alessdevita.valtherion.rendering.*;

import java.io.*;
import java.util.*;

/**
 * Gestionnaire de ressources pour Valtherion.
 * Charge et stocke les ressources (textures, shaders, modèles) pour éviter les doublons.
 */
public class ResourceManager {

    // Instance unique pour le singleton
    private static ResourceManager instance;

    // Map des textures chargées (chemin -> Texture)
    private final Map<String, Texture> textures;

    // Map des shaders chargés (nom -> Shader)
    private final Map<String, Shader> shaders;

    // Map des modèles chargés (chemin -> Model)
    private final Map<String, Model> models;

    // Chemin de base pour les ressources
    private String basePath;

    /**
     * Constructeur privé pour le singleton.
     */
    private ResourceManager() {
        this.textures = new HashMap<>();
        this.shaders = new HashMap<>();
        this.models = new HashMap<>();
        this.basePath = "resources/";
    }

    /**
     * Récupère l'instance unique de ResourceManager.
     * @return Instance unique.
     */
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    // --- Méthodes pour les textures ---

    /**
     * Charge une texture.
     * @param path Chemin de la texture (relatif au basePath).
     * @param type Type de la texture.
     * @return Texture chargée.
     */
    public Texture loadTexture(String path, Texture.TextureType type) {
        String fullPath = basePath + path;

        // Vérifier si la texture est déjà chargée
        if (textures.containsKey(fullPath)) {
            return textures.get(fullPath);
        }

        // Charger la texture
        try {
            Texture texture = new Texture(fullPath);
            textures.put(fullPath, texture);
            return texture;
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la texture : " + fullPath);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère une texture.
     * @param path Chemin de la texture.
     * @return Texture ou null si non trouvée.
     */
    public Texture getTexture(String path) {
        String fullPath = basePath + path;
        return textures.get(fullPath);
    }

    /**
     * Supprime une texture.
     * @param path Chemin de la texture.
     */
    public void unloadTexture(String path) {
        String fullPath = basePath + path;
        Texture texture = textures.remove(fullPath);
        if (texture != null) {
            texture.cleanup();
        }
    }

    // --- Méthodes pour les shaders ---

    /**
     * Charge un shader.
     * @param name Nom du shader.
     * @param vertexPath Chemin du fichier vertex.
     * @param fragmentPath Chemin du fichier fragment.
     * @return Shader chargé.
     */
    public Shader loadShader(String name, String vertexPath, String fragmentPath) {
        String fullVertexPath = basePath + vertexPath;
        String fullFragmentPath = basePath + fragmentPath;

        // Vérifier si le shader est déjà chargé
        if (shaders.containsKey(name)) {
            return shaders.get(name);
        }

        // Charger le shader
        try {
            Shader shader = new Shader(fullVertexPath, fullFragmentPath);
            shaders.put(name, shader);
            return shader;
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du shader : " + name);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère un shader.
     * @param name Nom du shader.
     * @return Shader ou null si non trouvé.
     */
    public Shader getShader(String name) {
        return shaders.get(name);
    }

    /**
     * Supprime un shader.
     * @param name Nom du shader.
     */
    public void unloadShader(String name) {
        Shader shader = shaders.remove(name);
        if (shader != null) {
            shader.cleanup();
        }
    }

    // --- Méthodes pour les modèles ---

    /**
     * Charge un modèle.
     * @param path Chemin du modèle.
     * @return Modèle chargé.
     */
    public Model loadModel(String path) {
        String fullPath = basePath + path;

        // Vérifier si le modèle est déjà chargé
        if (models.containsKey(fullPath)) {
            return models.get(fullPath);
        }

        // Charger le modèle (à implémenter selon ton format de modèle)
        Model model = new Model(path);
        models.put(fullPath, model);
        return model;
    }

    /**
     * Récupère un modèle.
     * @param path Chemin du modèle.
     * @return Modèle ou null si non trouvé.
     */
    public Model getModel(String path) {
        String fullPath = basePath + path;
        return models.get(fullPath);
    }

    /**
     * Supprime un modèle.
     * @param path Chemin du modèle.
     */
    public void unloadModel(String path) {
        String fullPath = basePath + path;
        Model model = models.remove(fullPath);
        if (model != null) {
            model.cleanup();
        }
    }

    // --- Méthodes générales ---

    /**
     * Définit le chemin de base pour les ressources.
     * @param basePath Chemin de base.
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Supprime toutes les ressources.
     */
    public void clear() {
        for (Texture texture : textures.values()) {
            texture.cleanup();
        }
        textures.clear();

        for (Shader shader : shaders.values()) {
            shader.cleanup();
        }
        shaders.clear();

        for (Model model : models.values()) {
            model.cleanup();
        }
        models.clear();
    }

    // --- Getters ---

    public Map<String, Texture> getTextures() {
        return textures;
    }

    public Map<String, Shader> getShaders() {
        return shaders;
    }

    public Map<String, Model> getModels() {
        return models;
    }

    public String getBasePath() {
        return basePath;
    }

    /**
     * Retourne une représentation textuelle du gestionnaire de ressources.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ResourceManager{" +
                "textures=" + textures.size() +
                ", shaders=" + shaders.size() +
                ", models=" + models.size() +
                '}';
    }
}