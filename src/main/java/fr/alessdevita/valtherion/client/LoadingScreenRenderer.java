package fr.alessdevita.valtherion.client;

import fr.alessdevita.valtherion.client.gui.FontRenderer;
import fr.alessdevita.valtherion.client.gui.IProgressMeter;
import fr.alessdevita.valtherion.client.renderer.texture.TextureManager;
import fr.alessdevita.valtherion.client.resources.IResourceLoadListener;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Classe pour gérer le rendu de l'écran de chargement dans Valtherion.
 * Style DEVITA STUDIOS : fond noir, logo texture, barre de progression des ressources.
 * Implémente IResourceLoadListener pour suivre le chargement des ressources.
 * Implémente IProgressMeter pour la compatibilité avec les écrans de progression.
 */
public class LoadingScreenRenderer implements IResourceLoadListener, IProgressMeter {

    private static final Logger logger = LoggerFactory.getLogger(LoadingScreenRenderer.class);

    // Couleurs DEVITA STUDIOS - fond noir
    private static final float BG_R = 0.0f;
    private static final float BG_G = 0.0f;
    private static final float BG_B = 0.0f;

    // Chemin de la texture du logo (dans le classpath : src/main/resources/)
    private static final String LOGO_TEXTURE_PATH = "assets/valtherion/textures/gui/title/devitastudios.png";

    // Dimensions de la barre de progression
    private static final int BAR_HEIGHT = 20;
    private static final int BAR_WIDTH_PERCENT = 50; // Pourcentage de la largeur de l'écran

    // ID de la fenêtre GLFW
    private final long windowId;

    // Largeur de la fenêtre
    private final int windowWidth;

    // Hauteur de la fenêtre
    private final int windowHeight;

    // Progression du chargement (0.0 à 1.0)
    private float progress;

    // Message actuel affiché
    private String currentMessage;

    // Message de sous-titre (nom de la ressource en cours)
    private String subMessage;

    // Nombre total d'étapes de chargement
    private int totalSteps;

    // Étape actuelle
    private int currentStep;

    // Indique si l'écran de chargement est initialisé
    private boolean isInitialized;

    // TextureManager pour charger la texture du logo
    private TextureManager textureManager;

    // FontRenderer pour afficher le texte
    private FontRenderer fontRenderer;

    // Dimensions de la texture du logo (après chargement)
    private int textureWidth = 0;
    private int textureHeight = 0;

    // Ressources chargées et totales (pour l'affichage)
    private int loadedResources = 0;
    private int totalResources = 0;

    /**
     * Constructeur de l'écran de chargement.
     * @param windowId ID de la fenêtre GLFW.
     * @param windowWidth Largeur de la fenêtre.
     * @param windowHeight Hauteur de la fenêtre.
     */
    public LoadingScreenRenderer(long windowId, int windowWidth, int windowHeight) {
        this.windowId = windowId;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.currentMessage = "";
        this.subMessage = "";
        this.totalSteps = 0;
        this.currentStep = 0;
        this.fontRenderer = new FontRenderer(16.0f);
    }

    /**
     * Durée minimale du loading screen en millisecondes (1 seconde).
     */
    private static final long MIN_LOADING_DURATION_MS = 1000L;

    /**
     * Messages affichés séquentiellement pendant le chargement.
     */
    private static final String[] LOADING_MESSAGES = {
        "Chargement des ressources...",
        "Initialisation du son...",
        "Chargement de la police...",
        "Préparation du rendu...",
        "Chargement des entités...",
        "Configuration de la souris...",
        "Initialisation du monde...",
        "Chargement des effets...",
        "Préparation du menu...",
        "Bienvenue !"
    };

    /**
     * Temps de démarrage du loading screen en millisecondes.
     */
    private long startTime;

    /**
     * Indique si le chargement est terminé.
     */
    private boolean isLoadingComplete;

    /**
     * Constructeur avec référence à Valtherion.
     * @param valtherion Instance du client Valtherion.
     * @param windowId ID de la fenêtre GLFW.
     * @param windowWidth Largeur de la fenêtre.
     * @param windowHeight Hauteur de la fenêtre.
     */
    public LoadingScreenRenderer(Valtherion valtherion, long windowId, int windowWidth, int windowHeight) {
        this.windowId = windowId;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.currentMessage = "";
        this.subMessage = "";
        this.totalSteps = 0;
        this.currentStep = 0;
        this.startTime = System.currentTimeMillis();
        this.isLoadingComplete = false;
        if (valtherion != null) {
            this.textureManager = valtherion.getTextureManager();
            this.fontRenderer = valtherion.fontRenderer;
        }
        if (this.fontRenderer == null) {
            this.fontRenderer = new FontRenderer(16.0f);
        }
    }

