package it.vle.entellocore.model;

import it.vle.entellocore.exception.ContentExtractionException;

public sealed interface TextExtractionOutcome
        permits TextExtractionOutcome.Success,
        TextExtractionOutcome.Failure {

    String url();

    record Success(String url, ExtractedPost post) implements TextExtractionOutcome {}

    record Failure(String url, ContentExtractionException exception) implements TextExtractionOutcome {}
}