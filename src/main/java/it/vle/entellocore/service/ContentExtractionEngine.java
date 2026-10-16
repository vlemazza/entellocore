package it.vle.entellocore.service;

import it.vle.entellocore.model.TextExtractionOutcome;

import java.util.List;

public interface ContentExtractionEngine {

    List<TextExtractionOutcome> extractFromText(String text);
}