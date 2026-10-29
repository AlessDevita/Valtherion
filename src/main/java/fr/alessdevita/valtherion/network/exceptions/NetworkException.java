package fr.alessdevita.valtherion.network.exceptions;

/**
 * Exception pour les erreurs réseau dans Valtherion.
 * Levée lorsque des erreurs se produisent lors des opérations réseau.
 */
public class NetworkException extends Exception {

    // Code d'erreur (optionnel)
    private final int errorCode;

    /**
     * Constructeur de l'exception réseau.
     */
    public NetworkException() {
        super();
        this.errorCode = 0;
    }

    /**
     * Constructeur de l'exception réseau avec un message.
     * @param message Message de l'exception.
     */
    public NetworkException(String message) {
        super(message);
        this.errorCode = 0;
    }

    /**
     * Constructeur de l'exception réseau avec un message et un code d'erreur.
     * @param message Message de l'exception.
     * @param errorCode Code d'erreur.
     */
    public NetworkException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructeur de l'exception réseau avec un message et une cause.
     * @param message Message de l'exception.
     * @param cause Cause de l'exception.
     */
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = 0;
    }

    /**
     * Constructeur de l'exception réseau avec un message, un code d'erreur et une cause.
     * @param message Message de l'exception.
     * @param errorCode Code d'erreur.
     * @param cause Cause de l'exception.
     */
    public NetworkException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Constructeur de l'exception réseau avec une cause.
     * @param cause Cause de l'exception.
     */
    public NetworkException(Throwable cause) {
        super(cause);
        this.errorCode = 0;
    }

    // --- Getter ---

    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Retourne une représentation textuelle de l'exception réseau.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "NetworkException{" +
                "message='" + getMessage() + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}