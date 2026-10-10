package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.utils.DataConvert;
import org.jsoup.Jsoup;

import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostTumblr;

import java.util.ArrayList;
import java.util.List;

public class TumblrMetadataParser implements MetadataParser {
    @Override
    public ExtractedPost parse(JsonNode json) {
        String author = json.path("blog_name").asText();
        String text = this.extractBody(json.path("body"));
        List<String> tags = this.extractListTags(json.path("tags"));
        Long timestamp = DataConvert.toUnixTimestamp(json.path("date").asText());

        return new ExtractedPostTumblr(author, text, List.of(), timestamp, tags);
    }

    private List<String> extractListTags(JsonNode node) {

        List<String> result = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode item : node) {
                result.add("#"+item.asText());
            }
        }

        return result;
    }

    private String extractBody(JsonNode node) {

        String html = node.asText("");

        if (html.isEmpty()) {
            return "";
        }

        html = html
                .replace("<br>", "\n")
                .replace("<br/>", "\n")
                .replace("<br />", "\n")
                .replace("</p>", "\n\n");

        return Jsoup.parse(html).wholeText().trim();
    }
}
