package it.vle.entellocore.model;

import java.nio.file.Path;
import java.util.List;

public class ExtractedPostInstagram extends ExtractedPost{
    private final String username;
    public ExtractedPostInstagram(String author, String username, String text, List<Path> media, Long timestamp) {
        super(author, text, media, timestamp);
        this.username = username;
    }
    public String getUrlProfile(){
        return "https://www.instagram.com/" + username;
    }

}
