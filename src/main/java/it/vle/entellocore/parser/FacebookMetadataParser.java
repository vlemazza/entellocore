package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostFacebook;
import it.vle.entellocore.utils.DataConvert;

import java.util.List;

public class FacebookMetadataParser implements MetadataParser {
    @Override
    public ExtractedPost parse(JsonNode json) {

        String author = json.path("username").asText();

        String text = this.parseDescription(json);

        String date = json.path("date").asText("");
        Long timestamp = date.isEmpty() ? null : DataConvert.toUnixTimestamp(date);

        String type = json.path("type").asText(null);

        return new ExtractedPostFacebook(author, text, List.of(), timestamp, type);

    }

    public String parseDescription(JsonNode json) {
        return json.path("description").asText("");
    }
}
