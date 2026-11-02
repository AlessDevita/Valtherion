package fr.alessdevita.valtherion.world.demo;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.WorldSettings;
import fr.alessdevita.valtherion.world.WorldType;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire de monde de démonstration pour Valtherion.
 * Crée et gère un monde simplifié destiné à la démonstration du moteur.
 * Fournit des méthodes pratiques pour initialiser rapidement un environnement de test.
 */
public class DemoWorldManager {

    /**
     * Monde de démonstration actuel.
     */
    private DemoWorldServer demoWorld;

    /**
     * Indique si le monde de démonstration est initialisé.
     */
    private boolean initialized;

    /**
     * Paramètres par défaut du monde de démonstration.
     */
    private WorldSettings defaultSettings;

    /**
     * Constructeur avec les paramètres par défaut.
     * Crée un monde normal avec génération de structures.
     */
    public DemoWorldManager() {
        this(new WorldSettings(42L, WorldType.NORMAL, true, false));
    }

    /**
     * Constructeur avec des paramètres personnalisés.
     *
     * @param settings Paramètres du monde de démonstration.
     */
    public DemoWorldManager(WorldSettings settings) {
        this.defaultSettings = settings != null ? settings : new WorldSettings(42L);
        this.initialized = false;
        this.demoWorld = null;
    }

    /**
     * Initialise le monde de démonstration.
     * Crée le monde serveur de démonstration avec les paramètres configurés.
     *
     * @return Le monde de démonstration initialisé.
     */
    public DemoWorldServer initialize() {
        if (initialized && demoWorld != null) {
            return demoWorld;
        }

        demoWorld = new DemoWorldServer("DemoWorld", defaultSettings);
        demoWorld.init();
        initialized = true;

        return demoWorld;
    }

    /**
     * Retourne le monde de démonstration.
     * Si le monde n'est pas encore initialisé, l'initialise automatiquement.
     *
     * @return Le monde de démonstration.
     */
    public DemoWorldServer getDemoWorld() {
        if (!initialized) {
            initialize();
        }
        return demoWorld;
    }

    /**
     * Retourne le monde de démonstration en tant que World générique.
     *
     * @return Le monde de démonstration ou null si non initialisé.
     */
    public World getWorld() {
        return demoWorld;
    }

    /**
     * Réinitialise le monde de démonstration.
     * Ferme le monde actuel et crée un nouveau monde avec les paramètres par défaut.
     *
     * @return Le nouveau monde de démonstration.
     */
    public DemoWorldServer reset() {
        if (demoWorld != null) {
            demoWorld.cleanup();
        }
        initialized = false;
        return initialize();
    }

    /**
     * Réinitialise le monde de démonstration avec de nouveaux paramètres.
     *
     * @param settings Nouveaux paramètres du monde.
     * @return Le nouveau monde de démonstration.
     */
    public DemoWorldServer reset(WorldSettings settings) {
        this.defaultSettings = settings != null ? settings : new WorldSettings(42L);
        return reset();
    }

    /**
     * Ferme le monde de démonstration et libère les ressources.
     */
    public void shutdown() {
        if (demoWorld != null) {
            demoWorld.cleanup();
            demoWorld = null;
        }
        initialized = false;
    }

    /**
     * Retourne true si le monde de démonstration est initialisé.
     *
     * @return true si initialisé.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Retourne les paramètres par défaut du monde de démonstration.
     *
     * @return Les paramètres par défaut.
     */
    public WorldSettings getDefaultSettings() {
        return defaultSettings;
    }

    /**
     * Définit les paramètres par défaut du monde de démonstration.
     *
     * @param settings Nouveaux paramètres par défaut.
     */
    public void setDefaultSettings(WorldSettings settings) {
        this.defaultSettings = settings != null ? settings : new WorldSettings(42L);
    }

    /**
     * Effectue un tick sur le monde de démonstration.
     * Si le monde n'est pas initialisé, l'initialise d'abord.
     *
     * @param deltaTime Temps écoulé depuis le dernier tick en secondes.
     */
    public void tick(float deltaTime) {
        if (!initialized) {
            initialize();
        }
        if (demoWorld != null) {
            demoWorld.update(deltaTime);
        }
    }

    /**
     * Retourne une représentation textuelle du gestionnaire.
     *
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "DemoWorldManager{" +
                "world=" + (demoWorld != null ? demoWorld.getName() : "null") +
                ", initialized=" + initialized +
                ", settings=" + defaultSettings +
                '}';
    }
}
