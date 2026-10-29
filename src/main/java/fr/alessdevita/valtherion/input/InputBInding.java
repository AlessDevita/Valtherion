package fr.alessdevita.valtherion.input;

/**
 * Lie une action à une touche ou un bouton.
 */
public class InputBInding {
    private int keyCode;
    private InputManager inputManager;

    public InputBInding(int keyCode, InputManager inputManager) {
        this.keyCode = keyCode;
        this.inputManager = inputManager;
    }

    public boolean isPressed() {
        return inputManager.getKeyboard().isKeyDown(String.valueOf(keyCode));
    }
}