package it.vle.entellocore.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.SocialService;
import it.vle.entellocore.parser.HackerNewsMetadataParser;

import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HackerNewsService implements ContentService {

    private final ObjectMapper mapper;
    private final HttpClient client;
    private final HackerNewsMetadataParser parser;

    public HackerNewsService() {
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient();
        this.parser = new HackerNewsMetadataParser();
    }

    @Override
    public ExtractedPost extract(String url, SocialService service) {

        if (service == SocialService.NOT_AVAILABLE) {
            throw new RuntimeException("Unsupported URL");
        }

        URI apiUrl = this.buildApiUrl(url);

        JsonNode json = this.fetchJson(apiUrl);

        return parser.parse(json);
    }

    private String extractId(String url) {
        try {
            URI uri = URI.create(url);

            String query = uri.getQuery();

            if (query == null) {
                return null;
            }

            for (String param : query.split("&")) {
                String[] parts = param.split("=");

                if (parts.length == 2 && parts[0].equals("id")) {
                    return URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                }
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid HackerNews URL", e);
        }

        return null;
    }

    private URI buildApiUrl(String url) {

        String id = extractId(url);

        if (id == null) {
            throw new IllegalArgumentException("Missing HackerNews id");
        }

        return URI.create("https://hacker-news.firebaseio.com/v0/item/" + id + ".json?print=pretty");
    }

    private JsonNode fetchJson(URI uri) {

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("HackerNews API error: " + response.statusCode());
            }

            return mapper.readTree(response.body());

        } catch (Exception e) {
            throw new RuntimeException("Cannot fetch HackerNews data", e);
        }
    }
}