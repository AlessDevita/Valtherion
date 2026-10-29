package fr.alessdevita.valtherion.save.exceptions;

/**
 * Exception pour les erreurs de sauvegarde dans Valtherion.
 */
public class SaveException extends Exception {

    /**
     * Constructeur de l'exception de sauvegarde.
     */
    public SaveException() {
        super();
    }

    /**
     * Constructeur de l'exception de sauvegarde avec un message.
     * @param message Message de l'exception.
     */
    public SaveException(String message) {
        super(message);
    }

    /**
     * Constructeur de l'exception de sauvegarde avec un message et une cause.
     * @param message Message de l'exception.
     * @param cause Cause de l'exception.
     */
    public SaveException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur de l'exception de sauvegarde avec une cause.
     * @param cause Cause de l'exception.
     */
    public SaveException(Throwable cause) {
        super(cause);
    }
}