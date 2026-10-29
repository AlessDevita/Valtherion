package fr.alessdevita.valtherion.api.events;

/**
 * Classe de base pour tous les événements de Valtherion.
 * Les événements sont utilisés pour notifier les plugins de certaines actions dans le jeu.
 */
public abstract class Event {
    // Nom de l'événement
    private final String name;

    // État de l'événement (annulé ou non)
    private boolean cancelled;

    /**
     * Constructeur de l'événement.
     * @param name Nom de l'événement.
     */
    public Event(String name) {
        this.name = name;
        this.cancelled = false;
    }

    /**
     * Retourne le nom de l'événement.
     * @return Nom de l'événement.
     */
    public String getName() {
        return name;
    }

    /**
     * Vérifie si l'événement est annulé.
     * @return true si l'événement est annulé, false sinon.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Annule l'événement.
     * @param cancelled true pour annuler l'événement, false sinon.
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Retourne une représentation textuelle de l'événement.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", cancelled=" + cancelled +
                '}';
    }
}
