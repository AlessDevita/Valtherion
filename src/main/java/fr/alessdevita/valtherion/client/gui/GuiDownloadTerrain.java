package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * Interface de téléchargement du terrain dans Valtherion.
 * Affiche une barre de progression et un message pendant le téléchargement.
 */
public class GuiDownloadTerrain extends Gui {

    // Progression du téléchargement (0.0 à 1.0)
    private float downloadProgress;

    // Message de téléchargement
    private String downloadMessage;

    // Bouton pour annuler le téléchargement
    private GuiButton cancelButton;

    /**
     * Constructeur de l'interface de téléchargement du terrain.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiDownloadTerrain(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.downloadProgress = 0.0f;
        this.downloadMessage = "Téléchargement du terrain...";

        // Initialiser le bouton
        initButton();
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise le bouton de l'interface.
     */
    private void initButton() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;

        // Bouton pour annuler le téléchargement
        cancelButton = new GuiButton(
                (width - buttonWidth) / 2.0f,
                height / 2.0f + 50.0f,
                buttonWidth,
                buttonHeight,
                "Annuler"
        );
        cancelButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        cancelButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        cancelButton.setOnClick(button -> valtherion.cancelTerrainDownload());

        // Ajouter le bouton à la GUI
        addElement(cancelButton);
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner la barre de progression
        drawProgressBar();

        // Dessiner le message de téléchargement
        drawDownloadMessage();

        // Dessiner le bouton
        super.render();
    }

    /**
     * Dessine le fond de l'interface.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();
    }

    /**
     * Dessine la barre de progression.
     */
    private void drawProgressBar() {
        // Calculer la largeur de la barre de progression
        int barWidth = (int) (width * 0.6f);
        int barHeight = 20;
        int barX = (width - barWidth) / 2;
        int barY = (int) (height / 2.0f);

        // Dessiner le fond de la barre (gris foncé)
        GL11.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(barX, barY);
        GL11.glVertex2i(barX + barWidth, barY);
        GL11.glVertex2i(barX + barWidth, barY + barHeight);
        GL11.glVertex2i(barX, barY + barHeight);
        GL11.glEnd();

        // Dessiner la barre de progression (bleu clair)
        int progressWidth = (int) (barWidth * downloadProgress);
        GL11.glColor4f(0.2f, 0.6f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(barX, barY);
        GL11.glVertex2i(barX + progressWidth, barY);
        GL11.glVertex2i(barX + progressWidth, barY + barHeight);
        GL11.glVertex2i(barX, barY + barHeight);
        GL11.glEnd();
    }

    /**
     * Dessine le message de téléchargement.
     */
    private void drawDownloadMessage() {
        // Dessiner un rectangle pour simuler le texte
        float messageWidth = 200.0f;
        float messageHeight = 20.0f;
        float messageX = (width - messageWidth) / 2.0f;
        float messageY = height / 2.0f - 30.0f;

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(messageX, messageY);
        GL11.glVertex2f(messageX + messageWidth, messageY);
        GL11.glVertex2f(messageX + messageWidth, messageY + messageHeight);
        GL11.glVertex2f(messageX, messageY + messageHeight);
        GL11.glEnd();
    }

    /**
     * Met à jour la progression du téléchargement.
     * @param progress Progression (0.0 à 1.0).
     */
    public void setDownloadProgress(float progress) {
        this.downloadProgress = Math.max(0.0f, Math.min(1.0f, progress));
    }

    /**
     * Incrémente la progression du téléchargement.
     * @param increment Incrément de progression.
     */
    public void incrementDownloadProgress(float increment) {
        setDownloadProgress(downloadProgress + increment);
    }

    // --- Getters et Setters ---

    public float getDownloadProgress() {
        return downloadProgress;
    }

    public String getDownloadMessage() {
        return downloadMessage;
    }

    public void setDownloadMessage(String downloadMessage) {
        this.downloadMessage = downloadMessage;
    }

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiDownloadTerrain{" +
                "downloadProgress=" + downloadProgress +
                ", downloadMessage='" + downloadMessage + '\'' +
                '}';
    }
}
