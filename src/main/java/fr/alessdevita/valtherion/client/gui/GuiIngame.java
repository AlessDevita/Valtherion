package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.inventory.Item;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Interface en jeu (HUD) dans Valtherion.
 * Affiche la barre de santé, la barre de mana, l'inventaire rapide, et les messages de chat.
 */
public class GuiIngame extends Gui {

    public void updateTick() {
        if (chatMessageDisplayTime > 0.0F) chatMessageDisplayTime = Math.max(0.0F, chatMessageDisplayTime - 0.05F);
    }

    // Joueur associé
    private LivingEntity player;

    // Inventaire du joueur
    private Inventory playerInventory;

    // Liste des messages de chat temporaires
    private final List<ChatMessage> chatMessages;

    // Temps d'affichage des messages de chat (en secondes)
    private float chatMessageDisplayTime;

    // Couleur de la barre de santé
    private Vector4f healthBarColor;

    // Couleur de la barre de mana
    private Vector4f manaBarColor;

    // Couleur de la barre d'expérience
    private Vector4f xpBarColor;

    /**
     * Constructeur de l'interface en jeu.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiIngame(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.player = valtherion.thePlayer;
        this.playerInventory = player == null ? null : player.getInventory();
        this.chatMessages = new ArrayList<>();
        this.chatMessageDisplayTime = 5.0f;
        this.healthBarColor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f); // Rouge
        this.manaBarColor = new Vector4f(0.0f, 0.0f, 1.0f, 1.0f); // Bleu
        this.xpBarColor = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f); // Vert
    }

    public GuiIngame(Valtherion valtherion, List<ChatMessage> chatMessages) {
        super();
        this.chatMessages = chatMessages;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Mettre à jour les messages de chat
        for (int i = chatMessages.size() - 1; i >= 0; i--) {
            ChatMessage message = chatMessages.get(i);
            message.update(deltaTime);

            // Supprimer les messages invisibles
            if (!message.isVisible()) {
                chatMessages.remove(i);
            }
        }
    }

    @Override
    public void render() {
        // Dessiner la barre de santé
        drawHealthBar();

        // Dessiner la barre de mana
        drawManaBar();

        // Dessiner la barre d'expérience
        drawXPBar();

        // Dessiner l'inventaire rapide (hotbar)
        drawHotbar();

        // Dessiner les messages de chat
        drawChatMessages();
    }

    public Object getChatGUI() {
        return null;
    }

    /**
     * Dessine la barre de santé.
     */
    private void drawHealthBar() {
        float barWidth = 200.0f;
        float barHeight = 20.0f;
        float barX = 10.0f;
        float barY = height - 40.0f;

        // Dessiner le fond de la barre
        GL11.glColor4f(0.2f, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(barX, barY);
        GL11.glVertex2f(barX + barWidth, barY);
        GL11.glVertex2f(barX + barWidth, barY + barHeight);
        GL11.glVertex2f(barX, barY + barHeight);
        GL11.glEnd();

        // Dessiner la barre de santé
        float healthPercentage = player.getHealth() / player.getMaxHealth();
        float healthWidth = barWidth * healthPercentage;
        GL11.glColor4f(healthBarColor.x, healthBarColor.y, healthBarColor.z, healthBarColor.w);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(barX, barY);
        GL11.glVertex2f(barX + healthWidth, barY);
        GL11.glVertex2f(barX + healthWidth, barY + barHeight);
        GL11.glVertex2f(barX, barY + barHeight);
        GL11.glEnd();
    }

    /**
     * Dessine la barre de mana.
     */
    private void drawManaBar() {
        float barWidth = 200.0f;
        float barHeight = 15.0f;
        float barX = 10.0f;
        float barY = height - 65.0f;

        // Dessiner le fond de la barre
        GL11.glColor4f(0.0f, 0.0f, 0.2f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(barX, barY);
        GL11.glVertex2f(barX + barWidth, barY);
        GL11.glVertex2f(barX + barWidth, barY + barHeight);
        GL11.glVertex2f(barX, barY + barHeight);
        GL11.glEnd();

        // Dessiner la barre de mana
        float manaPercentage = player.getMaxMana() <= 0 ? 0 : player.getMana() / player.getMaxMana();
        float manaWidth = barWidth * manaPercentage;
        GL11.glColor4f(manaBarColor.x, manaBarColor.y, manaBarColor.z, manaBarColor.w);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(barX, barY);
        GL11.glVertex2f(barX + manaWidth, barY);
        GL11.glVertex2f(barX + manaWidth, barY + barHeight);
        GL11.glVertex2f(barX, barY + barHeight);
        GL11.glEnd();
    }

    /**
     * Dessine la barre d'expérience.
     */
    private void drawXPBar() {
        float barWidth = 200.0f;
        float barHeight = 10.0f;
        float barX = 10.0f;
        float barY = height - 85.0f;

        // Dessiner le fond de la barre
        GL11.glColor4f(0.0f, 0.2f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(barX, barY);
        GL11.glVertex2f(barX + barWidth, barY);
        GL11.glVertex2f(barX + barWidth, barY + barHeight);
        GL11.glVertex2f(barX, barY + barHeight);
        GL11.glEnd();

        // Dessiner la barre d'expérience
        float xpPercentage = (float) player.getXP() / player.getMaxXP();
        float xpWidth = barWidth * xpPercentage;
        GL11.glColor4f(xpBarColor.x, xpBarColor.y, xpBarColor.z, xpBarColor.w);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(barX, barY);
        GL11.glVertex2f(barX + xpWidth, barY);
        GL11.glVertex2f(barX + xpWidth, barY + barHeight);
        GL11.glVertex2f(barX, barY + barHeight);
        GL11.glEnd();
    }

    /**
     * Dessine l'inventaire rapide (hotbar).
     */
    private void drawHotbar() {
        int slotSize = 30;
        int slotsPerRow = 9;
        int startX = (width - slotsPerRow * slotSize) / 2;
        int startY = height - slotSize - 10;

        for (int i = 0; i < slotsPerRow; i++) {
            int x = startX + i * slotSize;
            int y = startY;

            // Dessiner le slot
            GL11.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2i(x, y);
            GL11.glVertex2i(x + slotSize, y);
            GL11.glVertex2i(x + slotSize, y + slotSize);
            GL11.glVertex2i(x, y + slotSize);
            GL11.glEnd();

            // Dessiner l'objet sélectionné
            if (i == playerInventory.getSelectedSlot()) {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex2i(x, y);
                GL11.glVertex2i(x + slotSize, y);
                GL11.glVertex2i(x + slotSize, y + slotSize);
                GL11.glVertex2i(x, y + slotSize);
                GL11.glEnd();
            }

            // Dessiner l'objet dans le slot
            Item itemStack = playerInventory.getItem(i);
            if (itemStack != null) {
                GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2i(x + 2, y + 2);
                GL11.glVertex2i(x + slotSize - 2, y + 2);
                GL11.glVertex2i(x + slotSize - 2, y + slotSize - 2);
                GL11.glVertex2i(x + 2, y + slotSize - 2);
                GL11.glEnd();
            }
        }
    }

    /**
     * Dessine les messages de chat.
     */
    private void drawChatMessages() {
        float startY = height - 120.0f;
        float lineHeight = 15.0f;

        for (int i = chatMessages.size() - 1; i >= 0; i--) {
            ChatMessage message = chatMessages.get(i);
            if (message.isVisible()) {
                float y = startY - (chatMessages.size() - 1 - i) * lineHeight;

                // Dessiner le message (simplifié)
                GL11.glColor4f(1.0f, 1.0f, 1.0f, message.getAlpha());
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2f(10.0f, y);
                GL11.glVertex2f(10.0f + message.getText().length() * 5.0f, y);
                GL11.glVertex2f(10.0f + message.getText().length() * 5.0f, y + lineHeight);
                GL11.glVertex2f(10.0f, y + lineHeight);
                GL11.glEnd();
            }
        }
    }

    /**
     * Ajoute un message de chat.
     * @param message Message à ajouter.
     */
    public void addChatMessage(String message) {
        chatMessages.add(new ChatMessage(message, chatMessageDisplayTime));
    }

    // --- Getters et Setters ---

    public LivingEntity getPlayer() {
        return player;
    }

    public void setPlayer(LivingEntity player) {
        this.player = player;
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public void setPlayerInventory(Inventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public float getChatMessageDisplayTime() {
        return chatMessageDisplayTime;
    }

    public void setChatMessageDisplayTime(float chatMessageDisplayTime) {
        this.chatMessageDisplayTime = chatMessageDisplayTime;
    }

    public Vector4f getHealthBarColor() {
        return healthBarColor;
    }

    public void setHealthBarColor(Vector4f healthBarColor) {
        this.healthBarColor = healthBarColor;
    }

    public Vector4f getManaBarColor() {
        return manaBarColor;
    }

    public void setManaBarColor(Vector4f manaBarColor) {
        this.manaBarColor = manaBarColor;
    }

    public Vector4f getXpBarColor() {
        return xpBarColor;
    }

    public void setXpBarColor(Vector4f xpBarColor) {
        this.xpBarColor = xpBarColor;
    }

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        chatMessages.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiIngame{" +
                "player=" + player +
                ", chatMessages=" + chatMessages.size() +
                '}';
    }

    // --- Classe interne pour les messages de chat ---

    /**
     * Classe pour représenter un message de chat temporaire.
     */
    public static class ChatMessage {
        private String text;
        private float displayTime;
        private float alpha;

        public ChatMessage(String text, float displayTime) {
            this.text = text;
            this.displayTime = displayTime;
            this.alpha = 1.0f;
        }

        /**
         * Met à jour le message.
         * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
         */
        public void update(float deltaTime) {
            displayTime -= deltaTime;
            alpha = Math.min(1.0f, displayTime / 2.0f);
        }

        // Getters et Setters
        public String getText() { return text; }
        public float getDisplayTime() { return displayTime; }
        public float getAlpha() { return alpha; }
        public boolean isVisible() { return displayTime > 0.0f; }
    }
}
