package it.vle.entellocore.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

public enum SocialService {

    TWITTER(List.of("twitter.com", "x.com"), ExtractorType.GALLERY_DL),
    BLUESKY(List.of("bsky.app"), ExtractorType.GALLERY_DL),
    TIKTOK(List.of("tiktok.com"), ExtractorType.GALLERY_DL),
    INSTAGRAM(List.of("instagram.com"), ExtractorType.GALLERY_DL),
    REDDIT(List.of("reddit.com"), ExtractorType.GALLERY_DL),
    BILIBILI(List.of("bilibili.com"), ExtractorType.GALLERY_DL),
    PIXIV(List.of("pixiv.net"), ExtractorType.GALLERY_DL),
    AO3(List.of("archiveofourown.org"), ExtractorType.GALLERY_DL),
    POIPIKU(List.of("poipiku.com"), ExtractorType.GALLERY_DL),
    TUMBLR(List.of("tumblr.com"), ExtractorType.GALLERY_DL),
    PINTEREST(List.of("pinterest.com"), ExtractorType.GALLERY_DL),
    YOUTUBE(List.of("youtube.com", "youtu.be"), ExtractorType.YOUTUBE_DL),

    HACKER_NEWS(List.of("news.ycombinator.com"), ExtractorType.HACKER_NEWS),
    NOT_AVAILABLE(List.of(), ExtractorType.NOT_AVAILABLE);

    private static final List<String> NITTER_INSTANCES = List.of("nitter.tiekoetter.com", "shitter.thepixora.com");
    private final List<String> domains;
    private final ExtractorType extractorType;

    SocialService(List<String> domains, ExtractorType extractorType) {
        this.domains = domains;
        this.extractorType = extractorType;
    }

    public ExtractorType getExtractorType() {
        return extractorType;
    }

    public boolean matches(String url) {
        URI uri = parseHttpUri(url);
        if (uri == null || uri.getRawUserInfo() != null) {
            return false;
        }
        String host = uri.getHost().toLowerCase(Locale.ROOT);
        return matchesHost(host, domains) || this == TWITTER && matchesHost(host, NITTER_INSTANCES);
    }

    public static SocialService fromUrl(String url) {
        for (SocialService service : values()) {
            if (service == NOT_AVAILABLE) {
                continue;
            }
            if (service.matches(url)) {
                return service;
            }
        }
        return NOT_AVAILABLE;
    }

    public String normalizeTwitterUrl(String url) {
        if (this != TWITTER) {
            return url;
        }

        URI uri = parseHttpUri(url);
        if (uri == null || uri.getHost() == null) {
            return url;
        }

        String host = uri.getHost().toLowerCase(Locale.ROOT);
        String normalizedHost;

        if (matchesHost(host, NITTER_INSTANCES)) {
            normalizedHost = "twitter.com";
        } else {
            return url;
        }

        try {
            return new URI(
                    uri.getScheme(),
                    null,
                    normalizedHost,
                    -1,
                    uri.getPath(),
                    uri.getQuery(),
                    uri.getFragment()
            ).toASCIIString();
        } catch (URISyntaxException exception) {
            return url;
        }
    }

    private static boolean matchesHost(String host, List<String> domains) {
        for (String domain : domains) {
            if (host.equals(domain) || host.endsWith("." + domain)) {
                return true;
            }
        }
        return false;
    }

    private static URI parseHttpUri(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (uri.isOpaque() || uri.getHost() == null || scheme == null
                    || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
                return null;
            }
            return uri;
        } catch (URISyntaxException exception) {
            return null;
        }
    }

}