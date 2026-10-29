package fr.alessdevita.valtherion.core;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Système de logging personnalisé pour Valtherion.
 * Permet d'afficher des messages dans la console et de les sauvegarder dans un fichier.
 */
public class Logger {

    // Niveaux de log
    public enum LogLevel {
        INFO,    // Messages d'information
        DEBUG,   // Messages de debug
        WARN,    // Avertissements
        ERROR    // Erreurs
    }

    // Format de date pour les logs
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Nom du logger (ex: "Engine", "World", etc.)
    private final String name;

    // Fichier de log
    private final String logFilePath;
    private PrintWriter logFileWriter;

    // Niveau minimum de log à afficher
    private LogLevel minLogLevel;

    /**
     * Constructeur du Logger.
     * @param name Nom du logger (utilisé comme préfixe dans les messages).
     */
    public Logger(String name) {
        this(name, LogLevel.INFO, "logs/" + name + ".log");
    }

    /**
     * Constructeur du Logger avec personnalisation.
     * @param name Nom du logger.
     * @param minLogLevel Niveau minimum de log à afficher.
     * @param logFilePath Chemin vers le fichier de log.
     */
    public Logger(String name, LogLevel minLogLevel, String logFilePath) {
        this.name = name;
        this.minLogLevel = minLogLevel;
        this.logFilePath = logFilePath;

        // Créer le fichier de log
        try {
            this.logFileWriter = new PrintWriter(new FileWriter(logFilePath, true));
        } catch (IOException e) {
            System.err.println("Impossible de créer le fichier de log : " + e.getMessage());
        }
    }

    /**
     * Log un message avec le niveau INFO.
     * @param message Message à logger.
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Log un message avec le niveau DEBUG.
     * @param message Message à logger.
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Log un message avec le niveau WARN.
     * @param message Message à logger.
     */
    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    /**
     * Log un message avec le niveau ERROR.
     * @param message Message à logger.
     */
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    /**
     * Méthode générique pour logger un message.
     * @param level Niveau de log.
     * @param message Message à logger.
     */
    private void log(LogLevel level, String message) {
        // Vérifier si le niveau est suffisant
        if (level.ordinal() < minLogLevel.ordinal()) {
            return;
        }

        // Formater le message avec la date, le niveau et le nom
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String formattedMessage = String.format("[%s] [%s] [%s] %s", timestamp, level, name, message);

        // Afficher dans la console
        switch (level) {
            case ERROR:
                System.err.println(formattedMessage);
                break;
            case WARN:
                System.out.println("\u001B[33m" + formattedMessage + "\u001B[0m"); // Jaune
                break;
            case INFO:
                System.out.println("\u001B[32m" + formattedMessage + "\u001B[0m"); // Vert
                break;
            case DEBUG:
                System.out.println("\u001B[36m" + formattedMessage + "\u001B[0m"); // Cyan
                break;
        }

        // Écrire dans le fichier de log
        if (logFileWriter != null) {
            logFileWriter.println(formattedMessage);
            logFileWriter.flush();
        }
    }

    /**
     * Ferme le Logger et libère les ressources.
     */
    public void close() {
        if (logFileWriter != null) {
            logFileWriter.close();
        }
    }

    // --- Getters et Setters ---

    public String getName() {
        return name;
    }

    public LogLevel getMinLogLevel() {
        return minLogLevel;
    }

    public void setMinLogLevel(LogLevel minLogLevel) {
        this.minLogLevel = minLogLevel;
    }
}