package fr.alessdevita.valtherion.client.gui;

import org.joml.Vector4f;
/**
 * Classe pour représenter une ligne de chat dans Valtherion
 * Stocke le message, l'expéditeur, et le temps d'affichage.
 */
public class ChatLine {
	// Message de la ligne de chat
	private String message;
	
	// Nom de l'expéditeur
	private String sender;
	
	// Temps d'affichage restant (en secondes)
	private float displayTime;
	
	// Temps total d'affichage (en secondes)
	private float totalDisplayTime;
	
	// Couleur du texte
	private Vector4f color;
	
	// Indique si la ligne est visible
	private boolean isVisibe;
	
	// Échelle du texte (pour les animations)
    private float scale;
    
    // Opacité du texte (pour les animations)
    private float alpha;
    private boolean isVisible;

    /**
     * Constructeur de la ligne de chat.
     * @param message Message de la ligne de chat.
     * @param sender Nom de l'expéditeur.
     */
    public ChatLine(String message, String sender) {
        this(message, sender, 10.0f); // Temps d'affichage par défaut : 10 secondes
    }
    
    /**
     * Constructeur de la ligne de chat avec un temps d'affichage.
     * @param message Message de la ligne de chat.
     * @param sender Nom de l'expéditeur.
     * @param displayTime Temps d'affichage (en secondes).
     */
    public ChatLine(String message, String sender, float displayTime) {
        this.message = message;
        this.sender = sender;
        this.displayTime = displayTime;
        this.totalDisplayTime = displayTime;
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // Blanc
        this.isVisible = true;
        this.scale = 1.0f;
        this.alpha = 1.0f;
    }
    
    /**
     * Met à jour la ligne de chat.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        if (!isVisible) {
            return;
        }
        
        // Mettre à jour le temps d'affichage restant
        displayTime -= deltaTime;
        
        // Si le temps d'affichage est écoulé, masquer la ligne
        if (displayTime <= 0.0f) {
            isVisible = false;
        }
        
        // Calculer l'opacité en fonction du temps restant (fondu)
        alpha = Math.min(1.0f, displayTime / 2.0f);
    }
    
    /**
     * Récupère le message formaté (avec le nom de l'expéditeur).
     * @return Message formaté.
     */
    public String getFormattedMessage() {
        return "[" + sender + "]: " + message;
    }
    
    // --- Getters et Setters ---
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getSender() {
        return sender;
    }
    
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public float getDisplayTime() {
        return displayTime;
    }
    
    public void setDisplayTime(float displayTime) {
        this.displayTime = displayTime;
    }
    
    public float getTotalDisplayTime() {
        return totalDisplayTime;
    }
    
    public void setTotalDisplayTime(float totalDisplayTime) {
        this.totalDisplayTime = totalDisplayTime;
    }
    
    public Vector4f getColor() {
        return color;
    }
    
    public void setColor(Vector4f color) {
        this.color = color;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
    
    public float getScale() {
        return scale;
    }
    
    public void setScale(float scale) {
        this.scale = scale;
    }
    
    public float getAlpha() {
        return alpha;
    }
    
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
    
    /**
     * Retourne une représentation textuelle de la ligne de chat.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ChatLine{" + 
               "message='" + message + '\'' + 
               ", sender='" + sender + '\'' + 
               ", displayTime=" + displayTime + 
               '}';
    }
}
