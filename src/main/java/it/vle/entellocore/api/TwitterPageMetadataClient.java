
package it.vle.entellocore.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class TwitterPageMetadataClient {

    private static final Logger LOGGER = Logger.getLogger(TwitterPageMetadataClient.class.getName());

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    private final HttpClient httpClient;

    public TwitterPageMetadataClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public String fetchDescription(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.isBlank()) {
            return null;
        }

        try {
            URI uri = URI.create(sourceUrl);

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(REQUEST_TIMEOUT)
                    .header("Accept", "text/html")
                    .header("User-Agent", "Mozilla/5.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                LOGGER.warning("Twitter post page returned HTTP " + response.statusCode());
                return null;
            }

            Document page = Jsoup.parse(response.body());

            for (String selector : List.of(
                    "meta[property=og:description]",
                    "meta[name=description]",
                    "meta[name=twitter:description]"
            )) {
                String description = page.select(selector)
                        .attr("content")
                        .trim();

                if (!description.isBlank()) {
                    return description;
                }
            }

            LOGGER.warning("Twitter post page does not expose a description in its metadata.");

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            LOGGER.warning("Interrupted while retrieving Twitter post metadata.");

        } catch (IOException | IllegalArgumentException exception) {
            LOGGER.warning("Unable to retrieve Twitter post metadata: " + exception.getMessage());
        }

        return null;
    }

}
