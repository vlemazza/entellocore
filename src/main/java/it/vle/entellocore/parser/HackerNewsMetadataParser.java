package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostHackerNews;

import java.util.List;

public class HackerNewsMetadataParser implements MetadataParser {

    @Override
    public ExtractedPost parse(JsonNode json) {
        String author = json.path("by").asText();

        String text = parseText(json.path("text").asText());

        String title = json.path("title").asText();

        Long timestamp = json.path("time").asLong();

        String url = json.path("url").asText();

        String type = json.path("type").asText();

        String score = json.path("score").asText();

        return new ExtractedPostHackerNews(author, text, List.of(), title, url, type, score, timestamp);
    }

    private String parseText(String html) {
        if (html == null || html.isBlank()) return html;
        Document document = Jsoup.parseBodyFragment(html);
        java.util.List<String> paragraphs = document.select("p").eachText();
        return paragraphs.isEmpty()
                ? document.body().text()
                : String.join("\n\n", paragraphs);
    }
}
