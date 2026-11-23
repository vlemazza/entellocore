package it.vle.entellocore.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.vle.entellocore.exception.ContentExtractionException;
import it.vle.entellocore.parser.FacebookMetadataParser;
import it.vle.entellocore.service.process.ExternalProcessRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class YoutubeDlFacebookDescriptionEnricher implements FacebookDescriptionEnricher {

    private static final Logger LOGGER = Logger.getLogger(YoutubeDlFacebookDescriptionEnricher.class.getName());
    private static final Duration PROCESS_TIMEOUT = Duration.ofMinutes(5);

    private final String ytDlpCommand;
    private final ObjectMapper mapper;
    private final FacebookMetadataParser metadataParser;

    public YoutubeDlFacebookDescriptionEnricher() {
        this("yt-dlp", new ObjectMapper());
    }

    YoutubeDlFacebookDescriptionEnricher(String ytDlpCommand, ObjectMapper mapper) {
        this.ytDlpCommand = ytDlpCommand;
        this.mapper = mapper;
        this.metadataParser = new FacebookMetadataParser();
    }

    @Override
    public String extractDescription(String url) throws ContentExtractionException, InterruptedException {
        Path directory = null;
        try {
            directory = Files.createTempDirectory("facebook-ytdlp-");
            ExternalProcessRunner.run(
                    buildCommand(url, directory),
                    PROCESS_TIMEOUT,
                    "yt-dlp Facebook metadata extraction failed"
            );

            Path infoFile = findInfoFile(directory);
            JsonNode json = mapper.readTree(infoFile.toFile());
            String description = metadataParser.parseDescription(json);
            return description == null || description.isBlank() ? null : description;
        } catch (IOException | ContentExtractionException exception) {
            throw new ContentExtractionException("Unable to extract Facebook video description with yt-dlp", exception);
        } finally {
            if (directory != null) {
                deleteDirectory(directory);
            }
        }
    }

    List<String> buildCommand(String url, Path directory) {
        return List.of(
                ytDlpCommand,
                "--no-warnings",
                "--no-playlist",
                "--skip-download",
                "--write-info-json",
                "--output",
                directory.resolve("%(id)s.%(ext)s").toString(),
                url
        );
    }

    private Path findInfoFile(Path directory) throws IOException, ContentExtractionException {
        try (Stream<Path> files = Files.list(directory)) {
            return files.filter(path -> path.getFileName().toString().endsWith(".info.json"))
                    .findFirst()
                    .orElseThrow(() -> new ContentExtractionException("yt-dlp returned no Facebook metadata"));
        }
    }

    private void deleteDirectory(Path directory) {
        try (Stream<Path> paths = Files.walk(directory)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "Unable to remove temporary Facebook yt-dlp files", exception);
        }
    }
}