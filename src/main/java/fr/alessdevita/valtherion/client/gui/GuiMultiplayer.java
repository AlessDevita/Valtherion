package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Interface multijoueur dans Valtherion.
 * Permet de gérer la liste des serveurs et de se connecter à un serveur.
 */
public class GuiMultiplayer extends Gui {

    // Liste des serveurs
    private final List<ServerInfo> serverList;

    // Serveur sélectionné
    private ServerInfo selectedServer;

    // Bouton pour se connecter
    private GuiButton connectButton;

    // Bouton pour ajouter un serveur
    private GuiButton addServerButton;

    // Bouton pour supprimer un serveur
    private GuiButton removeServerButton;

    // Bouton pour actualiser la liste des serveurs
    private GuiButton refreshButton;

    // Bouton pour revenir au menu principal
    private GuiButton backButton;

    // Champ de texte pour l'adresse du serveur
    private GuiTextField serverAddressField;

    /**
     * Constructeur de l'interface multijoueur.
     * @param valtherion Instance du jeu Valtherion.
     * @param width Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     */
    public GuiMultiplayer(Valtherion valtherion, int width, int height) {
        super(valtherion, width, height);
        this.serverList = new ArrayList<>();
        this.selectedServer = null;

        // Initialiser les serveurs par défaut
        initDefaultServers();

        // Initialiser les boutons
        initButtons();

        // Initialiser le champ de texte
        initTextField();
    }

    @Override
    public void init() {
        super.init();
    }

    /**
     * Initialise les serveurs par défaut.
     */
    private void initDefaultServers() {
        serverList.add(new ServerInfo("Serveur Local", "127.0.0.1", 25565));
        serverList.add(new ServerInfo("Serveur Valtherion", "valtherion.fr", 25565));
    }

