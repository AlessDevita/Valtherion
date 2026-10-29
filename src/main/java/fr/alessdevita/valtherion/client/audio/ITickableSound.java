package fr.alessdevita.valtherion.client.audio;

/**
 * Interface pour les sons mis à jour chaque tick dans Valtherion.
 * Permet de mettre à jour la position ou d'autres propriétés du son à chaque tick.
 */
public interface ITickableSound extends ISound {

    /**
     * Met à jour le son.
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (en secondes).
     */
    void tick(float deltaTime);

    /**
     * Définit si le son doit être mis à jour.
     * @param tickable true pour activer la mise à jour.
     */
    void setTickable(boolean tickable);

    /**
     * Récupère si le son est mis à jour.
     * @return true si le son est mis à jour.
     */
    boolean isTickable();
}
