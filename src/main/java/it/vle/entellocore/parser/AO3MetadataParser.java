package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPostAO3;
import it.vle.entellocore.utils.DataConvert;

import java.util.ArrayList;
import java.util.List;

public class AO3MetadataParser implements MetadataParser {
    @Override
    public ExtractedPostAO3 parse(JsonNode json) {

        String author = json.path("author").asText();
        String text = this.extractSummary(json.path("summary"));

        Long timestamp = DataConvert.toUnixTimestamp(json.path("date").asText());

        return new ExtractedPostAO3(

                author,
                text,
                List.of(),
                timestamp,

                json.path("title").asText(),

                this.extractList(json.path("rating")),
                this.extractList(json.path("categories")),
                this.extractList(json.path("fandom")),
                this.extractList(json.path("relationships")),
                this.extractList(json.path("characters")),

                json.path("words").asInt(),

                json.path("chapters").size()
                        + "/?",

                json.path("likes").asInt(),
                json.path("views").asInt()
        );
    }


    private List<String> extractList(JsonNode node) {

        List<String> result = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode item : node) {
                result.add(item.asText());
            }
        }

        return result;
    }


    private String extractSummary(JsonNode node) {

        if (!node.isArray()) {
            return node.asText();
        }


        StringBuilder builder = new StringBuilder();

        for (JsonNode item : node) {

            if (!builder.isEmpty()) {
                builder.append("\n\n");
            }

            builder.append(item.asText());
        }

        return builder.toString();
    }
}