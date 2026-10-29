package fr.alessdevita.valtherion.client.settings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyBinding {
    private static final Map<Integer, Boolean> KEY_STATES = new ConcurrentHashMap<Integer, Boolean>();
    private static final Map<Integer, Integer> PRESS_COUNTS = new ConcurrentHashMap<Integer, Integer>();
    private final int keyCode;

    public KeyBinding() {
        this(0);
    }

    public KeyBinding(int keyCode) {
        this.keyCode = keyCode;
    }

    public static void setKeyBindState(int key, boolean b) {
        KEY_STATES.put(key, b);
    }

    public static void onTickKey(int key) {
        onTick(key);
    }

    public static void onTick(int key) {
        PRESS_COUNTS.put(key, PRESS_COUNTS.getOrDefault(key, 0) + 1);
    }

    public boolean isPressed() {
        Integer count = PRESS_COUNTS.get(this.keyCode);
        if (count == null || count == 0) {
            return false;
        }
        if (count == 1) {
            PRESS_COUNTS.remove(this.keyCode);
        } else {
            PRESS_COUNTS.put(this.keyCode, count - 1);
        }
        return true;
    }

    public boolean getIsKeyPressed() {
        return KEY_STATES.getOrDefault(this.keyCode, false);
    }

    public boolean isKeyDown() { return getIsKeyPressed(); }

    public int getKeyCode() {
        return this.keyCode;
    }
}
