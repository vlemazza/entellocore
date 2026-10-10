package it.vle.entellocore.parser;

import com.fasterxml.jackson.databind.JsonNode;
import it.vle.entellocore.model.ExtractedPostPinterest;
import it.vle.entellocore.utils.DataConvert;

import java.util.List;

public class PinterestMetadataParser implements MetadataParser{
    @Override
    public ExtractedPostPinterest parse(JsonNode json) {
        String author = json.path("native_creator").path("full_name").asText();

        String title = json.path("title").asText();

        String text = json.path("closeup_unified_description").asText();

        Long timestamp = DataConvert.toUnixTimestampRfc1123(json.path("created_at").asText());

        return new ExtractedPostPinterest(author, text, List.of(), title, timestamp);
    }
}
