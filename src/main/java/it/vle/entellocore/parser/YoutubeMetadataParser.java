package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostYoutube;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class YoutubeMetadataParser implements MetadataParser {

    @Override
    public ExtractedPost parse(JsonNode json) {
        String title = json.path("title").asText();
        String uploader = json.path("uploader").asText();
        String channel = json.path("channel").asText();
        String author = uploader.isBlank() ? channel : uploader;

        return new ExtractedPostYoutube(title, author, channel, List.of(), this.readTimestamp(json));
    }

    private Long readTimestamp(JsonNode json) {
        JsonNode timestamp = json.get("timestamp");
        if (timestamp != null && timestamp.canConvertToLong()) {
            return timestamp.asLong();
        }

        String uploadDate = json.path("upload_date").asText();
        if (uploadDate.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(uploadDate, DateTimeFormatter.BASIC_ISO_DATE)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toEpochSecond();
        } catch (DateTimeParseException exception) {
            return null;
        }
    }
}
