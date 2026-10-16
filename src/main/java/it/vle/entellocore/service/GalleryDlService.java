package it.vle.entellocore.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.vle.entellocore.api.BlueskyApi;
import it.vle.entellocore.exception.ContentExtractionException;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.ExtractedPostBluesky;
import it.vle.entellocore.model.SocialService;
import it.vle.entellocore.parser.MetadataParser;
import it.vle.entellocore.parser.MetadataParserFactory;
import it.vle.entellocore.service.process.ExternalProcessRunner;
import it.vle.entellocore.utils.CoreConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;


public class GalleryDlService implements ContentService {

    private static final Logger LOGGER = Logger.getLogger(GalleryDlService.class.getName());
    private static final Duration PROCESS_TIMEOUT = Duration.ofMinutes(5);

    private final Path galleryDlPath;
    private final ObjectMapper mapper;
    private final MetadataParserFactory parserFactory;
    private final BlueskyApi blueskyApi;

    public GalleryDlService() {
        this(Path.of("gallery-dl"), CoreConfiguration.galleryDlConfigPath(), new ObjectMapper());
    }

    public GalleryDlService(Path galleryDlPath, Path configurationPath, ObjectMapper mapper) {
        this.galleryDlPath = galleryDlPath;
        this.configurationPath = configurationPath;
        this.mapper = mapper;
        this.parserFactory = new MetadataParserFactory();
        this.blueskyApi = new BlueskyApi();
    }

    private final Path configurationPath;

    @Override
    public ExtractedPost extract(String url, SocialService service) throws ContentExtractionException {

        Path directory = null;
        ExtractedPost metadata = null;
        boolean extractionCompleted = false;
        try {
            MetadataParser parser = parserFactory.getParser(service);
            directory = Files.createTempDirectory("gallerydl-");
            List<String> command = List.of(
                    galleryDlPath.toString(),
                    "-c",
                    configurationPath.toString(),
                    "-D",
                    directory.toString(),
                    "--write-info-json",
                    url
            );
            ExternalProcessRunner.run(command, PROCESS_TIMEOUT, "gallery-dl failed");

            Path info = directory.resolve("info.json");
            JsonNode json = mapper.readTree(info.toFile());
            metadata = parser.parse(json);
            metadata.addTemporaryDirectory(directory);

            try (Stream<Path> files = Files.walk(directory)) {
                List<Path> media = files.filter(Files::isRegularFile).filter(path -> !path.equals(info)).toList();
                metadata.replaceMedia(media);
            }

            ExtractedPost enrichedPost = this.enrichBlueskyPost(metadata);
            extractionCompleted = true;
            return enrichedPost;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ContentExtractionException("gallery-dl was interrupted", e);
        } catch (IOException e) {
            throw new ContentExtractionException("Unable to extract content with gallery-dl", e);
        } finally {
            if (!extractionCompleted) {
                try {
                    if (metadata != null) {
                        metadata.close();
                    } else if (directory != null) {
                        this.deleteDirectory(directory);
                    }
                } catch (IOException cleanupException) {
                    LOGGER.log(Level.WARNING, "Unable to remove temporary gallery-dl files", cleanupException);
                }
            }
        }
    }

    private void deleteDirectory(Path directory) throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    private ExtractedPost enrichBlueskyPost(ExtractedPost post) throws ContentExtractionException {
        if (!(post instanceof ExtractedPostBluesky bluesky) || bluesky.getEmbeddedPostUri() == null) {
            return post;
        }

        String embeddedUrl = blueskyApi.getWebUrlFromAtUri(bluesky.getEmbeddedPostUri());
        if (embeddedUrl == null) {
            return post;
        }

        ExtractedPost embeddedPost = extract(embeddedUrl, SocialService.BLUESKY);
        if (embeddedPost instanceof ExtractedPostBluesky embeddedBluesky) {
            return bluesky.withEmbeddedPost(embeddedBluesky);
        }

        try {
            embeddedPost.close();
        } catch (IOException exception) {
            throw new ContentExtractionException("Unable to remove temporary files for embedded Bluesky post", exception);
        }
        return post;
    }
}
