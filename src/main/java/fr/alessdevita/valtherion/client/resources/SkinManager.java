package fr.alessdevita.valtherion.client.resources;

import fr.alessdevita.valtherion.client.renderer.texture.TextureManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Gestionnaire de skins : charge et met en cache les skins des joueurs
 * depuis le disque ou depuis une source réseau (à implémenter).
 */
public final class SkinManager {
    private final TextureManager textureManager;
    private final File skinsDir;
    private final Map<String, String> skinCache = new ConcurrentHashMap<>();
    private final ExecutorService downloadExecutor = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r, "SkinDownloader");
        thread.setDaemon(true);
        return thread;
    });

    public SkinManager() {
        this(null, null);
    }

    public SkinManager(TextureManager textureManager, File skinsDir) {
        this.textureManager = textureManager;
        this.skinsDir = skinsDir;
        if (skinsDir != null && !skinsDir.exists()) {
            skinsDir.mkdirs();
        }
    }

    /**
     * Charge un skin depuis le disque.
     *
     * @param playerName le nom du joueur
     * @return la clé de texture, ou {@code null} si introuvable
     */
    public String loadSkin(String playerName) {
        if (playerName == null) return null;
        String cached = this.skinCache.get(playerName);
        if (cached != null) return cached;

        if (this.skinsDir == null) return null;
        File skinFile = new File(this.skinsDir, playerName + ".png");
        if (!skinFile.isFile()) return null;

        String textureKey = "skins/" + playerName;
        this.skinCache.put(playerName, textureKey);
        return textureKey;
    }

    /**
     * Enregistre un skin depuis un flux et le lie dans le TextureManager.
     *
     * @param playerName le nom du joueur
     * @param stream le flux de l'image PNG
     * @param callback appelé avec la clé de texture une fois chargé
     */
    public void loadSkin(String playerName, InputStream stream, Consumer<String> callback) {
        if (playerName == null || stream == null) return;
        this.downloadExecutor.submit(() -> {
            try {
                BufferedImage image = javax.imageio.ImageIO.read(stream);
                if (image == null) {
                    if (callback != null) callback.accept(null);
                    return;
                }
                String textureKey = "skins/" + playerName;
                if (this.textureManager != null) {
                    this.textureManager.getDynamicTextureLocation(playerName, image);
                }
                this.skinCache.put(playerName, textureKey);
                if (callback != null) callback.accept(textureKey);
            } catch (IOException e) {
                if (callback != null) callback.accept(null);
            }
        });
    }

    /**
     * Sauvegarde un skin sur le disque.
     *
     * @param playerName le nom du joueur
     * @param data les données PNG
     */
    public void saveSkin(String playerName, byte[] data) {
        if (playerName == null || data == null || this.skinsDir == null) return;
        File skinFile = new File(this.skinsDir, playerName + ".png");
        try {
            Files.write(skinFile.toPath(), data);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du skin " + playerName + ": " + e.getMessage());
        }
    }

    /** Retourne la clé de texture pour un joueur, ou {@code null}. */
    public String getSkinTexture(String playerName) {
        return this.skinCache.get(playerName);
    }

    /** Vérifie si un skin est en cache. */
    public boolean hasSkin(String playerName) {
        return this.skinCache.containsKey(playerName);
    }

    /** Invalide le cache pour un joueur. */
    public void removeSkin(String playerName) {
        this.skinCache.remove(playerName);
    }

    public void shutdown() {
        this.downloadExecutor.shutdown();
    }
}
