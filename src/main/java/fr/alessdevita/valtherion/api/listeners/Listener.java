package fr.alessdevita.valtherion.api.listeners;

import fr.alessdevita.valtherion.api.events.Event;

/**
 * Écouteur d'événements pour les plugins Valtherion.
 * Les plugins peuvent implémenter cette interface pour réagir aux événements du jeu.
 */
public interface Listener {

    /**
     * Méthode appelée lorsqu'un événement est déclenché.
     * @param event Événement déclenché.
     */
    void onEvent(Event event);
}