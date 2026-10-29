package fr.alessdevita.valtherion.api.events.block;

import fr.alessdevita.valtherion.api.events.CancelableEvent;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.block.Block;

/**
 * Événement déclenché lorsqu'un bloc est cassé.
 * Peut être annulé pour empêcher la destruction du bloc.
 */
public class BlockBreakEvent extends CancelableEvent {

    // Joueur qui casse le bloc
    private final Player player;

    // Bloc qui est cassé
    private final Block block;

    // Coordonnées du bloc
    private final int x, y, z;

    // Type de bloc (ex: "DIAMOND_ORE", "DIRT")
    private final String blockType;

    // Expérience à donner au joueur (optionnel)
    private int experience;

    // Indique si le bloc a déjà été détruit (pour éviter les doublons)
    private boolean isCancelledByServer;

    /**
     * Constructeur de l'événement de destruction de bloc.
     * @param player Joueur qui casse le bloc.
     * @param block Bloc qui est cassé.
     * @param x Coordonnée X du bloc.
     * @param y Coordonnée Y du bloc.
     * @param z Coordonnée Z du bloc.
     */
    public BlockBreakEvent(Player player, Block block, int x, int y, int z) {
        super("BlockBreakEvent");
        this.player = player;
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockType = block != null ? block.getType().name() : "UNKNOWN";
        this.experience = block != null ? block.getExperienceDropped() : 0;
        this.isCancelledByServer = false;
    }

    /**
     * Retourne le joueur qui casse le bloc.
     * @return Joueur.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retourne le bloc qui est cassé.
     * @return Bloc.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Retourne la coordonnée X du bloc.
     * @return Coordonnée X.
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la coordonnée Y du bloc.
     * @return Coordonnée Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Retourne la coordonnée Z du bloc.
     * @return Coordonnée Z.
     */
    public int getZ() {
        return z;
    }

    /**
     * Retourne le type de bloc.
     * @return Type de bloc.
     */
    public String getBlockType() {
        return blockType;
    }

    /**
     * Retourne l'expérience à donner au joueur.
     * @return Expérience.
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Définit l'expérience à donner au joueur.
     * @param experience Expérience.
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Vérifie si le bloc a été détruit par le serveur.
     * @return true si le bloc a été détruit par le serveur.
     */
    public boolean isCancelledByServer() {
        return isCancelledByServer;
    }

    /**
     * Définit si le bloc a été détruit par le serveur.
     * @param cancelledByServer true si le bloc a été détruit par le serveur.
     */
    public void setCancelledByServer(boolean cancelledByServer) {
        isCancelledByServer = cancelledByServer;
    }

    /**
     * Retourne une représentation textuelle de l'événement.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "BlockBreakEvent{" +
                "player=" + (player != null ? player.getName() : "null") +
                ", blockType='" + blockType + '\'' +
                ", position=(" + x + ", " + y + ", " + z + ")" +
                ", experience=" + experience +
                ", cancelled=" + isCancelled() +
                '}';
    }
}