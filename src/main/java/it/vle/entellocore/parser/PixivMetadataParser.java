package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostPixiv;
import it.vle.entellocore.utils.DataConvert;

import java.util.List;

public class PixivMetadataParser implements MetadataParser{

    @Override
    public ExtractedPost parse(JsonNode json) {

        String author = json.path("user").path("account").asText();

        String title = json.path("title").asText();

        String text = json.path("caption").asText();

        Long timestamp = DataConvert.toUnixTimestamp(json.path("date").asText());

        return new ExtractedPostPixiv(author, text, List.of(), timestamp, title);
    }
}
