package fr.alessdevita.valtherion.ai;

import fr.alessdevita.valtherion.ai.behavior.Behavior;
import fr.alessdevita.valtherion.ai.behavior.BehaviorTree;
import fr.alessdevita.valtherion.ai.pathfinding.Node;
import fr.alessdevita.valtherion.ai.pathfinding.Pathfinder;
import fr.alessdevita.valtherion.core.Logger;
import fr.alessdevita.valtherion.entity.living.LivingEntity;
import fr.alessdevita.valtherion.world.World;

import java.util.*;

/**
 * Gestionnaire principal de l'IA pour Valtherion.
 * Coordonne les comportements des entités (mobs, NPCs, boss) et gère le pathfinding.
 */
public class AIManager {

    // Logger pour les messages de debug
    private final Logger logger;

    // Référence au monde pour le pathfinding et les collisions
    private World world;

    // Liste des entités avec IA
    private final List<LivingEntity> aiEntities;

    // Pathfinder pour la recherche de chemin (A*)
    private Pathfinder pathfinder;

    // Arbres de comportement pour les entités
    private final Map<LivingEntity, BehaviorTree> behaviorTrees;

    // Compteur pour les mises à jour (debug)
    private int updateCount;

    /**
     * Constructeur du gestionnaire d'IA.
     * @param world Référence au monde du jeu.
     */
    public AIManager(World world) {
        this.logger = new Logger("AIManager");
        this.world = world;
        this.aiEntities = new ArrayList<>();
        this.behaviorTrees = new HashMap<>();
        this.pathfinder = new Pathfinder(world);
        this.updateCount = 0;

        logger.info("Gestionnaire d'IA initialisé.");
    }

    /**
     * Met à jour toutes les entités IA.
     * Appelé à chaque tick du jeu.
     */
    public void update() {
        updateCount++;

        // Mettre à jour chaque entité IA
        for (LivingEntity entity : aiEntities) {
            if (entity == null || !entity.isAlive()) {
                continue; // Ignorer les entités mortes ou nulles
            }

            // Mettre à jour l'arbre de comportement de l'entité
            BehaviorTree tree = behaviorTrees.get(entity);
            if (tree != null) {
                tree.update(entity);
            }
        }

        // Afficher un message de debug (optionnel)
        if (updateCount % 100 == 0) {
            logger.debug("Mise à jour de l'IA (" + aiEntities.size() + " entités actives).");
        }
    }

    /**
     * Ajoute une entité à la liste des entités IA.
     * @param entity Entité à ajouter.
     * @param behaviorTree Arbre de comportement associé à l'entité.
     */
    public void addEntity(LivingEntity entity, BehaviorTree behaviorTree) {
        if (entity == null) {
            logger.warn("Impossible d'ajouter une entité nulle.");
            return;
        }

        if (!aiEntities.contains(entity)) {
            aiEntities.add(entity);
            behaviorTrees.put(entity, behaviorTree);
            logger.debug("Entité IA ajoutée : " + entity.getClass().getSimpleName());
        }
    }

    /**
     * Supprime une entité de la liste des entités IA.
     * @param entity Entité à supprimer.
     */
    public void removeEntity(LivingEntity entity) {
        if (entity == null) {
            logger.warn("Impossible de supprimer une entité nulle.");
            return;
        }

        aiEntities.remove(entity);
        behaviorTrees.remove(entity);
        logger.debug("Entité IA supprimée : " + entity.getClass().getSimpleName());
    }

    /**
     * Trouve un chemin entre deux points pour une entité.
     * @param entity Entité qui cherche le chemin.
     * @param startX Coordonnée X de départ.
     * @param startY Coordonnée Y de départ.
     * @param startZ Coordonnée Z de départ.
     * @param endX Coordonnée X d'arrivée.
     * @param endY Coordonnée Y d'arrivée.
     * @param endZ Coordonnée Z d'arrivée.
     * @return Liste de nœuds représentant le chemin (ou une liste vide si aucun chemin n'est trouvé).
     */
    public List<Node> findPath(LivingEntity entity, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        if (entity == null) {
            logger.warn("Impossible de trouver un chemin pour une entité nulle.");
            return Collections.emptyList();
        }

        // Utiliser le pathfinder pour trouver un chemin
        List<Node> path = pathfinder.findPath(startX, startY, startZ, endX, endY, endZ);

        if (path.isEmpty()) {
            logger.debug("Aucun chemin trouvé pour " + entity.getClass().getSimpleName() + " de (" +
                    startX + ", " + startY + ", " + startZ + ") à (" + endX + ", " + endY + ", " + endZ + ").");
        }

        return path;
    }

    /**
     * Ajoute un comportement à une entité.
     * @param entity Entité cible.
     * @param behavior Comportement à ajouter.
     */
    public void addBehavior(LivingEntity entity, Behavior behavior) {
        if (entity == null || behavior == null) {
            logger.warn("Impossible d'ajouter un comportement : entité ou comportement nul.");
            return;
        }

        BehaviorTree tree = behaviorTrees.get(entity);
        if (tree != null) {
            tree.addBehavior(behavior);
            logger.debug("Comportement ajouté à " + entity.getClass().getSimpleName() + " : " +
                    behavior.getClass().getSimpleName());
        }
    }

    /**
     * Supprime un comportement d'une entité.
     * @param entity Entité cible.
     * @param behavior Comportement à supprimer.
     */
    public void removeBehavior(LivingEntity entity, Behavior behavior) {
        if (entity == null || behavior == null) {
            logger.warn("Impossible de supprimer un comportement : entité ou comportement nul.");
            return;
        }

        BehaviorTree tree = behaviorTrees.get(entity);
        if (tree != null) {
            tree.removeBehavior(behavior);
            logger.debug("Comportement supprimé de " + entity.getClass().getSimpleName() + " : " +
                    behavior.getClass().getSimpleName());
        }
    }

    /**
     * Vérifie si une entité est gérée par l'IA.
     * @param entity Entité à vérifier.
     * @return true si l'entité est gérée, false sinon.
     */
    public boolean hasEntity(LivingEntity entity) {
        return aiEntities.contains(entity);
    }

    /**
     * Retourne le nombre d'entités IA actives.
     * @return Nombre d'entités IA.
     */
    public int getEntityCount() {
        return aiEntities.size();
    }

    /**
     * Retourne l'arbre de comportement d'une entité.
     * @param entity Entité cible.
     * @return Arbre de comportement (ou null si l'entité n'en a pas).
     */
    public BehaviorTree getBehaviorTree(LivingEntity entity) {
        return behaviorTrees.get(entity);
    }

    // --- Getters et Setters ---

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        this.pathfinder = new Pathfinder(world); // Recréer le pathfinder avec le nouveau monde
    }

    public Pathfinder getPathfinder() {
        return pathfinder;
    }

    public List<LivingEntity> getAiEntities() {
        return Collections.unmodifiableList(aiEntities);
    }
}