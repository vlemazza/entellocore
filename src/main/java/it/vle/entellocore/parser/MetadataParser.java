package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;

public interface MetadataParser {

    ExtractedPost parse(JsonNode json);
}