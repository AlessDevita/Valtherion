package fr.alessdevita.valtherion.ecs;

import fr.alessdevita.valtherion.ecs.components.InventoryComponent;
import fr.alessdevita.valtherion.ecs.components.QuestComponent;

import fr.alessdevita.valtherion.inventory.Inventory;
import fr.alessdevita.valtherion.quest.QuestLog;
import org.joml.Vector3f;

import java.util.*;

/**
 * Classe pour une entité dans le système ECS de Valtherion.
 * Une entité est un simple conteneur d'ID et de composants.
 */
public class Entity {

    // ID unique de l'entité
    private final String id;

    // Map des composants (Type -> Composant)
    private final Map<Class<? extends Component>, Component> components;

    // Indique si l'entité est active
    private boolean isActive;

    // Indique si l'entité est marquée pour destruction
    private boolean isMarkedForDestruction;

    // Tags de l'entité (pour le filtrage)
    private final Set<String> tags;

    /**
     * Constructeur de l'entité.
     * @param id ID unique de l'entité.
     */
    public Entity(String id) {
        this.id = id;
        this.components = new HashMap<>();
        this.isActive = true;
        this.isMarkedForDestruction = false;
        this.tags = new HashSet<>();
    }

    /**
     * Ajoute un composant à l'entité.
     * @param component Composant à ajouter.
     * @param <T> Type du composant.
     * @return L'entité elle-même (pour le chaînage).
     */
    public <T extends Component> Entity addComponent(T component) {
        if (component != null) {
            components.put(component.getClass(), component);
            component.setEntity(this);
        }
        return this;
    }

    /**
     * Récupère un composant de l'entité.
     *
     * @param componentClass Classe du composant.
     * @param <T>            Type du composant.
     * @return Composant ou null si non trouvé.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentClass) {
        return (T) components.get(componentClass);
    }

    /**
     * Vérifie si l'entité a un composant.
     * @param componentClass Classe du composant.
     * @return true si l'entité a le composant, false sinon.
     */
    public boolean hasComponent(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
    }

    /**
     * Retire un composant de l'entité.
     * @param componentClass Classe du composant.
     * @param <T> Type du composant.
     * @return Composant retiré ou null si non trouvé.
     */
    public <T extends Component> T removeComponent(Class<T> componentClass) {
        T component = getComponent(componentClass);
        if (component != null) {
            components.remove(componentClass);
            component.setEntity(null);
        }
        return component;
    }

    // --- Méthodes utilitaires (stubs) pour compatibilité avec le reste du code ---

    public void move(float dx, float dy, float dz) {
        // no-op pour l'entité ECS
    }

    public float getX() { return 0f; }
    public float getY() { return 0f; }
    public float getZ() { return 0f; }

    public String getName() { return id; }

    public Inventory getInventory() {
        InventoryComponent ic = getComponent(InventoryComponent.class);
        return ic != null ? ic.getInventory() : null;
    }

    public QuestLog getQuestLog() {
        // Non implémenté: retourne null pour compilation
        return null;
    }

    public int getLevel() {
        // Valeur par défaut
        return 1;
    }

    public Vector3f getPosition() {
        return new Vector3f(0f, 0f, 0f);
    }

    /**
     * Ajoute un tag à l'entité.
     * @param tag Tag à ajouter.
     * @return L'entité elle-même (pour le chaînage).
     */
    public Entity addTag(String tag) {
        if (tag != null) {
            tags.add(tag);
        }
        return this;
    }

    /**
     * Vérifie si l'entité a un tag.
     * @param tag Tag à vérifier.
     * @return true si l'entité a le tag, false sinon.
     */
    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    /**
     * Retire un tag de l'entité.
     * @param tag Tag à retirer.
     */
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    /**
     * Active ou désactive l'entité.
     * @param active true pour activer, false pour désactiver.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Marque l'entité pour destruction.
     */
    public void markForDestruction() {
        isMarkedForDestruction = true;
    }

    /**
     * Détruit l'entité.
     */
    public void destroy() {
        // Nettoyer tous les composants
        for (Component component : components.values()) {
            component.cleanup();
        }
        components.clear();
        tags.clear();
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isMarkedForDestruction() {
        return isMarkedForDestruction;
    }

    public Collection<Component> getAllComponents() {
        return components.values();
    }

    public Set<String> getTags() {
        return tags;
    }

    /**
     * Retourne une représentation textuelle de l'entité.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Entity{" +
                "id='" + id + '\'' +
                ", active=" + isActive +
                ", components=" + components.size() +
                ", tags=" + tags +
                '}';
    }

}