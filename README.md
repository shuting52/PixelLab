# PixelLab

PixelLab is a design and prototyping repository for Android app UI development, layered effect exploration, and AI-assisted product iteration. The workspace currently contains both the extracted Android resources and a runnable demo project for a Photoshop-like FX panel.

## Project status

This repo is best understood as a hybrid project state:

- It preserves the original Android app extraction and asset library
- It includes a standalone demo under the Android FX prototype area
- It is suitable for further development by AI agents, designers, and Android engineers

The current repo includes:

- Android resource folders and metadata
- extracted app assets and package content
- a prototype Android app for FX editing and UI experiments
- a remote version-checking and update popup concept

## Repository structure

- assets/ — image assets, fonts, stickers, effects, presets, background packs
- res/ — Android resource XML, drawables, layouts, styles, menus
- PixelLab/ — project-specific app content
- android_fx_demo/ — runnable Android sample app for FX panel prototype
- app_version.json — remote version metadata for update detection
- .github/workflows/ — CI workflow and automation hooks

## Android FX demo overview

The demo app under [android_fx_demo](android_fx_demo) is designed to simulate a Photoshop-style layer FX panel and UI pipeline.

Current features include:

- Drop shadow
- Inner shadow
- Outer glow
- Inner glow
- Bevel and emboss
- Color overlay
- Gradient overlay
- Pattern overlay
- Stroke controls
- Photoshop-style text FX presets
- Text shadow / glow / stroke / gradient / bevel preview
- Version update detection flow
- Animated update dialog

## How to continue developing with AI Agent

This project is intentionally structured to support second-stage development through AI-driven iteration. The typical workflow is:

1. Define the business goal clearly
2. Provide the project context and constraints to the AI agent
3. Let the agent read the relevant code, modify files, and produce a patch
4. Review the output and verify the result
5. Commit only after the change is tested or validated as far as possible

### Recommended development pattern

Use a session structure like this:

- Goal: implement a new feature or bug fix
- Scope: only the relevant module or demo folder
- Constraints: no full app rewrite, keep compatibility with the current Android setup
- Verification: run project checks or highlight environment limits if SDK is unavailable

### Example agent prompts

Use prompts such as the following:

- Improve the Android FX demo to look more like Adobe Photoshop layer styles
- Add a modern version update dialog with CSS-like animation and download progress
- Refactor the FX panel to support live preview of multiple effects at once
- Add a settings page for customizing brush, shadow, and glow presets
- Build a reusable update-check service that reads a remote JSON version manifest

### Good prompt structure

A strong AI prompt should include:

- project background
- target module or file
- expected behavior
- UI/UX style reference
- constraints such as Android SDK availability or no new dependency policy
- verification expectation

Example:

I am working on the Android FX demo inside this repo. Please read the existing FX panel code and implement a new version update dialog with modern CSS-inspired motion, dark theme styling, and a download progress state. Keep the change isolated to the demo app and do not break the current UI structure.

## Recommended second-stage AI workflow

### 1. Start with a narrow task

Avoid asking the agent to rewrite the entire app at once. Prefer smaller goals such as:

- implement a new update popup
- add a preset system
- improve the FX list UX
- support live toggle states

### 2. Ask for code references

Request the agent to identify the exact files involved before editing:

- MainActivity
- FxPanelView
- UpdateDialogActivity
- VersionCheckService
- AndroidManifest

### 3. Keep the scope realistic

The repo is partially extracted and does not include a full production Android Gradle source tree. The AI agent should be told to work within the demo app or extracted resource environment rather than assuming a complete original project exists.

### 4. Validate what is possible

If Android SDK is not available in the environment, the AI agent should still produce code and note the limitations honestly. Do not assume runtime verification is possible without Gradle or Android tooling.

## Remote update concept

The repo includes a version metadata file at [app_version.json](app_version.json). The intended flow is:

- app checks remote JSON
- compares current versionCode to remote versionCode
- shows update popup when a newer version exists
- opens the release URL or install page

This is designed to support a future remote update mechanism for app distribution and release validation.

## Development philosophy

This project is suitable for iterative enhancement rather than one-shot build completion. The best path is:

- prototype fast
- refine UI interactions
- keep logic modular
- validate code quality with static inspection and targeted checks
- prepare for final integration when the full original Android source is available

## Practical next steps

If continuing the project with AI agent support, the next high-value tasks are:

- upgrade the FX panel to support real layered effects preview
- add preset save and import logic
- refine the animated update dialog UX
- create a tighter GitHub release versioning workflow
- connect a remote release manifest to app startup checks

## Notes

This repo should be treated as a preserved project snapshot and prototype environment. It is well suited for AI-assisted iteration, UI concept development, and Android feature prototyping while the original source tree remains incomplete.
