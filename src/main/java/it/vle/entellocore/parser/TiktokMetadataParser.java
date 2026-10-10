package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostTiktok;
import it.vle.entellocore.utils.DataConvert;

import java.util.List;

public class TiktokMetadataParser implements MetadataParser {
    @Override
    public ExtractedPost parse(JsonNode json) {

        String author = json.path("user").asText();

        String text = json.path("desc").asText();

        Long timestamp = DataConvert.toUnixTimestamp(json.path("date").asText());

        return new ExtractedPostTiktok(author, text, List.of(),timestamp);
    }

}
