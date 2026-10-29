package fr.alessdevita.valtherion.client;

/**
 * Exception levée lors de la conversion de données au format Anvil.
 * Utilisée pour signaler des erreurs lors du chargement ou de la sauvegarde de chunks.
 */
public class AnvilConverterException extends Exception {

    // Code d'erreur (optionnel)
    private final int errorCode;

    // Chemin du fichier concerné (optionnel)
    private final String filePath;

    // Type d'erreur (optionnel)
    private final ErrorType errorType;

    // Énumération des types d'erreur
    public enum ErrorType {
        FILE_NOT_FOUND,       // Fichier introuvable
        INVALID_FORMAT,       // Format invalide
        CORRUPTED_DATA,       // Données corrompues
        UNSUPPORTED_VERSION,  // Version non supportée
        IO_ERROR,             // Erreur d'E/S
        UNKNOWN               // Erreur inconnue
    }

    /**
     * Constructeur de l'exception.
     */
    public AnvilConverterException() {
        super();
        this.errorCode = 0;
        this.filePath = null;
        this.errorType = ErrorType.UNKNOWN;
    }

    /**
     * Constructeur de l'exception avec un message.
     * @param message Message de l'exception.
     */
    public AnvilConverterException(String message) {
        super(message);
        this.errorCode = 0;
        this.filePath = null;
        this.errorType = ErrorType.UNKNOWN;
    }

    /**
     * Constructeur de l'exception avec un message et un code d'erreur.
     * @param message Message de l'exception.
     * @param errorCode Code d'erreur.
     */
    public AnvilConverterException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.filePath = null;
        this.errorType = ErrorType.UNKNOWN;
    }

    /**
     * Constructeur de l'exception avec un message, un code d'erreur et un type d'erreur.
     * @param message Message de l'exception.
     * @param errorCode Code d'erreur.
     * @param errorType Type d'erreur.
     */
    public AnvilConverterException(String message, int errorCode, ErrorType errorType) {
        super(message);
        this.errorCode = errorCode;
        this.filePath = null;
        this.errorType = errorType;
    }

    /**
     * Constructeur de l'exception avec un message, un code d'erreur, un type d'erreur et un chemin de fichier.
     * @param message Message de l'exception.
     * @param errorCode Code d'erreur.
     * @param errorType Type d'erreur.
     * @param filePath Chemin du fichier concerné.
     */
    public AnvilConverterException(String message, int errorCode, ErrorType errorType, String filePath) {
        super(message);
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.filePath = filePath;
    }

    /**
     * Constructeur de l'exception avec un message et une cause.
     * @param message Message de l'exception.
     * @param cause Cause de l'exception.
     */
    public AnvilConverterException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = 0;
        this.filePath = null;
        this.errorType = ErrorType.UNKNOWN;
    }

    /**
     * Constructeur de l'exception avec un message, un code d'erreur, un type d'erreur et une cause.
     * @param message Message de l'exception.
     * @param errorCode Code d'erreur.
     * @param errorType Type d'erreur.
     * @param cause Cause de l'exception.
     */
    public AnvilConverterException(String message, int errorCode, ErrorType errorType, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.filePath = null;
    }

    // --- Getters ---

    public int getErrorCode() {
        return errorCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * Retourne une représentation textuelle de l'exception.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnvilConverterException{");
        sb.append("message='").append(getMessage()).append("\''");
        if (errorCode != 0) {
            sb.append(", errorCode=").append(errorCode);
        }
        if (errorType != ErrorType.UNKNOWN) {
            sb.append(", errorType=").append(errorType);
        }
        if (filePath != null) {
            sb.append(", filePath='").append(filePath).append("\''");
        }
        sb.append('}');
        return sb.toString();
    }
}