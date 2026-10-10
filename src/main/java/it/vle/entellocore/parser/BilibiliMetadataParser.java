package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostBilibili;
import it.vle.entellocore.utils.DataConvert;

import java.util.List;

public class BilibiliMetadataParser implements MetadataParser {
    @Override
    public ExtractedPost parse(JsonNode json) {
        String author = json.path("username").asText();

        String text = this.extractContent(json.path("content"));

        Long timestamp = DataConvert.toUnixTimestamp(json.path("date").asText());

        return new ExtractedPostBilibili(author, text, List.of(), timestamp);
    }

    private String extractContent(JsonNode content) {

        if (content.isArray()) {

            StringBuilder builder = new StringBuilder();

            for (JsonNode node : content) {

                if (!builder.isEmpty()) {
                    builder.append("\n\n");
                }

                builder.append(node.asText());
            }

            return builder.toString();
        }

        return content.asText();
    }
}
