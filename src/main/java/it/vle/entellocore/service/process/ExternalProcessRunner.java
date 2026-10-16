package it.vle.entellocore.service.process;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class ExternalProcessRunner {

    private static final int MAX_LOG_BYTES = 16 * 1024;

    public static String run(List<String> command, Duration timeout, String failureMessage) throws IOException, InterruptedException {
        Path outputFile = Files.createTempFile("entellocore-extract-", ".log");
        Process process = null;
        try {
            process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .redirectOutput(outputFile.toFile())
                    .start();
            if (!process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
                process.destroyForcibly();
                throw new IOException(failureMessage + " (timed out after " + timeout + ")");
            }

            String output = readOutputLog(outputFile);
            if (process.exitValue() != 0) {
                throw new IOException(failureMessage + " (exit code " + process.exitValue() + "): " + output);
            }
            return output;
        } catch (InterruptedException exception) {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
            throw exception;
        } finally {
            Files.deleteIfExists(outputFile);
        }
    }

    private static String readOutputLog(Path outputFile) throws IOException {
        long size = Files.size(outputFile);
        long skipped = Math.max(0, size - MAX_LOG_BYTES);

        try (InputStream input = Files.newInputStream(outputFile)) {
            input.skipNBytes(skipped);
            return new String(input.readAllBytes());
        }
    }
}
