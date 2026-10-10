package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostReddit;

import java.util.List;

public class RedditMetadataParser implements MetadataParser{
    @Override
    public ExtractedPost parse(JsonNode json) {

        String author = json.path("author").asText();

        String text = json.path("selftext").asText();

        String subreddit = json.path("subreddit_name_prefixed").asText();

        String title = json.path("title").asText();

        String urlExt = json.path("url_overridden_by_dest").asText();

        String urlExtDomain = json.path("domain").asText();

        Long timestamp = json.path("created").asLong();

        String upvote = json.path("ups").asText();

        String numComments = json.path("num_comments").asText();

        return new ExtractedPostReddit(
                author,
                text,
                List.of(),
                timestamp,
                subreddit,
                title,
                urlExt,
                urlExtDomain,
                upvote,
                numComments
        );

    }
}
