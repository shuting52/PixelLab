# Photoshop FX Panel Prototype

This directory contains a drop-in Android prototype for a Photoshop-style FX panel.

It is designed for the case where the original app source is missing and you want to restore the effect system in a clean, reusable way.

## Included effects

- Drop shadow
- Inner shadow
- Outer glow
- Inner glow
- Bevel and emboss
- Color overlay
- Gradient overlay
- Pattern overlay
- Stroke

## Integration

1. Copy `FxPanelPrototype.kt` into your app module, for example:
   `app/src/main/java/com/pixellab/fx/FxPanelPrototype.kt`
2. Add a `FxPanelView` to your layout.
3. Configure effects with `setEffects(...)`.
4. Feed an image or layer bitmap into the view.

## Example

```kotlin
val fx = listOf(
    FxConfig(FxType.DROP_SHADOW, color = Color.argb(128, 0, 0, 0), radius = 18f, distance = 12f),
    FxConfig(FxType.OUTER_GLOW, color = Color.argb(180, 86, 146, 255), radius = 24f),
    FxConfig(FxType.STROKE, color = Color.WHITE, strokeWidth = 4f)
)
fxPanelView.setEffects(fx)
fxPanelView.setSourceBitmap(bitmap)
```

## Notes

This is a prototype and not a full port of Photoshop itself. It gives the core visual behavior expected from a Photoshop FX panel and is meant to be expanded into a production layer engine when the original app source becomes available.
