package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostAO3 extends ExtractedPost {
    private final String title;

    private final List<String> rating;
    private final List<String> categories;
    private final List<String> fandoms;
    private final List<String> relationships;
    private final List<String> characters;
    private final int words;
    private final String chapters;
    private final int kudos;
    private final int hits;

    public ExtractedPostAO3(
            String author,
            String text,
            List<Path> media,
            Long timestamp,

            String title,

            List<String> rating,
            List<String> categories,
            List<String> fandoms,
            List<String> relationships,
            List<String> characters,


            int words,
            String chapters,
            int kudos,
            int hits
    ) {

        super(author, text, media, timestamp);

        this.title = title;

        this.rating = rating;
        this.categories = categories;
        this.fandoms = fandoms;
        this.relationships = relationships;
        this.characters = characters;
        this.words = words;
        this.chapters = chapters;
        this.kudos = kudos;
        this.hits = hits;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getRating() {
        return rating;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getFandoms() {
        return fandoms;
    }

    public List<String> getRelationships() {
        return relationships;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public int getWords() {
        return words;
    }

    public String getChapters() {
        return chapters;
    }

    public int getKudos() {
        return kudos;
    }

    public int getHits() {
        return hits;
    }

}