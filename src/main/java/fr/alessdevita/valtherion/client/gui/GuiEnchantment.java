package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.item.ItemStack;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Interface d'enchantement pour les objets dans Valtherion.
 * Permet d'afficher les enchantements disponibles et d'enchanter un objet.
 */
public class GuiEnchantment extends Gui {

    // Objet à enchanter
    private ItemStack itemStack;

    // Liste des enchantements disponibles
    private final List<Enchantment> availableEnchantments;

    // Enchantement sélectionné
    private Enchantment selectedEnchantment;

    // Bouton pour appliquer l'enchantement
    private GuiButton applyButton;

    // Bouton pour annuler
    private GuiButton cancelButton;

    // Coût en niveaux d'expérience
    private int xpCost;

    // Niveaux d'expérience disponibles
    private int availableXP;

    /**
     * Constructeur de l'interface d'enchantement.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param itemStack Objet à enchanter.
     */
    public GuiEnchantment(Valtherion valtherion, int width, int height, ItemStack itemStack) {
        super(valtherion, width, height);
        this.itemStack = itemStack;
        this.availableEnchantments = new ArrayList<>();
        this.selectedEnchantment = null;
        this.xpCost = 0;
        this.availableXP = valtherion.getPlayer().getClass().getModifiers();

        // Initialiser les enchantements disponibles
        initAvailableEnchantments();

        // Initialiser les boutons
        initButtons();
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise les enchantements disponibles.
     */
    private void initAvailableEnchantments() {
        // Ajouter des enchantements par défaut
        availableEnchantments.add(new Enchantment("Protection", 1, 2));
        availableEnchantments.add(new Enchantment("Tranchant", 1, 2));
        availableEnchantments.add(new Enchantment("Efficacité", 1, 1));
        availableEnchantments.add(new Enchantment("Durabilité", 1, 1));
        availableEnchantments.add(new Enchantment("Réparation", 1, 3));
    }

    /**
     * Initialise les boutons de l'interface.
     */
    private void initButtons() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;

        // Bouton pour appliquer l'enchantement
        applyButton = new GuiButton(
                width / 2.0f - buttonWidth - 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Appliquer"
        );
        applyButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        applyButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        applyButton.setOnClick(button -> {
            try {
                applyEnchantment();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Bouton pour annuler
        cancelButton = new GuiButton(
                width / 2.0f + 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Annuler"
        );
        cancelButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        cancelButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        cancelButton.setOnClick(button -> valtherion.closeEnchantmentGui());

        // Ajouter les boutons à la GUI
        addElement(applyButton);
        addElement(cancelButton);
    }

    /**
     * Applique l'enchantement sélectionné.
     */
    private void applyEnchantment() throws InterruptedException {
        if (selectedEnchantment == null) {
            return;
        }

        // Vérifier si le joueur a assez de niveaux d'expérience
        if (availableXP >= xpCost) {
            // Appliquer l'enchantement à l'objet
            itemStack.addEnchantment(selectedEnchantment);

            // Retirer les niveaux d'expérience
            valtherion.getPlayer().wait(xpCost);
            valtherion.getPlayer().wait();

            // Fermer l'interface
            valtherion.closeEnchantmentGui();
        }
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner l'objet à enchanter
        drawItem();

        // Dessiner les enchantements disponibles
        drawEnchantments();

        // Dessiner les boutons
        super.render();
    }

    /**
     * Dessine le fond de l'interface.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.1f, 0.1f, 0.2f, 0.9f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();
    }

    /**
     * Dessine l'objet à enchanter.
     */
    private void drawItem() {
        float itemX = width / 2.0f - 25.0f;
        float itemY = height / 2.0f - 50.0f;

        // Dessiner un rectangle pour simuler l'objet
        GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(itemX, itemY);
        GL11.glVertex2f(itemX + 50.0f, itemY);
        GL11.glVertex2f(itemX + 50.0f, itemY + 50.0f);
        GL11.glVertex2f(itemX, itemY + 50.0f);
        GL11.glEnd();
    }

    /**
     * Dessine les enchantements disponibles.
     */
    private void drawEnchantments() {
        float startX = 50.0f;
        float startY = height / 2.0f + 20.0f;
        float enchantmentHeight = 30.0f;

        for (int i = 0; i < availableEnchantments.size(); i++) {
            Enchantment enchantment = availableEnchantments.get(i);

            // Dessiner l'enchantement
            float x = startX;
            float y = startY - i * enchantmentHeight;

            // Couleur en fonction de si l'enchantement est sélectionné
            if (selectedEnchantment == enchantment) {
                GL11.glColor4f(0.2f, 0.6f, 1.0f, 1.0f);
            } else {
                GL11.glColor4f(0.5f, 0.5f, 0.8f, 1.0f);
            }

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x + 200.0f, y);
            GL11.glVertex2f(x + 200.0f, y + enchantmentHeight);
            GL11.glVertex2f(x, y + enchantmentHeight);
            GL11.glEnd();
        }
    }

    // --- Getters et Setters ---

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public List<Enchantment> getAvailableEnchantments() {
        return availableEnchantments;
    }

    public Enchantment getSelectedEnchantment() {
        return selectedEnchantment;
    }

    public void setSelectedEnchantment(Enchantment selectedEnchantment) {
        this.selectedEnchantment = selectedEnchantment;
        this.xpCost = selectedEnchantment != null ? selectedEnchantment.getLevel() * 2 : 0;
    }

    public int getXpCost() {
        return xpCost;
    }

    public int getAvailableXP() {
        return availableXP;
    }

    public void setAvailableXP(int availableXP) {
        this.availableXP = availableXP;
    }

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        availableEnchantments.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiEnchantment{" +
                "itemStack=" + itemStack +
                ", selectedEnchantment=" + selectedEnchantment +
                '}';
    }

    // --- Classe interne pour les enchantements ---

    /**
     * Classe pour représenter un enchantement.
     */
    public static class Enchantment {
        private String name;
        private int level;
        private int xpCost;

        public Enchantment(String name, int level, int xpCost) {
            this.name = name;
            this.level = level;
            this.xpCost = xpCost;
        }

        // Getters et Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public int getXpCost() { return xpCost; }
        public void setXpCost(int xpCost) { this.xpCost = xpCost; }
    }
}
