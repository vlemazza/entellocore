package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostTwitter;
import it.vle.entellocore.utils.DataConvert;

import java.util.List;

public class TwitterMetadataParser implements MetadataParser {

    @Override
    public ExtractedPost parse(JsonNode json) {

        String author = json.path("author").path("nick").asText();

        String username = json.path("author").path("name").asText();

        String profileImageUrl = json.path("author").path("profile_image").asText();

        String text = json.path("content").asText();

        Long timestamp = DataConvert.toUnixTimestamp(json.path("date").asText());

        String replyCount = json.path("reply_count").asText();

        String retweetCount = json.path("retweet_count").asText();

        String quoteCount = json.path("quote_count").asText();

        String viewCount = json.path("view_count").asText();

        return new ExtractedPostTwitter(
                author,
                text,
                List.of(),
                timestamp,
                replyCount,
                retweetCount,
                quoteCount,
                viewCount,
                profileImageUrl,
                username
        );
    }
}