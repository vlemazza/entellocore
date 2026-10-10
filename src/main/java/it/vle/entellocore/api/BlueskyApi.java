package it.vle.entellocore.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class BlueskyApi {

    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public BlueskyApi() {
        this.client = HttpClient.newHttpClient();
    }

    public String getWebUrlFromAtUri(String atUri) {

        try {
            String[] parts = atUri.split("/");

            if (parts.length < 5) {
                return null;
            }

            String did = parts[2];
            String rkey = parts[4];

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://public.api.bsky.app/xrpc/app.bsky.actor.getProfile?actor="
                                    + URLEncoder.encode(did, StandardCharsets.UTF_8)
                    ))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            JsonNode profile = mapper.readTree(response.body());

            String handle = profile.path("handle").asText();

            if (handle.isEmpty()) {
                return null;
            }

            return "https://bsky.app/profile/"
                    + handle
                    + "/post/"
                    + rkey;

        } catch (Exception e) {
            return null;
        }
    }
}