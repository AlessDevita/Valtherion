package fr.alessdevita.valtherion.client.settings;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import fr.alessdevita.valtherion.client.Valtherion;

public class GameSettings {
    public static final class Options {
        public static final Options MASTER = new Options("Master");
        public static final Options MUSIC = new Options("Music");
        public static final Options RECORDS = new Options("Records");
        public static final Options WEATHER = new Options("Weather");
        public static final Options BLOCKS = new Options("Blocks");
        public static final Options MOBS = new Options("Mobs");
        public static final Options ANIMALS = new Options("Animals");
        public static final Options PLAYERS = new Options("Players");
        public static final Options AMBIENT = new Options("Ambient");
        public static final Options SOUND = new Options("Sound");
        public static final Options FOV_SETTING = new Options("FOV");
        public static final Options GAMMA = new Options("Gamma");
        public static final Options DIFFICULTY = new Options("Difficulty");
        public static final Options GUI_SCALE = new Options("GUI Scale");
        public static final Options GRAPHICS_FANCY = new Options("Graphics");
        public static final Options RENDER_DISTANCE = new Options("Render Distance");
        public static final Options AMBIENT_OCCLUSION = new Options("Ambient Occlusion");
        public static final Options FXAA = new Options("FXAA");
        public static final Options BRIGHTNESS = new Options("Brightness");
        public static final Options ANAGLYPH = new Options("Anaglyph");
        public static final Options FRAMERATE_LIMIT = new Options("Framerate");
        public static final Options VIEW_BOBBING = new Options("View Bobbing");
        public static final Options CLOUDS = new Options("Clouds");
        public static final Options PARTICLES = new Options("Particles");
        public static final Options USE_VBO = new Options("Use VBO");
        public static final Options SMOOTH_FPS = new Options("Smooth FPS");
        public static final Options FAST_RENDER = new Options("Fast Render");
        public static final Options TRANSLUCENT_FANCY = new Options("Translucent");
        public static final Options CHAT_VISIBILITY = new Options("Chat Visibility");
        public static final Options CHAT_COLOR = new Options("Chat Color");
        public static final Options CHAT_LINKS = new Options("Chat Links");
        public static final Options CHAT_OPACITY = new Options("Chat Opacity");
        public static final Options CHAT_SCALE = new Options("Chat Scale");
        public static final Options CHAT_HEIGHT_FOCUSED = new Options("Focused Height");
        public static final Options CHAT_HEIGHT_UNFOCUSED = new Options("Unfocused Height");

        private final String enumString;

        private Options(String enumString) {
            this.enumString = enumString;
        }

        public String getEnumString() {
            return this.enumString;
        }
    }

    private static final Map<Options, Float> FLOAT_VALUES = new HashMap<>();
    private static final Map<Options, Boolean> BOOLEAN_VALUES = new HashMap<>();

    public boolean enableVsync;
    public boolean fullScreen;
    public boolean hideGUI;
    public boolean showDebugInfo;
    public boolean smoothCamera;
    public boolean pauseOnLostFocus = true;
    public boolean fancyGraphics = true;
    public int ambientOcclusion = 1;
    public int thirdPersonView;
    public int limitFramerate = 120;
    public int overrideHeight;
    public int overrideWidth;
    public boolean snooperEnabled;
    public int guiScale;
    public final KeyBinding keyBindInventory = new KeyBinding(69);
    public final KeyBinding keyBindForward = new KeyBinding(87);
    public final KeyBinding keyBindBack = new KeyBinding(83);
    public final KeyBinding keyBindLeft = new KeyBinding(65);
    public final KeyBinding keyBindRight = new KeyBinding(68);
    public final KeyBinding keyBindJump = new KeyBinding(32);
    public final KeyBinding keyBindSneak = new KeyBinding(340);
    public final KeyBinding keyBindTogglePerspective = new KeyBinding(294);
    public final KeyBinding keyBindSmoothCamera = new KeyBinding(0);
    public final KeyBinding keyBindDrop = new KeyBinding(81);
    public final KeyBinding keyBindChat = new KeyBinding(84);
    public final KeyBinding keyBindCommand = new KeyBinding(47);
    public final KeyBinding keyBindUseItem = new KeyBinding(-99);
    public final KeyBinding keyBindAttack = new KeyBinding(-100);
    public final KeyBinding keyBindPickBlock = new KeyBinding(-98);
    public final KeyBinding[] keyBindsHotbar = new KeyBinding[9];

    public GameSettings(Valtherion valtherion, File vDataDir) {
        this.enableVsync = false;
        this.overrideHeight = 720;
        this.overrideWidth = 1280;
        this.snooperEnabled = false;
        this.guiScale = 2;
        for (int slot = 0; slot < this.keyBindsHotbar.length; slot++) {
            this.keyBindsHotbar[slot] = new KeyBinding(49 + slot);
        }
    }

    public void setOptionValue(Options option) {
        if (option == null) {
            return;
        }
        Boolean value = BOOLEAN_VALUES.get(option);
        if (value == null) {
            float current = getOptionFloatValue(option);
            setOptionFloatValue(option, Math.min(1.0F, Math.max(0.0F, current + 0.1F)));
            return;
        }
        BOOLEAN_VALUES.put(option, !value);
    }

    public void notifySettingsChanged() {
    }

    public void saveOptions() {
    }

    public static Object getKeyBinding(Options enumOptions) {
        return enumOptions == null ? "" : enumOptions.getEnumString();
    }

    public static float getOptionFloatValue(Options option) {
        if (option == null) {
            return 0.0F;
        }
        Float value = FLOAT_VALUES.get(option);
        if (value == null) {
            value = 0.5F;
            FLOAT_VALUES.put(option, value);
        }
        return value;
    }

    public static void setOptionFloatValue(Options option, float v) {
        if (option != null) {
            FLOAT_VALUES.put(option, Math.max(0.0F, Math.min(1.0F, v)));
        }
    }
}
