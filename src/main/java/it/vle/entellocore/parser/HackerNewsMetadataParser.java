package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostHackerNews;

import java.util.List;

public class HackerNewsMetadataParser implements MetadataParser {

    @Override
    public ExtractedPost parse(JsonNode json) {
        String author = json.path("by").asText();

        String text = json.path("text").asText();

        String title = json.path("title").asText();

        Long timestamp = json.path("time").asLong();

        String url = json.path("url").asText();

        String type = json.path("type").asText();

        String score = json.path("score").asText();

        return new ExtractedPostHackerNews(author, text, List.of(), title, url, type, score, timestamp);
    }
}
