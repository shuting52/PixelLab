package com.pixellab.fx.demo

import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface

enum class TextFxType {
    DROP_SHADOW,
    OUTER_GLOW,
    STROKE,
    GRADIENT_FILL,
    INNER_GLOW,
    BEVEL
}

data class TextFxConfig(
    val text: String,
    val type: TextFxType = TextFxType.DROP_SHADOW,
    val typeface: Typeface? = null,
    val textSize: Float = 72f,
    val fillColor: Int = Color.WHITE,
    val shadowColor: Int = Color.argb(180, 0, 0, 0),
    val shadowRadius: Float = 12f,
    val shadowDx: Float = 8f,
    val shadowDy: Float = 8f,
    val strokeColor: Int? = null,
    val strokeWidth: Float = 0f,
    val glowColor: Int? = null,
    val glowRadius: Float = 0f,
    val gradientColors: IntArray = intArrayOf(Color.parseColor("#FFB347"), Color.parseColor("#FF5E62"), Color.parseColor("#7A5CFF")),
    val bevelHighlight: Int = Color.argb(140, 255, 255, 255),
    val bevelShadow: Int = Color.argb(140, 30, 30, 30),
    val bevelDepth: Float = 8f
)

object TextFx {

    fun renderTextBitmap(config: TextFxConfig): Bitmap {
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = config.fillColor
            textSize = config.textSize
            style = Paint.Style.FILL
            isDither = true
            config.typeface?.let { typeface = it }
        }

        val bounds = Rect()
        textPaint.getTextBounds(config.text, 0, config.text.length, bounds)
        val pad = (config.shadowRadius + config.strokeWidth + config.glowRadius + 18f).toInt().coerceAtLeast(24)
        val width = bounds.width() + pad * 2
        val height = bounds.height() + pad * 2

        val output = Bitmap.createBitmap(width.coerceAtLeast(1), height.coerceAtLeast(1), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val baseX = pad.toFloat() - bounds.left
        val baseY = pad.toFloat() - bounds.top

        when (config.type) {
            TextFxType.DROP_SHADOW -> drawDropShadow(canvas, config, baseX, baseY)
            TextFxType.OUTER_GLOW -> drawOuterGlow(canvas, config, baseX, baseY)
            TextFxType.STROKE -> drawStroke(canvas, config, baseX, baseY)
            TextFxType.GRADIENT_FILL -> drawGradientFill(canvas, config, baseX, baseY)
            TextFxType.INNER_GLOW -> drawInnerGlow(canvas, config, baseX, baseY)
            TextFxType.BEVEL -> drawBevel(canvas, config, baseX, baseY)
        }

        return output
    }

    private fun drawDropShadow(canvas: Canvas, config: TextFxConfig, x: Float, y: Float) {
        if (config.shadowRadius <= 0f) {
            drawFill(canvas, config, x, y)
            return
        }

        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = config.shadowColor
            textSize = config.textSize
            style = Paint.Style.FILL
            config.typeface?.let { typeface = it }
            setShadowLayer(config.shadowRadius, config.shadowDx, config.shadowDy, config.shadowColor)
        }
        canvas.drawText(config.text, x + config.shadowDx, y + config.shadowDy, shadowPaint)
        drawFill(canvas, config, x, y)
    }

    private fun drawOuterGlow(canvas: Canvas, config: TextFxConfig, x: Float, y: Float) {
        val glow = config.glowColor ?: Color.argb(200, 120, 200, 255)
        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = glow
            textSize = config.textSize
            style = Paint.Style.FILL
            config.typeface?.let { typeface = it }
            setShadowLayer(config.glowRadius.coerceAtLeast(2f), 0f, 0f, glow)
        }
        canvas.drawText(config.text, x, y, glowPaint)
        drawFill(canvas, config, x, y)
    }

    private fun drawStroke(canvas: Canvas, config: TextFxConfig, x: Float, y: Float) {
        val strokeColor = config.strokeColor ?: Color.BLACK
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = strokeColor
            textSize = config.textSize
            style = Paint.Style.STROKE
            strokeWidth = config.strokeWidth.coerceAtLeast(1f)
            strokeJoin = Paint.Join.ROUND
            config.typeface?.let { typeface = it }
        }
        canvas.drawText(config.text, x, y, strokePaint)
        drawFill(canvas, config, x, y)
    }

    private fun drawGradientFill(canvas: Canvas, config: TextFxConfig, x: Float, y: Float) {
        val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = config.textSize
            style = Paint.Style.FILL
            config.typeface?.let { typeface = it }
            shader = LinearGradient(
                x,
                y - config.textSize,
                x + config.textSize * 2,
                y + config.textSize,
                config.gradientColors,
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawText(config.text, x, y, gradientPaint)
    }

    private fun drawInnerGlow(canvas: Canvas, config: TextFxConfig, x: Float, y: Float) {
        val base = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        val baseCanvas = Canvas(base)
        drawFill(baseCanvas, config, x, y)

        val glowColor = config.glowColor ?: Color.argb(200, 255, 255, 255)
        val glow = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        val glowCanvas = Canvas(glow)
        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = glowColor
            textSize = config.textSize
            style = Paint.Style.FILL
            config.typeface?.let { typeface = it }
            maskFilter = BlurMaskFilter(config.glowRadius.coerceAtLeast(4f), BlurMaskFilter.Blur.NORMAL)
        }
        glowCanvas.drawText(config.text, x, y, glowPaint)

        val merged = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        val mergedCanvas = Canvas(merged)
        mergedCanvas.drawBitmap(base, 0f, 0f, null)
        val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        }
        mergedCanvas.drawBitmap(glow, 0f, 0f, maskPaint)
        canvas.drawBitmap(merged, 0f, 0f, null)
    }

    private fun drawBevel(canvas: Canvas, config: TextFxConfig, x: Float, y: Float) {
        val highlightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = config.bevelHighlight
            textSize = config.textSize
            style = Paint.Style.FILL
            config.typeface?.let { typeface = it }
        }
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = config.bevelShadow
            textSize = config.textSize
            style = Paint.Style.FILL
            config.typeface?.let { typeface = it }
        }

        canvas.drawText(config.text, x + config.bevelDepth, y + config.bevelDepth, shadowPaint)
        canvas.drawText(config.text, x - config.bevelDepth / 2f, y - config.bevelDepth / 2f, highlightPaint)
        drawFill(canvas, config, x, y)
    }

    private fun drawFill(canvas: Canvas, config: TextFxConfig, x: Float, y: Float) {
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = config.fillColor
            textSize = config.textSize
            style = Paint.Style.FILL
            config.typeface?.let { typeface = it }
        }
        canvas.drawText(config.text, x, y, fillPaint)
    }
}
