package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostBluesky;
import it.vle.entellocore.utils.DataConvert;

import java.util.List;

public class BlueskyMetadataParser implements MetadataParser {

    @Override
    public ExtractedPost parse(JsonNode json) {

        String author = json.path("author").path("displayName").asText();
        String avatar = json.path("author").path("avatar").asText();
        String username = json.path("author").path("handle").asText();
        String text = json.path("text").asText();
        String replyCount = json.path("replyCount").asText();
        String repostCount = json.path("repostCount").asText();
        String likeCount = json.path("likeCount").asText();

        if (text.isEmpty()) {
            text = json.path("record").path("text").asText();
        }

        Long timestamp = DataConvert.toUnixTimestamp(json.path("date").asText());

        String embeddedPostUri = this.parseEmbeddedPostUri(json.path("embed"));
        ExtractedPostBluesky.EmbedExternal embedExternal = this.parseEmbedExternal(json.path("embed"));

        return new ExtractedPostBluesky(
                author,
                text,
                List.of(),
                timestamp,
                null,
                embeddedPostUri,
                embedExternal,
                avatar,
                replyCount,
                repostCount,
                likeCount,
                username
        );
    }

    private ExtractedPostBluesky.EmbedExternal parseEmbedExternal(JsonNode embed) {
        if ("app.bsky.embed.external".equals(embed.path("$type").asText())) {
            return new ExtractedPostBluesky.EmbedExternal(
                    embed.path("external").path("description").asText(),
                    embed.path("external").path("title").asText(),
                    embed.path("external").path("uri").asText()
            );
        }
        return null;
    }

    private String parseEmbeddedPostUri(JsonNode embed) {

        if ("app.bsky.embed.record".equals(embed.path("$type").asText())) {
            String uriEmbed = embed.path("record").path("uri").asText();
            if (!uriEmbed.isEmpty()) {
                return uriEmbed;
            }
        }

        return null;
    }
}
