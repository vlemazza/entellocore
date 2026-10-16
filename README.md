# EntelloCore

[![Java 25](https://img.shields.io/badge/Java-25-orange)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/build-Maven-C71A36)](https://maven.apache.org/)
[![Entelloverse](https://img.shields.io/badge/Entelloverse-10)]()
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE)

Entellocore is the shared content extraction library used by EntelloVerse applications. It provides a common way to extract posts and media from URLs found in messages, leaving each application to handle its own platform integration.

It is currently used by [Entellord](https://github.com/vlemazza/entellord) a Discord bot. In future it will be reused by other applications such as Entellogram for Telegram.

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

## Project structure

`entellocore` handles extraction and provides shared models. Applications built on it manage their own messaging integrations and choose how to display the extracted content.

This keeps platform-specific code separate from URL detection, extractor selection, and media collection.