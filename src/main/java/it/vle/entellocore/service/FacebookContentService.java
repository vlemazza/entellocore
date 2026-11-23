package it.vle.entellocore.service;

import it.vle.entellocore.exception.ContentExtractionException;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostFacebook;
import it.vle.entellocore.model.SocialService;
import it.vle.entellocore.utils.FacebookUrlResolver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacebookContentService implements ContentService {

    private static final Logger LOGGER = Logger.getLogger(FacebookContentService.class.getName());

    private final ContentService galleryDlService;
    private final FacebookDescriptionEnricher descriptionEnricher;

    public FacebookContentService() {
        this(new GalleryDlService(), new YoutubeDlFacebookDescriptionEnricher());
    }

    public FacebookContentService(ContentService galleryDlService, FacebookDescriptionEnricher descriptionEnricher) {
        this.galleryDlService = galleryDlService;
        this.descriptionEnricher = descriptionEnricher;
    }

    @Override
    public ExtractedPost extract(String rawUrl, SocialService service) throws ContentExtractionException, IOException, InterruptedException {
        String url = FacebookUrlResolver.resolveFacebook(rawUrl);
        ExtractedPost post = galleryDlService.extract(url, service);
        if (!(post instanceof ExtractedPostFacebook facebookPost) || !facebookPost.isVideo()) {
            return post;
        }

        try {
            String description = descriptionEnricher.extractDescription(url);
            if (isBlank(description)) {
                return post;
            }
            return facebookPost.withDescription(description);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            try {
                post.close();
            } catch (IOException cleanupException) {
                exception.addSuppressed(cleanupException);
            }
            throw new ContentExtractionException("Facebook metadata enrichment was interrupted", exception);
        } catch (ContentExtractionException exception) {
            LOGGER.log(Level.WARNING, "Unable to enrich Facebook video description; using gallery-dl metadata", exception);
            return post;
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}