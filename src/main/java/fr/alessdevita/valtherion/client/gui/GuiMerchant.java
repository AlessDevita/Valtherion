package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.inventory.ItemStack;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Interface du marchand dans Valtherion.
 * Permet d'échanger des objets avec un marchand.
 */
public class GuiMerchant extends Gui {

    // Inventaire du marchand
    private Inventory merchantInventory;

    // Inventaire du joueur
    private Inventory playerInventory;

    // Liste des échanges disponibles
    private final List<MerchantTrade> trades;

    // Échange sélectionné
    private MerchantTrade selectedTrade;

    // Bouton pour effectuer l'échange
    private GuiButton tradeButton;

    // Bouton pour annuler
    private GuiButton cancelButton;

    // Index de l'échange sélectionné
    private int selectedTradeIndex;

    /**
     * Constructeur de l'interface du marchand.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param merchantInventory Inventaire du marchand.
     * @param playerInventory Inventaire du joueur.
     */
    public GuiMerchant(Valtherion valtherion, int width, int height, Inventory merchantInventory, Inventory playerInventory) {
        super(valtherion, width, height);
        this.merchantInventory = merchantInventory;
        this.playerInventory = playerInventory;
        this.trades = new ArrayList<>();
        this.selectedTrade = null;
        this.selectedTradeIndex = -1;

        // Initialiser les échanges par défaut
        initDefaultTrades();

        // Initialiser les boutons
        initButtons();
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise les échanges par défaut.
     */
    private void initDefaultTrades() {
        // Échange 1 : 1 émeraude pour 4 blocs de pierre
        trades.add(new MerchantTrade(
                new ItemStack("EMERALD", 1),
                new ItemStack("STONE", 4)
        ));

        // Échange 2 : 2 émeraudes pour 1 épée en fer
        trades.add(new MerchantTrade(
                new ItemStack("EMERALD", 2),
                new ItemStack("IRON_SWORD", 1)
        ));

        // Échange 3 : 3 émeraudes pour 1 armure en fer
        trades.add(new MerchantTrade(
                new ItemStack("EMERALD", 3),
                new ItemStack("IRON_ARMOR", 1)
        ));
    }

    /**
     * Initialise les boutons de l'interface.
     */
    private void initButtons() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;

        // Bouton pour effectuer l'échange
        tradeButton = new GuiButton(
                width / 2.0f - buttonWidth - 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Échanger"
        );
        tradeButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        tradeButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        tradeButton.setOnClick(button -> performTrade());

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
        cancelButton.setOnClick(button -> valtherion.closeMerchantGui());

        // Ajouter les boutons à la GUI
        addElement(tradeButton);
        addElement(cancelButton);
    }

    /**
     * Effectue l'échange sélectionné.
     */
    private void performTrade() {
        if (selectedTrade == null) {
            return;
        }

        // Vérifier si le joueur a assez d'objets pour l'échange
        if (playerInventory.hasItem(selectedTrade.getInput().getItemId(), selectedTrade.getInput().getCount())) {
            // Retirer les objets du joueur
            playerInventory.removeItem(selectedTrade.getInput().getItemId(), selectedTrade.getInput().getCount());

            // Ajouter les objets au joueur
            playerInventory.addItem(selectedTrade.getOutput().getItemId(), selectedTrade.getOutput().getCount());

            // Fermer l'interface
            valtherion.closeMerchantGui();
        }
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le nom du marchand
        drawMerchantName();

        // Dessiner les échanges
        drawTrades();

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
     * Dessine le nom du marchand.
     */
    private void drawMerchantName() {
        // Dessiner un rectangle pour simuler le nom du marchand
        float nameWidth = 150.0f;
        float nameHeight = 20.0f;
        float nameX = (width - nameWidth) / 2.0f;
        float nameY = height - 30.0f;

        GL11.glColor4f(1.0f, 0.8f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(nameX, nameY);
        GL11.glVertex2f(nameX + nameWidth, nameY);
        GL11.glVertex2f(nameX + nameWidth, nameY + nameHeight);
        GL11.glVertex2f(nameX, nameY + nameHeight);
        GL11.glEnd();
    }

    /**
     * Dessine les échanges disponibles.
     */
    private void drawTrades() {
        float startX = 50.0f;
        float startY = height / 2.0f + 50.0f;
        float tradeHeight = 40.0f;

        for (int i = 0; i < trades.size(); i++) {
            MerchantTrade trade = trades.get(i);

            // Dessiner l'échange
            float y = startY - i * tradeHeight;

            // Couleur en fonction de si l'échange est sélectionné
            if (selectedTradeIndex == i) {
                GL11.glColor4f(0.2f, 0.6f, 1.0f, 1.0f);
            } else {
                GL11.glColor4f(0.5f, 0.5f, 0.8f, 1.0f);
            }

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(startX, y);
            GL11.glVertex2f(startX + 300.0f, y);
            GL11.glVertex2f(startX + 300.0f, y + tradeHeight);
            GL11.glVertex2f(startX, y + tradeHeight);
            GL11.glEnd();
        }
    }

    /**
     * Gère les entrées souris pour sélectionner un échange.
     * @param button Bouton de la souris.
     * @param action Action (GLFW_PRESS, GLFW_RELEASE, etc.).
     * @param x Position X de la souris.
     * @param y Position Y de la souris.
     */
    @Override
    public void handleMouseInput(int button, int action, double x, double y) {
        super.handleMouseInput(button, action, x, y);

        if (action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            float startX = 50.0f;
            float startY = height / 2.0f + 50.0f;
            float tradeHeight = 40.0f;

            for (int i = 0; i < trades.size(); i++) {
                float yPos = startY - i * tradeHeight;

                if (x >= startX && x <= startX + 300.0f && y >= yPos && y <= yPos + tradeHeight) {
                    selectedTradeIndex = i;
                    selectedTrade = trades.get(i);
                    break;
                }
            }
        }
    }

    // --- Getters et Setters ---

    public Inventory getMerchantInventory() {
        return merchantInventory;
    }

    public void setMerchantInventory(Inventory merchantInventory) {
        this.merchantInventory = merchantInventory;
    }

    public Inventory getPlayerInventory() {
        return playerInventory;
    }

    public void setPlayerInventory(Inventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    public List<MerchantTrade> getTrades() {
        return trades;
    }

    public MerchantTrade getSelectedTrade() {
        return selectedTrade;
    }

    public void setSelectedTrade(MerchantTrade selectedTrade) {
        this.selectedTrade = selectedTrade;
    }

    public int getSelectedTradeIndex() {
        return selectedTradeIndex;
    }

    public void setSelectedTradeIndex(int selectedTradeIndex) {
        this.selectedTradeIndex = selectedTradeIndex;
        this.selectedTrade = trades.get(selectedTradeIndex);
    }

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        trades.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiMerchant{" +
                "trades=" + trades.size() +
                ", selectedTradeIndex=" + selectedTradeIndex +
                '}';
    }

    // --- Classe interne pour les échanges ---

    /**
     * Classe pour représenter un échange de marchand.
     */
    public static class MerchantTrade {
        private ItemStack input;
        private ItemStack output;

        public MerchantTrade(ItemStack input, ItemStack output) {
            this.input = input;
            this.output = output;
        }

        // Getters et Setters
        public ItemStack getInput() { return input; }
        public void setInput(ItemStack input) { this.input = input; }
        public ItemStack getOutput() { return output; }
        public void setOutput(ItemStack output) { this.output = output; }
    }
}
