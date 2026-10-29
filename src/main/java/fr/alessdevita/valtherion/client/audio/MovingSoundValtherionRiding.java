package fr.alessdevita.valtherion.client.audio;

import fr.alessdevita.valtherion.entity.Entity;
import org.joml.Vector3f;

/**
 * Classe pour les sons en mouvement des entités montées dans Valtherion.
 * Étend MovingSoundValtherion pour ajouter des fonctionnalités spécifiques aux entités montées.
 */
public class MovingSoundValtherionRiding extends MovingSoundValtherion {

    // Entité montée (ex: cheval, bateau)
    private Entity ridingEntity;

    // Vitesse de l'entité montée
    private float ridingSpeed;

    /**
     * Constructeur du son en mouvement pour les entités montées.
     * @param soundPath Chemin du fichier son.
     * @param entity Entité associée au son (ex: joueur).
     * @param ridingEntity Entité montée.
     * @param soundType Type de son.
     */
    public MovingSoundValtherionRiding(String soundPath, Entity entity, Entity ridingEntity, SoundType soundType) {
        super(soundPath, entity, soundType);
        this.ridingEntity = ridingEntity;
        this.ridingSpeed = 0.0f;
    }

    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);

        // Mettre à jour le volume en fonction de la vitesse de l'entité montée
        if (ridingEntity != null) {
            Vector3f ridingPosition = ridingEntity.getPosition();
            Vector3f entityPosition = getEntity().getPosition();

            // Calculer la distance entre l'entité et l'entité montée
            float dx = ridingPosition.x - entityPosition.x;
            float dy = ridingPosition.y - entityPosition.y;
            float dz = ridingPosition.z - entityPosition.z;
            float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

            // Ajuster le volume en fonction de la distance
            float volume = 1.0f - Math.min(1.0f, distance / 10.0f);
            setVolume(volume);
        }
    }

    // --- Getters et Setters ---

    public Entity getRidingEntity() {
        return ridingEntity;
    }

    public void setRidingEntity(Entity ridingEntity) {
        this.ridingEntity = ridingEntity;
    }

    public float getRidingSpeed() {
        return ridingSpeed;
    }

    public void setRidingSpeed(float ridingSpeed) {
        this.ridingSpeed = ridingSpeed;
    }

    /**
     * Retourne une représentation textuelle du son en mouvement pour les entités montées.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "MovingSoundValtherionRiding{" +
                "soundPath='" + getSoundPath() + '\'' +
                ", soundType=" + getSoundType() +
                ", ridingEntity=" + (ridingEntity != null ? ridingEntity.getId() : "null") +
                ", ridingSpeed=" + ridingSpeed +
                '}';
    }
}