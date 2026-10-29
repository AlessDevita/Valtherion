package fr.alessdevita.valtherion.save;

/**
 * Interface pour les objets qui peuvent être sauvegardés dans Valtherion.
 */
public interface Saveable {

    /**
     * Sérialise l'objet en un format sauvegardable.
     * @return Objet sérialisé (ex: JSON, Map, etc.).
     */
    Object serialize();

    /**
     * Désérialise l'objet à partir d'un format sauvegardé.
     * @param data Données sérialisées.
     */
    void deserialize(Object data);
}