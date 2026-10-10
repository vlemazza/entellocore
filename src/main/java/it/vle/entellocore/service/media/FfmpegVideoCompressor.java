package it.vle.entellocore.service.media;

import it.vle.entellocore.service.process.ExternalProcessRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public class FfmpegVideoCompressor implements VideoCompressor {

    private static final long MAX_AUDIO_BITRATE = 96_000;
    private static final double BITRATE_MARGIN = 0.90;
    private static final Duration FFPROBE_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration FFMPEG_TIMEOUT = Duration.ofMinutes(10);

    private final String ffmpegCommand;
    private final String ffprobeCommand;

    public FfmpegVideoCompressor() {
        this("ffmpeg", "ffprobe");
    }

    public FfmpegVideoCompressor(String ffmpegCommand, String ffprobeCommand) {
        this.ffmpegCommand = ffmpegCommand;
        this.ffprobeCommand = ffprobeCommand;
    }

    @Override
    public Path compress(Path source, long maxSizeBytes) {
        if (maxSizeBytes <= 0) {
            throw new IllegalArgumentException("Maximum file size must be greater than zero");
        }

        double durationSeconds = readDuration(source);
        long targetTotalBitrate = (long) (maxSizeBytes * 8.0 / durationSeconds * BITRATE_MARGIN);
        long audioBitrate = Math.min(targetTotalBitrate / 5, MAX_AUDIO_BITRATE);
        long videoBitrate = targetTotalBitrate - audioBitrate;
        Path output = this.createOutputPath(source);

        try {

            this.runFfmpeg(source, output, videoBitrate, audioBitrate);
            if (Files.size(output) > maxSizeBytes) {
                throw new MediaProcessingException(
                        "Compressed video exceeds maximum size of " + maxSizeBytes + " bytes: " + source
                );
            }
            return output;

        } catch (IOException e) {

            this.deleteOutput(output);
            throw new MediaProcessingException("Unable to compress video: " + source, e);

        } catch (RuntimeException e) {

            this.deleteOutput(output);
            throw e;
        }
    }

    private double readDuration(Path source) {
        String output = runCommand(List.of(
                ffprobeCommand,
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                source.toAbsolutePath().toString()
        ), FFPROBE_TIMEOUT, "Unable to read video duration with ffprobe: " + source).trim();

        try {
            double duration = Double.parseDouble(output);
            if (duration > 0 && Double.isFinite(duration)) {
                return duration;
            }
        } catch (NumberFormatException ignored) {

        }
        throw new MediaProcessingException("FFprobe returned an invalid video duration for: " + source);
    }

    private Path createOutputPath(Path source) {
        try {
            Path sourcePath = source.toAbsolutePath().normalize();
            return Files.createTempFile(sourcePath.getParent(), "entellocore-", ".mp4");
        } catch (IOException e) {
            throw new MediaProcessingException("Unable to create compressed video output for: " + source, e);
        }
    }

    private void runFfmpeg(Path source, Path output, long videoBitrate, long audioBitrate) {
        this.runCommand(List.of(
                ffmpegCommand,
                "-hide_banner", "-loglevel", "error", "-y",
                "-i", source.toAbsolutePath().toString(),
                "-map", "0:v:0",
                "-map", "0:a:0?",
                "-c:v", "libx264",
                "-preset", "fast",
                "-b:v", Long.toString(videoBitrate),
                "-maxrate", Long.toString(videoBitrate),
                "-bufsize", Long.toString(videoBitrate * 2),
                "-c:a", "aac",
                "-b:a", Long.toString(audioBitrate),
                "-pix_fmt", "yuv420p",
                "-movflags", "+faststart",
                output.toAbsolutePath().toString()
        ), FFMPEG_TIMEOUT, "FFmpeg failed to compress video: " + source);
    }

    private String runCommand(List<String> command, Duration timeout, String failureMessage) {
        try {
            return ExternalProcessRunner.run(command, timeout, failureMessage);
        } catch (IOException e) {
            throw new MediaProcessingException(failureMessage, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MediaProcessingException(failureMessage + " (interrupted)", e);
        }
    }

    private void deleteOutput(Path output) {
        try {
            Files.deleteIfExists(output);
        } catch (IOException ignored) {
        }
    }
}