package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;

import java.util.*;

/**
 * Composant pour les scripts d'une entité dans Valtherion.
 * Permet d'exécuter des scripts (Lua, Python, etc.) pour contrôler le comportement de l'entité.
 */
public class ScriptComponent implements Component {

    // Chemin du fichier de script
    private String scriptPath;

    // Contenu du script
    private String scriptContent;

    // Langage du script (LUA, PYTHON, etc.)
    private ScriptLanguage language;

    // Indique si le script est chargé
    private boolean isLoaded;

    // Indique si le script est en cours d'exécution
    private boolean isRunning;

    // Variables du script (pour l'interaction avec le jeu)
    private final Map<String, Object> variables;

    // Fonctions du script (pour les appels depuis le jeu)
    private final Map<String, ScriptFunction> functions;

    // Entité associée
    private Entity entity;

    // Énumération des langages de script
    public enum ScriptLanguage {
        LUA,      // Lua
        PYTHON,   // Python
        JAVASCRIPT // JavaScript
    }

    // Interface pour les fonctions de script
    public interface ScriptFunction {
        Object execute(Object... args);
    }

    /**
     * Constructeur du composant de script.
     */
    public ScriptComponent() {
        this.scriptPath = "";
        this.scriptContent = "";
        this.language = ScriptLanguage.LUA;
        this.isLoaded = false;
        this.isRunning = false;
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    /**
     * Constructeur du composant de script avec un chemin.
     * @param scriptPath Chemin du fichier de script.
     */
    public ScriptComponent(String scriptPath) {
        this();
        this.scriptPath = scriptPath;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void init() {
        loadScript();
    }

    /**
     * Charge le script depuis le fichier.
     */
    public void loadScript() {
        if (scriptPath == null || scriptPath.isEmpty()) {
            return;
        }

        // Logique pour charger le script depuis le fichier
        // Exemple : Utiliser une bibliothèque de script comme LuaJ ou Jython
        // scriptContent = FileUtils.readFileToString(new File(scriptPath));

        isLoaded = true;
        java.lang.System.out.println("Script chargé : " + scriptPath);
    }

    /**
     * Exécute le script.
     */
    public void execute() {
        if (!isLoaded || isRunning) {
            return;
        }

        isRunning = true;

        // Logique pour exécuter le script
        // Exemple : Utiliser une bibliothèque de script

        isRunning = false;
    }

    /**
     * Arrête l'exécution du script.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Définit une variable du script.
     * @param name Nom de la variable.
     * @param value Valeur de la variable.
     */
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    /**
     * Récupère une variable du script.
     * @param name Nom de la variable.
     * @return Valeur de la variable ou null si non trouvée.
     */
    public Object getVariable(String name) {
        return variables.get(name);
    }

    /**
     * Enregistre une fonction de script.
     * @param name Nom de la fonction.
     * @param function Fonction à enregistrer.
     */
    public void registerFunction(String name, ScriptFunction function) {
        functions.put(name, function);
    }

    /**
     * Appelle une fonction de script.
     * @param name Nom de la fonction.
     * @param args Arguments de la fonction.
     * @return Résultat de la fonction ou null si non trouvée.
     */
    public Object callFunction(String name, Object... args) {
        ScriptFunction function = functions.get(name);
        if (function != null) {
            return function.execute(args);
        }
        return null;
    }

    // --- Getters et Setters ---

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public String getScriptContent() {
        return scriptContent;
    }

    public void setScriptContent(String scriptContent) {
        this.scriptContent = scriptContent;
    }

    public ScriptLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ScriptLanguage language) {
        this.language = language;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public Map<String, ScriptFunction> getFunctions() {
        return functions;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ScriptComponent{" +
                "scriptPath='" + scriptPath + '\'' +
                ", language=" + language +
                ", isLoaded=" + isLoaded +
                ", isRunning=" + isRunning +
                '}';
    }
}