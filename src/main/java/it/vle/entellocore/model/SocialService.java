package it.vle.entellocore.model;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public enum SocialService {

    TWITTER(
            List.of("twitter.com", "x.com"),
            Map.of(
                    "nitter.tiekoetter.com", "twitter.com",
                    "shitter.thepixora.com", "twitter.com"
            ),
            ExtractorType.GALLERY_DL
    ),
    BLUESKY(
            List.of("bsky.app"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    TIKTOK(
            List.of("tiktok.com"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    INSTAGRAM(
            List.of("instagram.com"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    REDDIT(
            List.of("reddit.com"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    BILIBILI(
            List.of("bilibili.com"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    PIXIV(
            List.of("pixiv.net"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    AO3(
            List.of("archiveofourown.org"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    POIPIKU(
            List.of("poipiku.com"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    TUMBLR(
            List.of("tumblr.com"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    PINTEREST(
            List.of("pinterest.com"),
            Map.of(),
            ExtractorType.GALLERY_DL
    ),
    YOUTUBE(
            List.of("youtube.com", "youtu.be"),
            Map.of(),
            ExtractorType.YOUTUBE_DL
    ),
    HACKER_NEWS(
            List.of("news.ycombinator.com"),
            Map.of(),
            ExtractorType.HACKER_NEWS
    ),
    NOT_AVAILABLE(
            List.of(),
            Map.of(),
            ExtractorType.NOT_AVAILABLE
    );

    private final List<String> domains;
    private final Map<String, String> alternateHosts;
    private final ExtractorType extractorType;

    SocialService(
            List<String> domains,
            Map<String, String> alternateHosts,
            ExtractorType extractorType) {

        this.domains = domains;
        this.alternateHosts = alternateHosts;
        this.extractorType = extractorType;
    }

    public ExtractorType getExtractorType() {
        return extractorType;
    }

    public static SocialService fromHost(String host) {
        if (host == null || host.isBlank()) {
            return NOT_AVAILABLE;
        }

        String normalizedHost = host.toLowerCase(Locale.ROOT);

        for (SocialService service : values()) {
            if (service != NOT_AVAILABLE && service.supportsHost(normalizedHost)) {
                return service;
            }
        }

        return NOT_AVAILABLE;
    }

    public String normalizeHost(String host) {
        String normalizedHost = host.toLowerCase(Locale.ROOT);

        for (Map.Entry<String, String> entry : alternateHosts.entrySet()) {

            if (matchesDomain(normalizedHost, entry.getKey())) {
                return entry.getValue();
            }
        }

        return normalizedHost;
    }

    private boolean supportsHost(String host) {
        for (String domain : domains) {
            if (matchesDomain(host, domain)) {
                return true;
            }
        }

        for (String alternateHost : alternateHosts.keySet()) {
            if (matchesDomain(host, alternateHost)) {
                return true;
            }
        }

        return false;
    }

    private static boolean matchesDomain(String host, String domain) {
        return host.equals(domain) || host.endsWith("." + domain);
    }
}