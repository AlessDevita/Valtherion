package fr.alessdevita.valtherion.api.events;

/**
 * Classe de base pour les événements annulables.
 * Permet aux plugins d'annuler certains événements (ex: empêche la destruction d'un bloc).
 */
public abstract class CancelableEvent extends Event {

    /**
     * Constructeur de l'événement annulable.
     * @param name Nom de l'événement.
     */
    public CancelableEvent(String name) {
        super(name);
    }

    /**
     * Annule l'événement.
     */
    public void cancel() {
        setCancelled(true);
    }
}