    /**
     * Initialise les boutons de l'interface.
     */
    private void initButtons() {
        float buttonWidth = 150.0f;
        float buttonHeight = 30.0f;

        // Bouton pour se connecter
        connectButton = new GuiButton(
                width / 2.0f - buttonWidth - 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Se Connecter"
        );
        connectButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.2f, 1.0f));
        connectButton.setHoverColor(new Vector4f(0.4f, 0.8f, 0.4f, 1.0f));
        connectButton.setOnClick(button -> connectToSelectedServer());

        // Bouton pour ajouter un serveur
        addServerButton = new GuiButton(
                width / 2.0f + 20.0f,
                height - 50.0f,
                buttonWidth,
                buttonHeight,
                "Ajouter"
        );
        addServerButton.setNormalColor(new Vector4f(0.2f, 0.6f, 0.8f, 1.0f));
        addServerButton.setHoverColor(new Vector4f(0.4f, 0.8f, 1.0f, 1.0f));
        addServerButton.setOnClick(button -> addServer());

        // Bouton pour supprimer un serveur
        removeServerButton = new GuiButton(
                width / 2.0f - buttonWidth - 20.0f,
                height - 100.0f,
                buttonWidth,
                buttonHeight,
                "Supprimer"
        );
        removeServerButton.setNormalColor(new Vector4f(0.8f, 0.2f, 0.2f, 1.0f));
        removeServerButton.setHoverColor(new Vector4f(1.0f, 0.4f, 0.4f, 1.0f));
        removeServerButton.setOnClick(button -> removeServer());

        // Bouton pour actualiser la liste des serveurs
        refreshButton = new GuiButton(
                width / 2.0f + 20.0f,
                height - 100.0f,
                buttonWidth,
                buttonHeight,
                "Actualiser"
        );
        refreshButton.setNormalColor(new Vector4f(0.8f, 0.6f, 0.2f, 1.0f));
        refreshButton.setHoverColor(new Vector4f(1.0f, 0.8f, 0.4f, 1.0f));
        refreshButton.setOnClick(button -> refreshServerList());

        // Bouton pour revenir au menu principal
        backButton = new GuiButton(
                width / 2.0f,
                height - 150.0f,
                buttonWidth,
                buttonHeight,
                "Retour"
        );
        backButton.setNormalColor(new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));
        backButton.setHoverColor(new Vector4f(0.7f, 0.7f, 0.7f, 1.0f));
        backButton.setOnClick(button -> valtherion.openMainMenu());

        // Ajouter les boutons à la GUI
        addElement(connectButton);
        addElement(addServerButton);
        addElement(removeServerButton);
        addElement(refreshButton);
        addElement(backButton);
    }

    /**
     * Initialise le champ de texte pour l'adresse du serveur.
     */
    private void initTextField() {
        serverAddressField = new GuiTextField(
                (width - 200.0f) / 2.0f,
                height - 200.0f,
                200.0f,
                20.0f,
                ""
        );

        addElement(serverAddressField);
    }

    /**
     * Se connecte au serveur sélectionné.
     */
    private void connectToSelectedServer() {
        if (selectedServer != null) {
            valtherion.connectToServer(selectedServer.getAddress(), selectedServer.getPort());
        }
    }

    void connectToServer(ServerListEntryNormal entry) {
        if (entry != null) {
            valtherion.connectToServer(entry.serverAddress, 25565);
        }
    }

    /**
     * Ajoute un serveur à la liste.
     */
    private void addServer() {
        String address = serverAddressField.getText();
        if (!address.isEmpty()) {
            // Extraire l'adresse et le port (ex: "127.0.0.1:25565")
            String[] parts = address.split(":");
            String host = parts[0];
            int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 25565;

            serverList.add(new ServerInfo("Nouveau Serveur", host, port));
            serverAddressField.setText("");
        }
    }

    /**
     * Supprime le serveur sélectionné.
     */
    private void removeServer() {
        if (selectedServer != null) {
            serverList.remove(selectedServer);
            selectedServer = null;
        }
    }

    /**
     * Actualise la liste des serveurs.
     */
    private void refreshServerList() {
        // Dans un jeu réel, tu pourrais interroger un serveur maître pour obtenir la liste des serveurs
        System.out.println("Actualisation de la liste des serveurs...");
    }

    @Override
    public void render() {
        // Dessiner le fond
        drawBackground();

        // Dessiner le titre
        drawTitle();

        // Dessiner la liste des serveurs
        drawServerList();

        // Dessiner les boutons et le champ de texte
        super.render();
    }

    /**
     * Dessine le fond de l'interface.
     */
    protected void drawBackground() {
        GL11.glColor4f(0.0f, 0.0f, 0.1f, 0.9f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();
    }

    /**
     * Dessine le titre de l'interface.
     */
    private void drawTitle() {
        // Dessiner un rectangle pour simuler le titre "Multijoueur"
        float titleWidth = 150.0f;
        float titleHeight = 20.0f;
        float titleX = (width - titleWidth) / 2.0f;
        float titleY = height - 30.0f;

        GL11.glColor4f(1.0f, 0.8f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(titleX, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY);
        GL11.glVertex2f(titleX + titleWidth, titleY + titleHeight);
        GL11.glVertex2f(titleX, titleY + titleHeight);
        GL11.glEnd();
    }

    /**
     * Dessine la liste des serveurs.
     */
    private void drawServerList() {
        float startX = 50.0f;
        float startY = height / 2.0f + 50.0f;
        float serverHeight = 30.0f;

        for (int i = 0; i < serverList.size(); i++) {
            ServerInfo server = serverList.get(i);

            // Dessiner le serveur
            float y = startY - i * serverHeight;

            // Couleur en fonction de si le serveur est sélectionné
            if (selectedServer == server) {
                GL11.glColor4f(0.2f, 0.6f, 1.0f, 1.0f);
            } else {
                GL11.glColor4f(0.5f, 0.5f, 0.8f, 1.0f);
            }

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(startX, y);
            GL11.glVertex2f(startX + 300.0f, y);
            GL11.glVertex2f(startX + 300.0f, y + serverHeight);
            GL11.glVertex2f(startX, y + serverHeight);
            GL11.glEnd();
        }
    }

    /**
     * Gère les entrées souris pour sélectionner un serveur.
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
            float serverHeight = 30.0f;

            for (int i = 0; i < serverList.size(); i++) {
                float yPos = startY - i * serverHeight;

                if (x >= startX && x <= startX + 300.0f && y >= yPos && y <= yPos + serverHeight) {
                    selectedServer = serverList.get(i);
                    break;
                }
            }
        }
    }

    // --- Getters et Setters ---

    public List<ServerInfo> getServerList() {
        return serverList;
    }

    public ServerInfo getSelectedServer() {
        return selectedServer;
    }

    public void setSelectedServer(ServerInfo selectedServer) {
        this.selectedServer = selectedServer;
    }

    public GuiTextField getServerAddressField() {
        return serverAddressField;
    }

    /**
     * Nettoie les ressources de l'interface.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        serverList.clear();
    }

    /**
     * Retourne une représentation textuelle de l'interface.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "GuiMultiplayer{" +
                "serverList=" + serverList.size() +
                ", selectedServer=" + (selectedServer != null ? selectedServer.getName() : "null") +
                '}';
    }

    public void setSelectedServerIndex(int slotIndex) {

    }

    // --- Classe interne pour les informations du serveur ---

    /**
     * Classe pour représenter les informations d'un serveur.
     */
    public static class ServerInfo {
        private String name;
        private String address;
        private int port;

        public ServerInfo(String name, String address, int port) {
            this.name = name;
            this.address = address;
            this.port = port;
        }

        // Getters et Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
    }

    // --- Classe interne pour le champ de texte ---

    /**
     * Classe pour les champs de texte de la GUI.
     */
    public class GuiTextField extends GuiElement {
        private String text;
        private boolean isSelected;
        private Vector4f textColor;
        private Vector4f backgroundColor;

        public GuiTextField(float x, float y, float width, float height, String text) {
            super(x, y, width, height);
            this.text = text;
            this.isSelected = false;
            this.textColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.backgroundColor = new Vector4f(0.2f, 0.2f, 0.2f, 0.8f);
        }

        @Override
        public void render() {
            // Dessiner le fond du champ
            GL11.glColor4f(
                    backgroundColor.x,
                    backgroundColor.y,
                    backgroundColor.z,
                    backgroundColor.w
            );
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(position.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y);
            GL11.glVertex2f(position.x + size.x, position.y + size.y);
            GL11.glVertex2f(position.x, position.y + size.y);
            GL11.glEnd();

            // Dessiner le texte (simplifié)
            GL11.glColor4f(textColor.x, textColor.y, textColor.z, textColor.w);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(position.x + 5.0f, position.y + 5.0f);
            GL11.glVertex2f(position.x + 5.0f + text.length() * 5.0f, position.y + 5.0f);
            GL11.glVertex2f(position.x + 5.0f + text.length() * 5.0f, position.y + 15.0f);
            GL11.glVertex2f(position.x + 5.0f, position.y + 15.0f);
            GL11.glEnd();
        }

        // Getters et Setters
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public boolean isSelected() { return isSelected; }
        public void setSelected(boolean selected) { this.isSelected = selected; }
    }
}