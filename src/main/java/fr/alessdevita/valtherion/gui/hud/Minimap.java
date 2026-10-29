package fr.alessdevita.valtherion.gui.hud;

import fr.alessdevita.valtherion.gui.fonts.FontRenderer;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.world.World;

import org.joml.Vector3f;

/**
 * Composant pour la minimap dans le HUD de Valtherion.
 * Affiche une carte miniaturisée du monde autour du joueur.
 */
public class Minimap extends HUD.HUDElement {

    // Joueur associé à la minimap
    private final Player player;

    // Monde associé à la minimap
    private final World world;

    // Taille de la zone du monde affichée sur la minimap (en blocs)
    private int mapSize;

    // Échelle de la minimap
    private float scale;

    // Couleur de fond de la minimap
    private Vector3f backgroundColor;

    // Couleur du joueur sur la minimap
    private Vector3f playerColor;

    // Couleur des entités sur la minimap
    private Vector3f entityColor;

    // Couleur des blocs solides sur la minimap
    private Vector3f solidBlockColor;

    // Couleur des blocs liquides sur la minimap
    private Vector3f liquidBlockColor;

    // Indique si la minimap est en mode radar (affichage des entités uniquement)
    private boolean radarMode;

    /**
     * Constructeur de la minimap.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param width Largeur de la minimap.
     * @param height Hauteur de la minimap.
     * @param player Joueur associé.
     * @param world Monde associé.
     */
    public Minimap(float x, float y, float width, float height, Player player, World world) {
        super(x, y, width, height);
        this.player = player;
        this.world = world;
        this.mapSize = 100; // 100 blocs autour du joueur
        this.scale = width / mapSize;
        this.backgroundColor = new Vector3f(0.1f, 0.1f, 0.1f); // Gris très foncé
        this.playerColor = new Vector3f(1.0f, 0.0f, 0.0f); // Rouge
        this.entityColor = new Vector3f(0.0f, 1.0f, 0.0f); // Vert
        this.solidBlockColor = new Vector3f(0.5f, 0.5f, 0.5f); // Gris
        this.liquidBlockColor = new Vector3f(0.0f, 0.0f, 1.0f); // Bleu
        this.radarMode = false;
    }

    /**
     * Met à jour la minimap.
     */
    @Override
    public void update() {
        // Pas de mise à jour nécessaire pour la minimap
    }

    /**
     * Rend la minimap.
     * @param fontRenderer Rendu des textes.
     */
    @Override
    public void render(FontRenderer fontRenderer) {
        // Logique pour rendre la minimap
        System.out.println("Rendu de la minimap");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Taille : " + width + "x" + height);

        // Rendre le fond de la minimap
        System.out.println("Couleur de fond : " + backgroundColor);

        // Rendre le joueur au centre de la minimap
        float playerX = x + width / 2;
        float playerY = y + height / 2;
        System.out.println("Position du joueur sur la minimap : (" + playerX + ", " + playerY + ")");
        System.out.println("Couleur du joueur : " + playerColor);

        // Rendre les entités autour du joueur
        if (!radarMode) {
            renderTerrain();
        }

        renderEntities();

        // Rendre le texte de la minimap
        String minimapText = "Minimap";
        fontRenderer.renderText(
                minimapText,
                x,
                y - 20,
                0.8f,
                new Vector3f(1.0f, 1.0f, 1.0f)
        );
    }

    /**
     * Rend le terrain autour du joueur.
     */
    private void renderTerrain() {
        // Logique pour rendre le terrain sur la minimap
        // Exemple : Parcourir les blocs autour du joueur et les afficher
        int playerBlockX = (int) player.getX();
        int playerBlockY = (int) player.getY();
        int playerBlockZ = (int) player.getZ();

        for (int dx = -mapSize / 2; dx <= mapSize / 2; dx++) {
            for (int dz = -mapSize / 2; dz <= mapSize / 2; dz++) {
                int blockX = playerBlockX + dx;
                int blockZ = playerBlockZ + dz;

                // Récupérer le bloc à cette position
                // Exemple : Block block = world.getBlock(blockX, playerBlockY, blockZ);

                // Afficher le bloc sur la minimap
                // Exemple : Rendre un carré à la position (x + dx * scale, y + dz * scale)
            }
        }
    }

    /**
     * Rend les entités autour du joueur.
     */
    private void renderEntities() {
        // Logique pour rendre les entités sur la minimap
        float playerX = (float) player.getX();
        float playerZ = (float) player.getZ();

        for (fr.alessdevita.valtherion.entity.Entity entity : world.getEntities()) {
            if (entity == player) {
                continue; // Ignorer le joueur
            }

            float entityX = (float) entity.getX();
            float entityZ = (float) entity.getZ();

            // Calculer la position relative de l'entité par rapport au joueur
            float relativeX = entityX - playerX;
            float relativeZ = entityZ - playerZ;

            // Vérifier si l'entité est dans la zone de la minimap
            if (Math.abs(relativeX) <= mapSize / 2 && Math.abs(relativeZ) <= mapSize / 2) {
                // Calculer la position de l'entité sur la minimap
                float minimapEntityX = x + width / 2 + relativeX * scale;
                float minimapEntityY = y + height / 2 + relativeZ * scale;

                // Rendre l'entité sur la minimap
                System.out.println("Rendu de l'entité sur la minimap : (" + minimapEntityX + ", " + minimapEntityY + ")");
                System.out.println("Couleur de l'entité : " + entityColor);
            }
        }
    }

    // --- Getters et Setters ---

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
        this.scale = width / mapSize;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Vector3f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Vector3f getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Vector3f playerColor) {
        this.playerColor = playerColor;
    }

    public Vector3f getEntityColor() {
        return entityColor;
    }

    public void setEntityColor(Vector3f entityColor) {
        this.entityColor = entityColor;
    }

    public Vector3f getSolidBlockColor() {
        return solidBlockColor;
    }

    public void setSolidBlockColor(Vector3f solidBlockColor) {
        this.solidBlockColor = solidBlockColor;
    }

    public Vector3f getLiquidBlockColor() {
        return liquidBlockColor;
    }

    public void setLiquidBlockColor(Vector3f liquidBlockColor) {
        this.liquidBlockColor = liquidBlockColor;
    }

    public boolean isRadarMode() {
        return radarMode;
    }

    public void setRadarMode(boolean radarMode) {
        this.radarMode = radarMode;
    }
}