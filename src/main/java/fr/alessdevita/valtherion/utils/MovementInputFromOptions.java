package fr.alessdevita.valtherion.utils;

import fr.alessdevita.valtherion.client.settings.GameSettings;

/** Reads movement bindings from the current client settings. */
public final class MovementInputFromOptions {
    private final GameSettings settings;
    public float moveStrafe;
    public float moveForward;
    public boolean jump;
    public boolean sneak;
    public MovementInputFromOptions(GameSettings settings) { this.settings = settings; }
    public void updatePlayerMoveState() {
        moveStrafe = (settings.keyBindLeft.isKeyDown() ? 1.0F : 0.0F) - (settings.keyBindRight.isKeyDown() ? 1.0F : 0.0F);
        moveForward = (settings.keyBindForward.isKeyDown() ? 1.0F : 0.0F) - (settings.keyBindBack.isKeyDown() ? 1.0F : 0.0F);
        jump = settings.keyBindJump.isKeyDown();
        sneak = settings.keyBindSneak.isKeyDown();
    }
}
