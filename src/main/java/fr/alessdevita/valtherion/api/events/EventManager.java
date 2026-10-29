package fr.alessdevita.valtherion.api.events;

import fr.alessdevita.valtherion.api.listeners.Listener;
import fr.alessdevita.valtherion.core.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestionnaire des événements pour Valtherion.
 * Permet d'enregistrer des écouteurs et de déclencher des événements.
 */
public class EventManager {

    // Logger pour les messages de debug
    private final Logger logger;

    // Map des événements et de leurs écouteurs
    private final Map<Class<? extends Event>, List<Listener>> eventListeners;

    /**
     * Constructeur du gestionnaire des événements.
     */
    public EventManager() {
        this.logger = new Logger("EventManager");
        this.eventListeners = new HashMap<>();
    }

    /**
     * Enregistre un écouteur pour un type d'événement.
     * @param eventClass Classe de l'événement.
     * @param listener Écouteur à enregistrer.
     */
    public void registerListener(Class<? extends Event> eventClass, Listener listener) {
        eventListeners.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(listener);
        logger.debug("Écouteur enregistré pour " + eventClass.getSimpleName());
    }

    /**
     * Désenregistre un écouteur pour un type d'événement.
     * @param eventClass Classe de l'événement.
     * @param listener Écouteur à désenregistrer.
     */
    public void unregisterListener(Class<? extends Event> eventClass, Listener listener) {
        List<Listener> listeners = eventListeners.get(eventClass);
        if (listeners != null) {
            listeners.remove(listener);
            logger.debug("Écouteur désenregistré pour " + eventClass.getSimpleName());
        }
    }

    /**
     * Déclenche un événement.
     * @param event Événement à déclencher.
     */
    public void fireEvent(Event event) {
        if (event == null) {
            logger.warn("Impossible de déclencher un événement nul.");
            return;
        }

        // Récupérer les écouteurs pour ce type d'événement
        List<Listener> listeners = eventListeners.get(event.getClass());
        if (listeners == null || listeners.isEmpty()) {
            return;
        }

        // Notifier chaque écouteur
        for (Listener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                logger.error("Erreur lors du traitement de l'événement " + event.getName() +
                        " par l'écouteur " + listener.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Retourne la liste des écouteurs pour un type d'événement.
     * @param eventClass Classe de l'événement.
     * @return Liste des écouteurs.
     */
    public List<Listener> getListeners(Class<? extends Event> eventClass) {
        return eventListeners.getOrDefault(eventClass, new ArrayList<>());
    }
}