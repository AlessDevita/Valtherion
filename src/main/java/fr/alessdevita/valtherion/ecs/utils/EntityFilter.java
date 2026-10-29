package fr.alessdevita.valtherion.ecs.utils;

import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.Component;

import java.util.*;
import java.util.function.*;

/**
 * Classe pour filtrer les entités dans Valtherion.
 * Permet de sélectionner des entités en fonction de leurs composants, tags, ou autres critères.
 */
public class EntityFilter {

    // Liste des classes de composants requises
    private final List<Class<? extends Component>> requiredComponents;

    // Liste des classes de composants exclus
    private final List<Class<? extends Component>> excludedComponents;

    // Liste des tags requis
    private final List<String> requiredTags;

    // Liste des tags exclus
    private final List<String> excludedTags;

    // Prédicat personnalisé pour le filtrage
    private Predicate<Entity> customPredicate;

    // Limite maximale du nombre d'entités retournées
    private int limit;

    /**
     * Constructeur du filtre d'entités.
     */
    public EntityFilter() {
        this.requiredComponents = new ArrayList<>();
        this.excludedComponents = new ArrayList<>();
        this.requiredTags = new ArrayList<>();
        this.excludedTags = new ArrayList<>();
        this.customPredicate = null;
        this.limit = Integer.MAX_VALUE;
    }

    /**
     * Ajoute un composant requis au filtre.
     * @param componentClass Classe du composant.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter withComponent(Class<? extends Component> componentClass) {
        if (componentClass != null) {
            requiredComponents.add(componentClass);
        }
        return this;
    }

    /**
     * Ajoute plusieurs composants requis au filtre.
     * @param componentClasses Classes des composants.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter withComponents(Class<? extends Component>... componentClasses) {
        for (Class<? extends Component> componentClass : componentClasses) {
            withComponent(componentClass);
        }
        return this;
    }

    /**
     * Ajoute un composant exclu au filtre.
     * @param componentClass Classe du composant.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter withoutComponent(Class<? extends Component> componentClass) {
        if (componentClass != null) {
            excludedComponents.add(componentClass);
        }
        return this;
    }

    /**
     * Ajoute plusieurs composants exclus au filtre.
     * @param componentClasses Classes des composants.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter withoutComponents(Class<? extends Component>... componentClasses) {
        for (Class<? extends Component> componentClass : componentClasses) {
            withoutComponent(componentClass);
        }
        return this;
    }

    /**
     * Ajoute un tag requis au filtre.
     * @param tag Tag requis.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter withTag(String tag) {
        if (tag != null) {
            requiredTags.add(tag);
        }
        return this;
    }

    /**
     * Ajoute plusieurs tags requis au filtre.
     * @param tags Tags requis.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter withTags(String... tags) {
        for (String tag : tags) {
            withTag(tag);
        }
        return this;
    }

    /**
     * Ajoute un tag exclu au filtre.
     * @param tag Tag exclu.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter withoutTag(String tag) {
        if (tag != null) {
            excludedTags.add(tag);
        }
        return this;
    }

    /**
     * Ajoute plusieurs tags exclus au filtre.
     * @param tags Tags exclus.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter withoutTags(String... tags) {
        for (String tag : tags) {
            withoutTag(tag);
        }
        return this;
    }

    /**
     * Définit un prédicat personnalisé pour le filtrage.
     * @param predicate Prédicat personnalisé.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter withPredicate(Predicate<Entity> predicate) {
        this.customPredicate = predicate;
        return this;
    }

    /**
     * Définit une limite maximale du nombre d'entités retournées.
     * @param limit Limite maximale.
     * @return Le filtre lui-même (pour le chaînage).
     */
    public EntityFilter limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Vérifie si une entité correspond au filtre.
     * @param entity Entité à vérifier.
     * @return true si l'entité correspond au filtre, false sinon.
     */
    public boolean matches(Entity entity) {
        if (entity == null) {
            return false;
        }

        // Vérifier les composants requis
        for (Class<? extends Component> componentClass : requiredComponents) {
            if (!entity.hasComponent(componentClass)) {
                return false;
            }
        }

        // Vérifier les composants exclus
        for (Class<? extends Component> componentClass : excludedComponents) {
            if (entity.hasComponent(componentClass)) {
                return false;
            }
        }

        // Vérifier les tags requis
        for (String tag : requiredTags) {
            if (!entity.hasTag(tag)) {
                return false;
            }
        }

        // Vérifier les tags exclus
        for (String tag : excludedTags) {
            if (entity.hasTag(tag)) {
                return false;
            }
        }

        // Vérifier le prédicat personnalisé
        if (customPredicate != null && !customPredicate.test(entity)) {
            return false;
        }

        return true;
    }

    /**
     * Filtre une liste d'entités.
     * @param entities Liste d'entités à filtrer.
     * @return Liste des entités qui correspondent au filtre.
     */
    public List<Entity> filter(List<Entity> entities) {
        List<Entity> result = new ArrayList<>();

        for (Entity entity : entities) {
            if (matches(entity)) {
                result.add(entity);
                if (result.size() >= limit) {
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Filtre une collection d'entités.
     * @param entities Collection d'entités à filtrer.
     * @return Liste des entités qui correspondent au filtre.
     */
    public List<Entity> filter(Collection<Entity> entities) {
        return filter(new ArrayList<>(entities));
    }

    /**
     * Récupère la première entité qui correspond au filtre.
     * @param entities Liste d'entités à filtrer.
     * @return Première entité qui correspond au filtre ou null si non trouvée.
     */
    public Entity findFirst(List<Entity> entities) {
        for (Entity entity : entities) {
            if (matches(entity)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Récupère la première entité qui correspond au filtre.
     * @param entities Collection d'entités à filtrer.
     * @return Première entité qui correspond au filtre ou null si non trouvée.
     */
    public Entity findFirst(Collection<Entity> entities) {
        return findFirst(new ArrayList<>(entities));
    }

    /**
     * Vérifie si une collection d'entités contient au moins une entité qui correspond au filtre.
     * @param entities Collection d'entités à vérifier.
     * @return true si au moins une entité correspond au filtre, false sinon.
     */
    public boolean anyMatch(Collection<Entity> entities) {
        for (Entity entity : entities) {
            if (matches(entity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si toutes les entités d'une collection correspondent au filtre.
     * @param entities Collection d'entités à vérifier.
     * @return true si toutes les entités correspondent au filtre, false sinon.
     */
    public boolean allMatch(Collection<Entity> entities) {
        for (Entity entity : entities) {
            if (!matches(entity)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Réinitialise le filtre.
     */
    public void reset() {
        requiredComponents.clear();
        excludedComponents.clear();
        requiredTags.clear();
        excludedTags.clear();
        customPredicate = null;
        limit = Integer.MAX_VALUE;
    }

    // --- Getters ---

    public List<Class<? extends Component>> getRequiredComponents() {
        return requiredComponents;
    }

    public List<Class<? extends Component>> getExcludedComponents() {
        return excludedComponents;
    }

    public List<String> getRequiredTags() {
        return requiredTags;
    }

    public List<String> getExcludedTags() {
        return excludedTags;
    }

    public Predicate<Entity> getCustomPredicate() {
        return customPredicate;
    }

    public int getLimit() {
        return limit;
    }

    /**
     * Retourne une représentation textuelle du filtre.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "EntityFilter{" +
                "requiredComponents=" + requiredComponents +
                ", excludedComponents=" + excludedComponents +
                ", requiredTags=" + requiredTags +
                ", excludedTags=" + excludedTags +
                ", limit=" + limit +
                '}';
    }
}