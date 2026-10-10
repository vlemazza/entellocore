package it.vle.entellocore.service;

import it.vle.entellocore.model.ExtractedPost;

import java.util.Optional;

public interface ContentExtractionEngine {

    Optional<ExtractedPost> process(String rawUrl);
}
