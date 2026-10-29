package fr.alessdevita.valtherion.api.events.block;

import fr.alessdevita.valtherion.api.events.CancelableEvent;
import fr.alessdevita.valtherion.entity.living.Player;
import fr.alessdevita.valtherion.block.Block;

/**
 * Événement déclenché lorsqu'un bloc est placé.
 * Peut être annulé pour empêcher le placement du bloc.
 */
public class BlockPlaceEvent extends CancelableEvent {

    // Joueur qui place le bloc
    private final Player player;

    // Bloc qui est placé
    private final Block block;

    // Coordonnées du bloc
    private final int x, y, z;

    // Type de bloc (ex: "DIAMOND_BLOCK", "DIRT")
    private final String blockType;

    // Bloc remplacé (si applicable)
    private Block replacedBlock;

    // Indique si le bloc a déjà été placé (pour éviter les doublons)
    private boolean isCancelledByServer;

    /**
     * Constructeur de l'événement de placement de bloc.
     * @param player Joueur qui place le bloc.
     * @param block Bloc qui est placé.
     * @param x Coordonnée X du bloc.
     * @param y Coordonnée Y du bloc.
     * @param z Coordonnée Z du bloc.
     */
    public BlockPlaceEvent(Player player, Block block, int x, int y, int z) {
        super("BlockPlaceEvent");
        this.player = player;
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockType = block != null ? block.getType().name() : "UNKNOWN";
        this.isCancelledByServer = false;
    }

    /**
     * Retourne le joueur qui place le bloc.
     * @return Joueur.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retourne le bloc qui est placé.
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
     * Retourne le bloc remplacé.
     * @return Bloc remplacé.
     */
    public Block getReplacedBlock() {
        return replacedBlock;
    }

    /**
     * Définit le bloc remplacé.
     * @param replacedBlock Bloc remplacé.
     */
    public void setReplacedBlock(Block replacedBlock) {
        this.replacedBlock = replacedBlock;
    }

    /**
     * Vérifie si le bloc a été placé par le serveur.
     * @return true si le bloc a été placé par le serveur.
     */
    public boolean isCancelledByServer() {
        return isCancelledByServer;
    }

    /**
     * Définit si le bloc a été placé par le serveur.
     * @param cancelledByServer true si le bloc a été placé par le serveur.
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
        return "BlockPlaceEvent{" +
                "player=" + (player != null ? player.getName() : "null") +
                ", blockType='" + blockType + '\'' +
                ", position=(" + x + ", " + y + ", " + z + ")" +
                ", replacedBlock=" + (replacedBlock != null ? replacedBlock.getType().name() : "null") +
                ", cancelled=" + isCancelled() +
                '}';
    }
}
