package fr.alessdevita.valtherion.client.util;

/**
 * Exception levée lors d'une erreur de parsing JSON (modèle de bloc,
 * métadonnées, etc.).
 */
public class JsonException extends RuntimeException {
    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
