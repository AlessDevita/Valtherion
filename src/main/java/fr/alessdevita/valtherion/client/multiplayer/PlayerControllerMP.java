package fr.alessdevita.valtherion.client.multiplayer;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.client.entity.EntityClientPlayerMP;
import fr.alessdevita.valtherion.client.network.NetHandlerPlayClient;
import fr.alessdevita.valtherion.stats.StatFileWriter;
import fr.alessdevita.valtherion.world.World;

/** Client-side bridge for player actions that may be sent to a server. */
public class PlayerControllerMP {
    private final Valtherion client;
    private boolean creativeMode;

    public PlayerControllerMP() {
        this(null);
    }

    public PlayerControllerMP(Valtherion client) {
        this.client = client;
    }

    public void updateController() {
    }

    public void resetBlockRemoving() {
    }

    public boolean onPlayerDamageBlock(int x, int y, int z, Object side) {
        return this.client != null && this.client.theWorld != null
                && this.client.theWorld.getBlock(x, y, z) != null;
    }

    public void clickBlock(int x, int y, int z, Object side) {
        onPlayerDamageBlock(x, y, z, side);
    }

    public void attackEntity(EntityClientPlayerMP player, Object target) {
    }

    public boolean interactWithEntitySendPacket(EntityClientPlayerMP player, Object target) {
        return false;
    }

    public boolean onPlayerRightClick(EntityClientPlayerMP player, World world, Object stack,
                                      int x, int y, int z, Object side, Object hitVector) {
        return world != null && world.getBlock(x, y, z) != null;
    }

    public boolean sendUseItem(EntityClientPlayerMP player, World world, Object stack) {
        return stack != null;
    }

    public boolean isNotCreative() {
        return !this.creativeMode;
    }

    public boolean isInCreativeMode() {
        return this.creativeMode;
    }

    public void setCreativeMode(boolean creativeMode) {
        this.creativeMode = creativeMode;
    }

    public void onStoppedUsingItem(EntityClientPlayerMP player) {
    }

    public EntityClientPlayerMP createClientPlayer(WorldClient world, StatFileWriter stats) {
        return new EntityClientPlayerMP("local-player", "Player", world, null);
    }

    public void flipPlayer(EntityClientPlayerMP player) {
    }

    public void setPlayerCapabilities(EntityClientPlayerMP player) {
    }
}
