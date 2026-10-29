package fr.alessdevita.valtherion.ecs.systems;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.ecs.System;
import fr.alessdevita.valtherion.ecs.components.ScriptComponent;

import java.util.*;

/**
 * Système pour gérer les scripts des entités dans Valtherion.
 */
public class ScriptSystem implements System {

    // Gestionnaire ECS associé
    private ECSManager ecsManager;

    // Liste des entités avec un composant de script
    private List<Entity> scriptEntities;

    // Indique si le système est activé
    private boolean isEnabled;

    /**
     * Constructeur du système de scripts.
     */
    public ScriptSystem() {
        this.scriptEntities = new ArrayList<>();
        this.isEnabled = true;
    }

    @Override
    public void setECSManager(ECSManager ecsManager) {
        this.ecsManager = ecsManager;
    }

    @Override
    public ECSManager getECSManager() {
        return ecsManager;
    }

    @Override
    public void init() {
        // Initialiser les scripts
        for (Entity entity : scriptEntities) {
            ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
            if (scriptComponent != null) {
                scriptComponent.init();
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!isEnabled || ecsManager == null) {
            return;
        }

        // Récupérer les entités avec le composant Script
        scriptEntities = ecsManager.getEntitiesWithComponent(ScriptComponent.class);

        // Mettre à jour les scripts
        for (Entity entity : scriptEntities) {
            ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
            if (scriptComponent != null && scriptComponent.isLoaded()) {
                // Exécuter le script si nécessaire
                if (!scriptComponent.isRunning()) {
                    scriptComponent.execute();
                }
            }
        }
    }

    /**
     * Charge un script pour une entité.
     * @param entityId ID de l'entité.
     * @param scriptPath Chemin du fichier de script.
     */
    public void loadScript(String entityId, String scriptPath) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null) {
            ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
            if (scriptComponent == null) {
                scriptComponent = new ScriptComponent(scriptPath);
                entity.addComponent(scriptComponent);
            }
            scriptComponent.setScriptPath(scriptPath);
            scriptComponent.loadScript();
        }
    }

    /**
     * Exécute un script pour une entité.
     * @param entityId ID de l'entité.
     */
    public void executeScript(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(ScriptComponent.class)) {
            ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
            scriptComponent.execute();
        }
    }

    /**
     * Arrête un script pour une entité.
     * @param entityId ID de l'entité.
     */
    public void stopScript(String entityId) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(ScriptComponent.class)) {
            ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
            scriptComponent.stop();
        }
    }

    /**
     * Définit une variable de script pour une entité.
     * @param entityId ID de l'entité.
     * @param name Nom de la variable.
     * @param value Valeur de la variable.
     */
    public void setScriptVariable(String entityId, String name, Object value) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(ScriptComponent.class)) {
            ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
            scriptComponent.setVariable(name, value);
        }
    }

    /**
     * Appelle une fonction de script pour une entité.
     * @param entityId ID de l'entité.
     * @param functionName Nom de la fonction.
     * @param args Arguments de la fonction.
     * @return Résultat de la fonction.
     */
    public Object callScriptFunction(String entityId, String functionName, Object... args) {
        Entity entity = ecsManager.getEntity(entityId);
        if (entity != null && entity.hasComponent(ScriptComponent.class)) {
            ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
            return scriptComponent.callFunction(functionName, args);
        }
        return null;
    }

    // --- Getters et Setters ---

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public List<Entity> getScriptEntities() {
        return scriptEntities;
    }
}