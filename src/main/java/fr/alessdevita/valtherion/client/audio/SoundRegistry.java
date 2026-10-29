package fr.alessdevita.valtherion.client.audio;

import java.util.*;

/**
 * Registre des sons dans Valtherion.
 * Permet d'enregistrer, de récupérer et de gérer les sons par ID ou par nom.
 */
public class SoundRegistry {

    // Instance unique du registre des sons
    private static SoundRegistry instance;

    // Map des sons par ID (ID -> ISound)
    private final Map<Integer, ISound> soundsById;

    // Map des sons par nom (nom -> ISound)
    private final Map<String, ISound> soundsByName;

    // Pool de sons (ID -> SoundPoolEntry)
    private final Map<Integer, SoundPoolEntry> soundPool;

    // Compteur pour générer des IDs uniques
    private int idCounter;

    /**
     * Constructeur privé pour le singleton.
     */
    private SoundRegistry() {
        this.soundsById = new HashMap<>();
        this.soundsByName = new HashMap<>();
        this.soundPool = new HashMap<>();
        this.idCounter = 0;
    }

    /**
     * Récupère l'instance unique du registre des sons.
     * @return Instance unique.
     */
    public static SoundRegistry getInstance() {
        if (instance == null) {
            instance = new SoundRegistry();
        }
        return instance;
    }

    /**
     * Enregistre un son avec un ID unique.
     * @param sound Son à enregistrer.
     * @return ID du son.
     */
    public int registerSound(ISound sound) {
        int id = idCounter++;
        soundsById.put(id, sound);
        soundsByName.put(sound.getSoundPath(), sound);
        return id;
    }

    /**
     * Enregistre un son avec un nom spécifique.
     * @param name Nom du son.
     * @param sound Son à enregistrer.
     * @return ID du son.
     */
    public int registerSound(String name, ISound sound) {
        int id = idCounter++;
        soundsById.put(id, sound);
        soundsByName.put(name, sound);
        return id;
    }

    /**
     * Récupère un son par ID.
     * @param id ID du son.
     * @return Son ou null si non trouvé.
     */
    public ISound getSoundById(int id) {
        return soundsById.get(id);
    }

    /**
     * Récupère un son par nom.
     * @param name Nom du son.
     * @return Son ou null si non trouvé.
     */
    public ISound getSoundByName(String name) {
        return soundsByName.get(name);
    }

    /**
     * Ajoute un son au pool.
     * @param sound Son à ajouter.
     * @return Entrée dans le pool de sons.
     */
    public SoundPoolEntry addToPool(ISound sound) {
        SoundPoolEntry entry = new SoundPoolEntry(sound);
        soundPool.put(entry.getId(), entry);
        return entry;
    }

    /**
     * Récupère une entrée du pool par ID.
     * @param id ID de l'entrée.
     * @return Entrée dans le pool ou null si non trouvée.
     */
    public SoundPoolEntry getPoolEntry(int id) {
        return soundPool.get(id);
    }

    /**
     * Supprime une entrée du pool.
     * @param id ID de l'entrée.
     */
    public void removeFromPool(int id) {
        SoundPoolEntry entry = soundPool.remove(id);
        if (entry != null) {
            entry.stop();
        }
    }

    /**
     * Supprime un son par ID.
     * @param id ID du son.
     */
    public void unregisterSound(int id) {
        ISound sound = soundsById.remove(id);
        if (sound != null) {
            soundsByName.values().remove(sound);
        }
    }

    /**
     * Supprime un son par nom.
     * @param name Nom du son.
     */
    public void unregisterSound(String name) {
        ISound sound = soundsByName.remove(name);
        if (sound != null) {
            soundsById.values().remove(sound);
        }
    }

    /**
     * Supprime tous les sons.
     */
    public void clear() {
        soundsById.clear();
        soundsByName.clear();
        soundPool.clear();
    }

    /**
     * Met à jour tous les sons du pool.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    public void update(float deltaTime) {
        for (SoundPoolEntry entry : soundPool.values()) {
            if (entry.getSound() instanceof ITickableSound) {
                ((ITickableSound) entry.getSound()).tick(deltaTime);
            }
        }
    }

    // --- Getters ---

    public Map<Integer, ISound> getSoundsById() {
        return soundsById;
    }

    public Map<String, ISound> getSoundsByName() {
        return soundsByName;
    }

    public Map<Integer, SoundPoolEntry> getSoundPool() {
        return soundPool;
    }

    /**
     * Retourne une représentation textuelle du registre des sons.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SoundRegistry{" +
                "soundsById=" + soundsById.size() +
                ", soundsByName=" + soundsByName.size() +
                ", soundPool=" + soundPool.size() +
                '}';
    }
}
