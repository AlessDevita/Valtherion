package fr.alessdevita.valtherion.client.audio;

import fr.alessdevita.valtherion.entity.Entity;

/**
 * Classe pour les sons en mouvement spécifiques à Valtherion.
 * Étend MovingSound pour ajouter des fonctionnalités spécifiques au jeu.
 */
public class MovingSoundValtherion extends MovingSound {

    // Type de son (ex: PAS, ATTAQUE, MORT)
    private SoundType soundType;

    // Énumération des types de sons
    public enum SoundType {
        WALK,       // Pas
        RUN,        // Course
        JUMP,       // Saut
        ATTACK,     // Attaque
        HURT,       // Blessure
        DEATH,      // Mort
        AMBIENT,    // Ambiant
        IDLE        // Inactif
    }

    /**
     * Constructeur du son en mouvement spécifique à Valtherion.
     * @param soundPath Chemin du fichier son.
     * @param entity Entité associée au son.
     * @param soundType Type de son.
     */
    public MovingSoundValtherion(String soundPath, Entity entity, SoundType soundType) {
        super(soundPath, entity);
        this.soundType = soundType;
        setSoundCategory(SoundCategory.ENTITIES);
    }

    @Override
    public void play() {
        super.play();
        String soundPath = getSoundPath();
        System.out.println("Jouer le son Valtherion : " + soundPath + " (type : " + soundType + ")");
    }

    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        // Logique spécifique à Valtherion (ex: ajuster le volume en fonction de la distance)
    }

    // --- Getters et Setters ---

    public SoundType getSoundType() {
        return soundType;
    }

    public void setSoundType(SoundType soundType) {
        this.soundType = soundType;
    }

    /**
     * Retourne une représentation textuelle du son en mouvement spécifique à Valtherion.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "MovingSoundValtherion{" +
                "soundPath='" + getSoundPath() + '\'' +
                ", soundType=" + soundType +
                ", volume=" + getVolume() +
                ", pitch=" + getPitch() +
                '}';
    }
}