    /**
     * Démarre l'écran de chargement animé qui dure au moins la durée minimale.
     * Cette méthode est bloquante jusqu'à ce que le chargement soit terminé.
     */
    public void startLoadingScreen() {
        logger.info("=== DÉBUT startLoadingScreen ===");
        logger.info("Thread actuel : {}", Thread.currentThread().getName());
        logger.info("Fenêtre : 0x{}", Long.toHexString(this.windowId));
        logger.info("Dimensions : {}x{}", this.windowWidth, this.windowHeight);
        logger.info("FontRenderer : {}", (this.fontRenderer != null ? "OK" : "NULL"));
        logger.info("TextureManager : {}", (this.textureManager != null ? "OK" : "NULL"));

        this.startTime = System.currentTimeMillis();
        this.progress = 0.0f;
        this.currentMessage = LOADING_MESSAGES[0];
        this.isLoadingComplete = false;

        // Initialiser le rendu
        init();
        logger.info("init() terminé, isInitialized = {}", this.isInitialized);

        long elapsedTime = 0;
        int messageIndex = 0;
        int frameCount = 0;

        while (elapsedTime < MIN_LOADING_DURATION_MS) {
            elapsedTime = System.currentTimeMillis() - this.startTime;

            // Calculer la progression basée sur le temps écoulé
            this.progress = (float) elapsedTime / MIN_LOADING_DURATION_MS;

            // Mettre à jour le message en fonction de la progression
            int newIndex = (int) (this.progress * LOADING_MESSAGES.length);
            if (newIndex >= LOADING_MESSAGES.length) newIndex = LOADING_MESSAGES.length - 1;
            if (newIndex != messageIndex) {
                messageIndex = newIndex;
                this.currentMessage = LOADING_MESSAGES[messageIndex];
                logger.info("Message : {} ({}%)", this.currentMessage, (int)(this.progress * 100));
            }

            // Mettre à jour le sous-message avec le temps restant
            long remainingMs = MIN_LOADING_DURATION_MS - elapsedTime;
            long remainingSeconds = (remainingMs + 999) / 1000;
            this.subMessage = remainingSeconds + " seconde" + (remainingSeconds > 1 ? "s" : "") + " restante" + (remainingSeconds > 1 ? "s" : "");

            // Traiter les événements GLFW pour éviter "Ne répond pas"
            GLFW.glfwPollEvents();

            // Rendre l'écran
            render();
            GLFW.glfwSwapBuffers(this.windowId);
            frameCount++;

            // Petit délai pour ne pas surcharger le CPU (environ 30 FPS)
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Finalisation
        this.progress = 1.0f;
        this.currentMessage = LOADING_MESSAGES[LOADING_MESSAGES.length - 1];
        this.subMessage = "";
        this.isLoadingComplete = true;

        // Dernier rendu
        render();
        GLFW.glfwSwapBuffers(this.windowId);

        logger.info("=== FIN startLoadingScreen === (frames: {}, temps: {}ms)", frameCount, elapsedTime);
    }

    /**
     * Retourne true si le chargement est terminé.
     */
    public boolean isLoadingComplete() {
        return isLoadingComplete;
    }

    /**
     * Affiche un message de progression et met à jour l'écran.
     * @param message Message à afficher.
     */
    @Override
    public void displayProgressMessage(String message) {
        this.currentMessage = message;
        render();
        GLFW.glfwSwapBuffers(this.windowId);
    }

    /**
     * Réinitialise la progression et affiche un nouveau message.
     * @param message Message à afficher.
     */
    public void resetProgressAndMessage(String message) {
        this.progress = 0.0F;
        this.currentStep = 0;
        this.loadedResources = 0;
        this.totalResources = 0;
        this.currentMessage = message;
        this.subMessage = "";
        displayProgressMessage(message);
    }

    // --- Implémentation de IResourceLoadListener ---

    @Override
    public void onLoadingStart(int totalResources) {
        this.totalResources = totalResources;
        this.loadedResources = 0;
        this.progress = 0.0f;
        this.currentMessage = "Chargement des ressources...";
        this.subMessage = "";
        render();
        GLFW.glfwSwapBuffers(this.windowId);
    }

    @Override
    public void onResourceLoaded(int loadedResources, int totalResources, String resourceName) {
        this.loadedResources = loadedResources;
        this.totalResources = totalResources;
        this.progress = (float) loadedResources / totalResources;
        this.subMessage = resourceName;
        render();
        GLFW.glfwSwapBuffers(this.windowId);
    }

    @Override
    public void onLoadingComplete() {
        this.currentMessage = "Chargement terminé !";
        this.subMessage = "";
        this.progress = 1.0f;
        render();
        GLFW.glfwSwapBuffers(this.windowId);
    }

    // --- Implémentation de IProgressMeter ---

    @Override
    public void setLoadingProgress(int progress) {
        this.progress = progress / 100.0f;
        render();
        GLFW.glfwSwapBuffers(this.windowId);
    }

    /**
     * Initialise l'écran de chargement avec les capacités OpenGL.
     */
    public void init() {
        if (isInitialized) {
            return;
        }

        // Lier le contexte OpenGL
        GLFW.glfwMakeContextCurrent(windowId);

        // Créer les capacités OpenGL
        if (GL.getCapabilities() == null) {
            GL.createCapabilities();
        }

        // Configurer le viewport
        GL11.glViewport(0, 0, windowWidth, windowHeight);

        // Configurer la matrice de projection (orthographique pour le 2D)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, windowWidth, windowHeight, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // Désactiver le depth test (pas nécessaire en 2D)
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // Activer le blending pour les effets de transparence
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Initialiser le FontRenderer pour afficher le texte
        if (this.fontRenderer != null) {
            this.fontRenderer.init();
        }

        // Charger les dimensions de la texture une seule fois lors de l'initialisation
        loadTextureDimensions();

        isInitialized = true;
    }

    /**
     * Rend l'écran de chargement complet style DEVITA STUDIOS.
     */
    public void render() {
        if (!isInitialized) {
            init();
        }

        // Effacer l'écran avec le noir
        GL11.glClearColor(BG_R, BG_G, BG_B, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        // Dessiner le fond noir
        drawBackground();

        // Dessiner le logo DEVITA STUDIOS en pixel art futuriste
        drawDevitaLogo();

        // Dessiner la barre de progression
        drawProgressBar();

        // Dessiner le message de chargement
        drawLoadingMessage();
    }

    /**
     * Dessine le fond noir DEVITA STUDIOS.
     */
    private void drawBackground() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(BG_R, BG_G, BG_B, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0.0f, 0.0f);
        GL11.glVertex2f((float) windowWidth, 0.0f);
        GL11.glVertex2f((float) windowWidth, (float) windowHeight);
        GL11.glVertex2f(0.0f, (float) windowHeight);
        GL11.glEnd();
    }

    /**
     * Dessine le logo DEVITA STUDIOS en utilisant la texture au centre de l'écran.
     */
    private void drawDevitaLogo() {
        if (textureManager == null) {
            return;
        }

        // Récupérer les dimensions de la texture si pas encore connues
        if (textureWidth == 0 || textureHeight == 0) {
            loadTextureDimensions();
        }

        // Si on n'a toujours pas les dimensions, on ne peut pas dessiner
        if (textureWidth == 0 || textureHeight == 0) {
            return;
        }

        // Charger et lier la texture
        textureManager.bindTexture(LOGO_TEXTURE_PATH);

        // Activer la texture 2D
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        // Calculer la position centrée
        int logoX = (windowWidth - textureWidth) / 2;
        int logoY = (windowHeight - textureHeight) / 2 - 50; // Légèrement au-dessus du centre

        // Dessiner le rectangle texturé
        GL11.glBegin(GL11.GL_QUADS);
        // Bas-gauche
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(logoX, logoY + textureHeight);
        // Bas-droite
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(logoX + textureWidth, logoY + textureHeight);
        // Haut-droite
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(logoX + textureWidth, logoY);
        // Haut-gauche
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(logoX, logoY);
        GL11.glEnd();

        // Désactiver la texture après utilisation
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Charge les dimensions de la texture en lisant l'image depuis le classpath.
     */
    private void loadTextureDimensions() {
        try {
            // Essayer d'abord avec le ClassLoader du thread courant
            String path = LOGO_TEXTURE_PATH.startsWith("/") ? LOGO_TEXTURE_PATH.substring(1) : LOGO_TEXTURE_PATH;
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            
            // Si pas trouvé, essayer avec le ClassLoader de la classe
            if (stream == null) {
                stream = LoadingScreenRenderer.class.getClassLoader().getResourceAsStream(path);
            }
            
            // Si toujours pas trouvé, essayer avec le chemin relatif aux ressources
            if (stream == null) {
                stream = LoadingScreenRenderer.class.getResourceAsStream("/" + path);
            }
            
            if (stream != null) {
                try {
                    BufferedImage image = ImageIO.read(stream);
                    if (image != null) {
                        textureWidth = image.getWidth();
                        textureHeight = image.getHeight();
                    }
                } finally {
                    stream.close();
                }
            }
        } catch (Exception e) {
            logger.error("Erreur en chargeant les dimensions de la texture : {}", e.getMessage(), e);
        }
        // Fallback si la texture n'a pas pu être lue
        if (textureWidth == 0) {
            textureWidth = 400;
            textureHeight = 150;
            logger.info("Utilisation des dimensions par défaut : {}x{}", textureWidth, textureHeight);
        }
    }

    /**
     * Dessine la barre de progression style Minecraft avec couleurs dynamiques.
     */
    private void drawProgressBar() {
        int barWidth = (int) (windowWidth * BAR_WIDTH_PERCENT / 100.0f);
        int barHeight = BAR_HEIGHT;
        int barX = (windowWidth - barWidth) / 2;
        // Positionner la barre en dessous du logo
        int logoBottom = (windowHeight + textureHeight) / 2 - 50 + textureHeight;
        int barY = logoBottom + 20;

        // Bordure extérieure noire
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(barX - 2, barY - 2);
        GL11.glVertex2i(barX + barWidth + 2, barY - 2);
        GL11.glVertex2i(barX + barWidth + 2, barY + barHeight + 2);
        GL11.glVertex2i(barX - 2, barY + barHeight + 2);
        GL11.glEnd();

        // Fond de la barre (gris foncé)
        GL11.glColor4f(0.15f, 0.15f, 0.15f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(barX, barY);
        GL11.glVertex2i(barX + barWidth, barY);
        GL11.glVertex2i(barX + barWidth, barY + barHeight);
        GL11.glVertex2i(barX, barY + barHeight);
        GL11.glEnd();

        // Barre de progression remplie avec couleur dynamique
        if (progress > 0.0f) {
            int progressWidth = Math.max(1, (int) (barWidth * progress));

            // Couleur dynamique : du bleu vers le vert puis vers le blanc
            float r, g, b;
            if (progress < 0.5f) {
                // Bleu -> Vert
                float t = progress / 0.5f;
                r = 0.0f;
                g = t;
                b = 1.0f - t;
            } else {
                // Vert -> Blanc
                float t = (progress - 0.5f) / 0.5f;
                r = t;
                g = 1.0f;
                b = t;
            }

            // Remplissage de la barre
            GL11.glColor4f(r, g, b, 1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2i(barX, barY);
            GL11.glVertex2i(barX + progressWidth, barY);
            GL11.glVertex2i(barX + progressWidth, barY + barHeight);
            GL11.glVertex2i(barX, barY + barHeight);
            GL11.glEnd();

            // Effet de brillance sur le bord gauche
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2i(barX, barY);
            GL11.glVertex2i(barX, barY + barHeight);
            GL11.glEnd();
        }

        // Bordure intérieure
        GL11.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
        GL11.glBegin(GL11.GL_LINES);
        // Haut
        GL11.glVertex2i(barX, barY);
        GL11.glVertex2i(barX + barWidth, barY);
        // Bas
        GL11.glVertex2i(barX, barY + barHeight);
        GL11.glVertex2i(barX + barWidth, barY + barHeight);
        // Gauche
        GL11.glVertex2i(barX, barY);
        GL11.glVertex2i(barX, barY + barHeight);
        // Droite
        GL11.glVertex2i(barX + barWidth, barY);
        GL11.glVertex2i(barX + barWidth, barY + barHeight);
        GL11.glEnd();

        // Afficher le pourcentage au centre de la barre
        int percentage = (int) (progress * 100);
        String percentageText = percentage + "%";

        if (fontRenderer != null) {
            int textWidth = fontRenderer.getStringWidth(percentageText);
            int textX = barX + (barWidth - textWidth) / 2;
            int textY = barY + (barHeight - 12) / 2;
            fontRenderer.drawString(percentageText, textX, textY, 0xFFFFFF);
        }
    }

    /**
     * Dessine le message de chargement actuel avec le FontRenderer.
     */
    private void drawLoadingMessage() {
        if (fontRenderer == null) {
            return;
        }

        // Positionner le message en dessous de la barre de progression
        int logoBottom = (windowHeight + textureHeight) / 2 - 50 + textureHeight;
        int barY = logoBottom + 20;
        int messageY = barY + 45;

        // Message principal
        String mainMsg = currentMessage != null && !currentMessage.isEmpty()
                ? currentMessage : "Chargement...";

        // Afficher le message principal avec ombre
        fontRenderer.drawCenteredString(mainMsg, windowWidth / 2, messageY, 0xFFFFFF);

        // Afficher le compteur de ressources si disponible
        if (totalResources > 0) {
            String counterMsg = loadedResources + " / " + totalResources + " ressources";
            fontRenderer.drawCenteredString(counterMsg, windowWidth / 2, messageY + 16, 0xAAAAAA);
        }

        // Afficher le sous-message (nom de la ressource en cours)
        if (subMessage != null && !subMessage.isEmpty()) {
            // Tronquer si trop long
            String displaySub = subMessage;
            if (displaySub.length() > 60) {
                displaySub = "..." + displaySub.substring(displaySub.length() - 57);
            }
            fontRenderer.drawCenteredString(displaySub, windowWidth / 2, messageY + 32, 0x888888);
        }
    }

    /**
     * Définit le nombre total d'étapes de chargement.
     * @param steps Nombre total d'étapes.
     */
    public void setTotalSteps(int steps) {
        this.totalSteps = steps;
        this.currentStep = 0;
        this.progress = 0.0f;
    }

    /**
     * Avance à l'étape suivante de chargement et met à jour l'écran.
     */
    public void nextStep() {
        if (totalSteps > 0) {
            this.currentStep++;
            this.progress = (float) currentStep / totalSteps;
        }
        render();
        GLFW.glfwSwapBuffers(this.windowId);
    }

    /**
     * Met à jour la progression manuellement.
     * @param progress Progression (0.0 à 1.0).
     */
    public void setProgress(float progress) {
        this.progress = Math.max(0.0f, Math.min(1.0f, progress));
    }

    /**
     * Incrémente la progression.
     * @param increment Incrément de progression.
     */
    public void incrementProgress(float increment) {
        setProgress(progress + increment);
    }

    /**
     * Définit le message de chargement actuel.
     * @param message Message à afficher.
     */
    public void setMessage(String message) {
        this.currentMessage = message;
    }

    /**
     * Nettoie les ressources OpenGL.
     */
    public void cleanup() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        isInitialized = false;
    }

    // --- Getters ---

    /**
     * Retourne la progression actuelle.
     * @return Progression entre 0.0 et 1.0.
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Retourne si l'écran est initialisé.
     * @return true si initialisé.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Retourne le message actuel.
     * @return Message de chargement.
     */
    public String getCurrentMessage() {
        return currentMessage;
    }

    /**
     * Retourne l'étape actuelle.
     * @return Numéro de l'étape.
     */
    public int getCurrentStep() {
        return currentStep;
    }

    /**
     * Retourne le nombre total d'étapes.
     * @return Nombre total d'étapes.
     */
    public int getTotalSteps() {
        return totalSteps;
    }

    /**
     * Retourne le nombre de ressources chargées.
     * @return Nombre de ressources chargées.
     */
    public int getLoadedResources() {
        return loadedResources;
    }

    /**
     * Retourne le nombre total de ressources.
     * @return Nombre total de ressources.
     */
    public int getTotalResources() {
        return totalResources;
    }

    /**
     * Retourne le sous-message actuel.
     * @return Sous-message.
     */
    public String getSubMessage() {
        return subMessage;
    }

    /**
     * Définit le sous-message.
     * @param subMessage Sous-message à afficher.
     */
    public void setSubMessage(String subMessage) {
        this.subMessage = subMessage;
    }

    /**
     * Retourne le FontRenderer utilisé.
     * @return FontRenderer.
     */
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    /**
     * Définit le FontRenderer à utiliser pour afficher le texte.
     * @param fontRenderer Le FontRenderer à utiliser.
     */
    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }
}
