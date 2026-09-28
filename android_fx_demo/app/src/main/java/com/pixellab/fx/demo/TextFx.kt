package com.pixellab.fx.demo

import android.graphics.*

data class TextFxConfig(
    val text: String,
    val typeface: Typeface? = null,
    val textSize: Float = 72f,
    val textColor: Int = Color.WHITE,
    val shadowColor: Int = Color.argb(180, 0, 0, 0),
    val shadowRadius: Float = 12f,
    val shadowDx: Float = 8f,
    val shadowDy: Float = 8f,
    val strokeColor: Int? = null,
    val strokeWidth: Float = 0f,
    val innerGlowColor: Int? = null,
    val innerGlowRadius: Float = 0f
)

object TextFx {

    fun renderTextBitmap(config: TextFxConfig): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = config.textColor
            textSize = config.textSize
            style = Paint.Style.FILL
            config.typeface?.let { typeface = it }
        }

        val bounds = Rect()
        paint.getTextBounds(config.text, 0, config.text.length, bounds)
        val padding = (config.shadowRadius + config.strokeWidth + 8).toInt()
        val w = bounds.width() + padding * 2
        val h = bounds.height() + padding * 2

        val bitmap = Bitmap.createBitmap(w.coerceAtLeast(1), h.coerceAtLeast(1), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw shadow / outer glow
        if (config.shadowRadius > 0f) {
            val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = config.shadowColor
                textSize = config.textSize
                style = Paint.Style.FILL
                config.typeface?.let { typeface = it }
                setShadowLayer(config.shadowRadius, config.shadowDx, config.shadowDy, config.shadowColor)
            }
            canvas.drawText(config.text, padding.toFloat() - bounds.left, padding.toFloat() - bounds.top, shadowPaint)
        }

        // Draw stroke
        config.strokeColor?.let { sColor ->
            if (config.strokeWidth > 0f) {
                val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = sColor
                    textSize = config.textSize
                    style = Paint.Style.STROKE
                    strokeWidth = config.strokeWidth
                    strokeJoin = Paint.Join.ROUND
                    config.typeface?.let { typeface = it }
                }
                canvas.drawText(config.text, padding.toFloat() - bounds.left, padding.toFloat() - bounds.top, strokePaint)
            }
        }

        // Main fill
        canvas.drawText(config.text, padding.toFloat() - bounds.left, padding.toFloat() - bounds.top, paint)

        // Inner glow (approximation)
        config.innerGlowColor?.let { igColor ->
            if (config.innerGlowRadius > 0f) {
                val tmp = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
                val tmpCanvas = Canvas(tmp)
                val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = igColor
                    style = Paint.Style.FILL
                    config.typeface?.let { typeface = it }
                    maskFilter = BlurMaskFilter(config.innerGlowRadius, BlurMaskFilter.Blur.NORMAL)
                }
                tmpCanvas.drawText(config.text, padding.toFloat() - bounds.left, padding.toFloat() - bounds.top, glowPaint)
                val composite = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
                val compCanvas = Canvas(composite)
                compCanvas.drawBitmap(bitmap, 0f, 0f, null)
                val p = Paint(Paint.ANTI_ALIAS_FLAG).apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP) }
                compCanvas.drawBitmap(tmp, 0f, 0f, p)
                return composite
            }
        }

        return bitmap
    }
}
