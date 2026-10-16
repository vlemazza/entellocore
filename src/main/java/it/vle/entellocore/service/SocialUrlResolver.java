package it.vle.entellocore.service;

import it.vle.entellocore.model.SocialService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Optional;

final class SocialUrlResolver {

    Optional<ResolvedSocialUrl> resolve(String rawUrl) {
        URI uri = parseHttpUri(rawUrl);

        if (uri == null || uri.getRawUserInfo() != null) {
            return Optional.empty();
        }

        String host = uri.getHost().toLowerCase(Locale.ROOT);
        SocialService service = SocialService.fromHost(host);

        if (service == SocialService.NOT_AVAILABLE) {
            return Optional.empty();
        }

        String normalizedHost = service.normalizeHost(host);
        String normalizedUrl = rawUrl;

        if (!normalizedHost.equals(host)) {
            normalizedUrl = replaceHost(uri, normalizedHost);
        }

        return Optional.of(
                new ResolvedSocialUrl(
                        rawUrl,
                        normalizedUrl,
                        service
                )
        );
    }

    private static URI parseHttpUri(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) {
            return null;
        }

        try {
            URI uri = new URI(rawUrl);
            String scheme = uri.getScheme();

            if (uri.isOpaque()
                    || uri.getHost() == null
                    || !("http".equalsIgnoreCase(scheme)
                    || "https".equalsIgnoreCase(scheme))) {
                return null;
            }

            return uri;
        } catch (URISyntaxException exception) {
            return null;
        }
    }

    private static String replaceHost(URI uri, String normalizedHost) {
        StringBuilder normalized = new StringBuilder()
                .append(uri.getScheme())
                .append("://")
                .append(normalizedHost);

        if (uri.getRawPath() != null) {
            normalized.append(uri.getRawPath());
        }

        if (uri.getRawQuery() != null) {
            normalized.append('?').append(uri.getRawQuery());
        }

        if (uri.getRawFragment() != null) {
            normalized.append('#').append(uri.getRawFragment());
        }

        try {
            return new URI(normalized.toString()).toASCIIString();
        } catch (URISyntaxException exception) {
            return uri.toASCIIString();
        }
    }

    record ResolvedSocialUrl(
            String originalUrl,
            String normalizedUrl,
            SocialService service) {
    }
}