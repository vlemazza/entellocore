# EntelloCore

[![Java 25](https://img.shields.io/badge/Java-25-orange)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/build-Maven-C71A36)](https://maven.apache.org/)
[![Entelloverse](https://img.shields.io/badge/Entelloverse-10)]()
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)

EntelloCore is a shared Java library for extracting social media posts and their associated media from URLs found in text messages.

It centralizes URL detection, platform-specific extraction, URL normalization and media collection, providing an extraction interface for applications in the EntelloVerse ecosystem.

## EntelloVerse ecosystem

EntelloCore is currently used by:

- [Entellord](https://github.com/vlemazza/entellord) - A Discord bot.
- [Entellogram](https://github.com/vlemazza/entellogram) - A Telegram bot.

## Features

- Extract posts and media from URLs found in text messages.
- Support multiple social platforms.
- Normalize URLs from supported alternative frontends.
- Handle multiple URLs in a message independently.

## Supported platforms

- Twitter
- Bluesky
- TikTok
- Instagram
- Reddit
- Bilibili
- Pixiv
- Archive of Our Own
- Poipiku
- Tumblr
- Pinterest
- YouTube
- Hacker News

## Requirements

- Java 25
- Maven
- `gallery-dl`
- `yt-dlp`
- `ffmpeg`

## Usage

EntelloCore provides a shared interface for extracting content from text messages.

```
List<TextExtractionOutcome> outcomes =
entelloCore.extractFromText(message);
```

Each `TextExtractionOutcome` represents the result of processing a URL found in the input text.

Successfully extracted content is represented by `ExtractedPost` objects, which provide the metadata and media associated with the original post.

## Project structure

`entellocore` handles extraction and provides shared models. Applications built on it manage their own messaging integrations and choose how to display the extracted content.


## Gallery-dl configuration

EntelloCore reads the runtime `gallery-dl` configuration from the absolute path specified by the `GALLERY_DL_CONFIG` environment variable.

A reusable configuration template is provided in:

```config/gallery-dl.conf.example```

Copy it to the shared deployment configuration directory as `gallery-dl.conf` and update the required credentials and cookie paths.

Example directory structure:
```
config/
├── gallery-dl.conf
└── session/
├── cookies-instagram.txt
└── ...
```
Set the environment variable to the absolute configuration path:

```GALLERY_DL_CONFIG=/app/config/gallery-dl.conf```

Entellord and Entellogram mount the shared parent `config/` directory at `/app/config` in their containers. Both applications use the same configuration file:

```
/app/config/gallery-dl.conf
/app/config/session/
```