package it.vle.entellocore.exception;

public class ContentExtractionException extends Exception {

    public ContentExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentExtractionException(String message) {
        super(message);
    }
}