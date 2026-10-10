package it.vle.entellocore.utils;

import java.nio.file.Files;
import java.nio.file.Path;

public class CoreConfigValidator {

    public static void validate() {
        checkGalleryDl();
        checkYoutubeDl();

        Path configPath = CoreConfiguration.galleryDlConfigPath();

        if (!Files.exists(configPath)) {
            throw new RuntimeException("gallery-dl config not found: " + configPath);
        }

        System.out.println("Entellocore configuration OK");
    }

    public static void checkGalleryDl() {
        try {
            Process process = new ProcessBuilder("gallery-dl", "--version").redirectErrorStream(true).start();
            String output = new String(process.getInputStream().readAllBytes());
            int exit = process.waitFor();

            if (exit != 0) {
                throw new RuntimeException("gallery-dl failed: " + output);
            }

            System.out.println("gallery-dl OK: " + output.trim());
        } catch (Exception e) {
            throw new RuntimeException("gallery-dl not installed", e);
        }
    }

    public static void checkYoutubeDl() {
        try {
            Process process = new ProcessBuilder("yt-dlp", "--version").redirectErrorStream(true).start();
            String output = new String(process.getInputStream().readAllBytes());
            int exit = process.waitFor();

            if (exit != 0) {
                throw new RuntimeException("yt-dlp failed: " + output);
            }

            System.out.println("yt-dlp OK: " + output.trim());
        } catch (Exception e) {
            throw new RuntimeException("yt-dlp not installed", e);
        }
    }
}
