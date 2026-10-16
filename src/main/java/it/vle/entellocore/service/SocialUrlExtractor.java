package it.vle.entellocore.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class SocialUrlExtractor {

    private static final Pattern URL_PATTERN = Pattern.compile(
                    "https?://[^\\s<>\"']+",
                    Pattern.CASE_INSENSITIVE
            );

    private SocialUrlExtractor() {}

    static List<String> extractUrls(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        Set<String> urls = new LinkedHashSet<>();
        Matcher matcher = URL_PATTERN.matcher(text);

        while (matcher.find()) {
            String url = trimTrailingPunctuation(matcher.group());

            if (!url.isBlank()) {
                urls.add(url);
            }
        }

        return List.copyOf(urls);
    }

    private static String trimTrailingPunctuation(String url) {
        String result = url;

        while (!result.isEmpty()) {
            char last = result.charAt(result.length() - 1);

            if (".,!?;:".indexOf(last) >= 0) {
                result = result.substring(0, result.length() - 1);
                continue;
            }

            char opening = switch (last) {
                case ')' -> '(';
                case ']' -> '[';
                case '}' -> '{';
                default -> '\0';
            };

            if (opening == '\0' || count(result, last) <= count(result, opening)) {
                break;
            }

            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    private static long count(String value, char target) {
        return value.chars().filter(c -> c == target).count();
    }
}