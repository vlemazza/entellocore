package it.vle.entellocore.service;

public class ContentExtractionException extends RuntimeException {

    public ContentExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentExtractionException(String message) {
        super(message);
    }
}
