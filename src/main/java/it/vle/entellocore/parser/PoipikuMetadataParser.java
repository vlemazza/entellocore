package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostPoipiku;

import java.util.List;

public class PoipikuMetadataParser implements MetadataParser{

    @Override
    public ExtractedPost parse(JsonNode json) {
        String author = json.path("user_name").asText();

        String text = json.path("description").asText();

        return new ExtractedPostPoipiku(author, text, List.of(), null);
    }
}
