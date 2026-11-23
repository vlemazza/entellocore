package it.vle.entellocore.service;

import it.vle.entellocore.exception.ContentExtractionException;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.SocialService;

import java.io.IOException;

public interface ContentService {
    ExtractedPost extract(String url, SocialService service) throws ContentExtractionException, IOException, InterruptedException;
}
