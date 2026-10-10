package it.vle.entellocore.service;

import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.SocialService;

public interface ContentService {
    ExtractedPost extract(String url, SocialService service);
}
