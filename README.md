# PixelLab

PixelLab is an Android project workspace currently stored as a resource-heavy app extraction. This repository contains the app's Android package structure, compiled resources, asset bundles, and metadata needed to preserve the project state in GitHub.

## Project status

This workspace is not a complete Gradle Android source project yet. It appears to contain an extracted Android app structure rather than the full original source tree. The main project artifacts present include:

- Android manifest
- compiled resources and assets
- resource folders under `res/`
- app package files and APK-related payloads
- generated metadata for the app

## Included files

The repository currently contains directories such as:

- `assets/` for images, fonts, stickers, effects, and presets
- `res/` for Android XML drawables, layouts, values, menus, and XML configs
- `PixelLab/` for project-specific app content
- `AndroidManifest.xml` and compiled resources like `resources.arsc`

## Build status

A GitHub Actions workflow is already configured at [.github/workflows/build.yml](.github/workflows/build.yml). It checks for a standard Android Gradle project and will run automatically when the missing project files are restored.

At the moment, the workflow is intentionally safe: it exits gracefully if `gradlew` and Gradle settings files are not present, because the full original Android project files are not in this workspace.

## What is needed to build the APK

To compile this app normally, you need the original Android source project files, including at least:

- `gradlew`
- `settings.gradle` or `settings.gradle.kts`
- `build.gradle` / `build.gradle.kts`
- module-level Gradle config
- Android SDK configuration and signing setup

Once those files are restored, the GitHub Action will build the debug APK and release bundle automatically.

## Repository setup

This repository is already connected to GitHub and the default branch is `main`.

## Notes

This is best treated as a preserved Android app snapshot or extracted asset repository. If the original project source is recovered later, this repo can be upgraded into a fully buildable Android project without losing the asset and resource history already tracked in Git.
