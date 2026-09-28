# Android FX Demo

This is a runnable demo project that recreates a Photoshop-like FX panel in Android.

## Included features

- Drop Shadow
- Inner Shadow
- Outer Glow
- Inner Glow
- Bevel & Emboss
- Color Overlay
- Gradient Overlay
- Pattern Overlay
- Stroke
- Photoshop-style text FX presets
- Text shadow, glow, stroke, gradient, and bevel effects

## Structure

- `app/` contains the Android app module
- `FxPanelView.kt` renders layer effects
- `TextFx.kt` renders Photoshop-like text FX presets
- `MainActivity.kt` contains demo UI, preset switcher, and control panel

## Text FX workflow

The demo includes a lightweight text-effect renderer for quick visual exploration. It is designed to mimic common Photoshop text styling layers, including:

- Drop shadow with position and blur controls
- Outer glow effect preview
- Stroke effect with strong contrast
- Gradient fill for premium headline look
- Bevel-style highlight and shadow

This is a concept prototype for UI/UX iteration and can be expanded into a larger layer-based text effect editor.

## Notes

This is a prototype UI for visualizing how a Photoshop-style FX panel can work in Android. It is intended to be used as a design and prototyping base when the original PixelLab source is restored.
