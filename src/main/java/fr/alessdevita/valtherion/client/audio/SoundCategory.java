package fr.alessdevita.valtherion.client.audio;

import fr.alessdevita.valtherion.client.settings.GameSettings;

/**
 * Énumération des catégories de sons dans Valtherion.
 * Permet de contrôler le volume de chaque type de son séparément.
 */
public enum SoundCategory {
    MASTER,       // Volume principal (affecte toutes les catégories)
    MUSIC,        // Musique de fond
    BLOCKS,       // Sons des blocs (placement, destruction)
    ENTITIES,     // Sons des entités (mobs, joueurs)
    AMBIENT,      // Sons ambiants (vent, pluie)
    WEATHER,      // Sons météorologiques (tonnerre, pluie)
    PLAYERS,      // Sons des joueurs (pas, sauts)
    UI,           // Sons de l'interface utilisateur (clics, notifications)
    RECORDS,      // Sons des disques (jukebox)
    VOICE         // Sons vocaux (dialogues, narrations)
    ;
    public static final GameSettings.Options SOUND = GameSettings.Options.SOUND;
    public static final GameSettings.Options FOV_SETTING = GameSettings.Options.FOV_SETTING;
    public static final GameSettings.Options GAMMA = GameSettings.Options.GAMMA;
    public static final GameSettings.Options DIFFICULTY = GameSettings.Options.DIFFICULTY;
    public static final GameSettings.Options GUI_SCALE = GameSettings.Options.GUI_SCALE;
    public static final GameSettings.Options GRAPHICS_FANCY = GameSettings.Options.GRAPHICS_FANCY;
    public static final GameSettings.Options RENDER_DISTANCE = GameSettings.Options.RENDER_DISTANCE;
    public static final GameSettings.Options AMBIENT_OCCLUSION = GameSettings.Options.AMBIENT_OCCLUSION;
    public static final GameSettings.Options FXAA = GameSettings.Options.FXAA;
    public static final GameSettings.Options BRIGHTNESS = GameSettings.Options.BRIGHTNESS;
    public static final GameSettings.Options ANAGLYPH = GameSettings.Options.ANAGLYPH;
    public static final GameSettings.Options FRAMERATE_LIMIT = GameSettings.Options.FRAMERATE_LIMIT;
    public static final GameSettings.Options VIEW_BOBBING = GameSettings.Options.VIEW_BOBBING;
    public static final GameSettings.Options CLOUDS = GameSettings.Options.CLOUDS;
    public static final GameSettings.Options PARTICLES = GameSettings.Options.PARTICLES;
    public static final GameSettings.Options USE_VBO = GameSettings.Options.USE_VBO;
    public static final GameSettings.Options SMOOTH_FPS = GameSettings.Options.SMOOTH_FPS;
    public static final GameSettings.Options FAST_RENDER = GameSettings.Options.FAST_RENDER;
    public static final GameSettings.Options TRANSLUCENT_FANCY = GameSettings.Options.TRANSLUCENT_FANCY;
    public static final GameSettings.Options MOBS = GameSettings.Options.MOBS;
    public static final GameSettings.Options ANIMALS = GameSettings.Options.ANIMALS;
    public static final GameSettings.Options CHAT_VISIBILITY = GameSettings.Options.CHAT_VISIBILITY;
    public static final GameSettings.Options CHAT_COLOR = GameSettings.Options.CHAT_COLOR;
    public static final GameSettings.Options CHAT_LINKS = GameSettings.Options.CHAT_LINKS;
    public static final GameSettings.Options CHAT_OPACITY = GameSettings.Options.CHAT_OPACITY;
    public static final GameSettings.Options CHAT_SCALE = GameSettings.Options.CHAT_SCALE;
    public static final GameSettings.Options CHAT_HEIGHT_FOCUSED = GameSettings.Options.CHAT_HEIGHT_FOCUSED;
    public static final GameSettings.Options CHAT_HEIGHT_UNFOCUSED = GameSettings.Options.CHAT_HEIGHT_UNFOCUSED;
}
