package it.vle.entellocore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.vle.entellocore.model.ExtractedPost;
import it.vle.entellocore.model.SocialService;
import it.vle.entellocore.parser.MetadataParser;
import it.vle.entellocore.parser.MetadataParserFactory;
import it.vle.entellocore.service.process.ExternalProcessRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class YoutubeDlService implements ContentService {

    private static final Duration PROCESS_TIMEOUT = Duration.ofMinutes(5);
    private static final long MAX_VIDEO_DURATION_SECONDS = 3600;
    private static final String FORMAT =
            "bv[height<=720][vcodec^=avc1]+ba[acodec^=mp4a]/b[height<=720][vcodec^=avc1]";

    private final String ytDlpCommand;
    private final ObjectMapper mapper = new ObjectMapper();
    private final MetadataParserFactory parserFactory = new MetadataParserFactory();

    public YoutubeDlService() {
        this("yt-dlp");
    }

    public YoutubeDlService(String ytDlpCommand) {
        this.ytDlpCommand = ytDlpCommand;
    }

    @Override
    public ExtractedPost extract(String url, SocialService service) {
        if (service != SocialService.YOUTUBE) {
            throw new ContentExtractionException("Unsupported URL for yt-dlp");
        }

        Path directory = null;
        ExtractedPost post = null;
        boolean extractionCompleted = false;

        try {
            directory = Files.createTempDirectory("ytdlp-");
            ExternalProcessRunner.run(
                    this.buildCommand(url, directory),
                    PROCESS_TIMEOUT,
                    "yt-dlp failed"
            );

            Path infoFile = findInfoFile(directory);
            MetadataParser parser = parserFactory.getParser(service);
            post = parser.parse(mapper.readTree(infoFile.toFile()));
            post.addTemporaryDirectory(directory);
            List<Path> media;
            try (Stream<Path> files = Files.list(directory)) {
                media = files.filter(Files::isRegularFile)
                        .filter(path -> !path.getFileName().toString().endsWith(".info.json"))
                        .toList();
            }
            if (media.isEmpty()) {
                throw new ContentExtractionException(
                        "No video downloaded"
                );
            }

            post.replaceMedia(media);
            extractionCompleted = true;
            return post;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ContentExtractionException("yt-dlp was interrupted", exception);
        } catch (IOException exception) {
            throw new ContentExtractionException("Unable to extract video with yt-dlp", exception);
        } finally {
            if (!extractionCompleted && directory != null) {
                try {
                    if (post != null) {
                        post.close();
                    } else {
                        this.deleteDirectory(directory);
                    }
                } catch (IOException _) {
                }
            }
        }
    }

    List<String> buildCommand(String url, Path directory) {
        return List.of(
                ytDlpCommand,
                "--no-warnings",
                "--no-playlist",
                "--write-info-json",
                "--match-filter", "duration <= " + MAX_VIDEO_DURATION_SECONDS,
                "--format", FORMAT,
                "--merge-output-format", "mp4",
                "--output", directory.resolve("%(id)s.%(ext)s").toString(),
                url
        );
    }

    private Path findInfoFile(Path directory) throws IOException {
        try (Stream<Path> files = Files.list(directory)) {
            return files.filter(path -> path.getFileName().toString().endsWith(".info.json"))
                    .findFirst()
                    .orElseThrow(() -> new ContentExtractionException(
                            "yt-dlp returned no video metadata"
                    ));
        }
    }

    private void deleteDirectory(Path directory) throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }
}