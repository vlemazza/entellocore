package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostInstagram;
import it.vle.entellocore.utils.DataConvert;

import java.util.List;

public class InstagramMetadataParser implements MetadataParser {
    @Override
    public ExtractedPost parse(JsonNode json) {

        String author = json.path("fullname").asText();

        String text = json.path("description").asText();

        Long timestamp = DataConvert.toUnixTimestamp(json.path("date").asText());

        return new ExtractedPostInstagram(author, text, List.of(),timestamp);

    }
}
