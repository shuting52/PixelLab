package com.pixellab.fx.demo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.Shader
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var fxView: FxPanelView
    private lateinit var selectedLabel: TextView
    private lateinit var effectList: LinearLayout

    private var selectedType: FxType = FxType.DROP_SHADOW
    private var opacityValue = 0.75f
    private var distanceValue = 18f
    private var sizeValue = 20f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#121922"))
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val topBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(24, 18, 24, 18)
            setBackgroundColor(Color.parseColor("#1b2330"))
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val title = TextView(this).apply {
            text = "Photoshop FX"
            textSize = 20f
            setTextColor(Color.WHITE)
        }
        val action = TextView(this).apply {
            text = "Layer Style"
            textSize = 12f
            setTextColor(Color.parseColor("#9ab5d1"))
            gravity = Gravity.END
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        }
        topBar.addView(title)
        topBar.addView(action)
        root.addView(topBar)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val leftPanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#181f2a"))
            layoutParams = ViewGroup.LayoutParams(
                260,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(12, 12, 12, 12)
        }

        val effectTitle = TextView(this).apply {
            text = "Effects"
            textSize = 14f
            setTextColor(Color.parseColor("#b4c4d8"))
            setPadding(12, 8, 12, 12)
        }
        leftPanel.addView(effectTitle)

        effectList = leftPanel
        val effectNames = listOf(
            "Drop Shadow" to FxType.DROP_SHADOW,
            "Inner Shadow" to FxType.INNER_SHADOW,
            "Outer Glow" to FxType.OUTER_GLOW,
            "Inner Glow" to FxType.INNER_GLOW,
            "Bevel & Emboss" to FxType.BEVEL_EMBOSS,
            "Color Overlay" to FxType.COLOR_OVERLAY,
            "Gradient Overlay" to FxType.GRADIENT_OVERLAY,
            "Pattern Overlay" to FxType.PATTERN_OVERLAY,
            "Stroke" to FxType.STROKE
        )

        effectNames.forEach { (label, type) ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setBackgroundColor(if (type == selectedType) Color.parseColor("#24314b") else Color.parseColor("#1d2531"))
                setPadding(8, 6, 8, 6)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val check = CheckBox(this).apply {
                isChecked = type == selectedType
                isEnabled = false
                buttonTintList = android.content.res.ColorStateList.valueOf(
                    if (type == selectedType) Color.parseColor("#7db4ff") else Color.parseColor("#7a8394")
                )
            }

            val button = Button(this, null, android.R.style.Widget_MaterialButton_OutlinedButton).apply {
                text = label
                textSize = 12f
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.TRANSPARENT)
                setPadding(8, 8, 8, 8)
                minHeight = 0
                height = 52
                gravity = Gravity.START or Gravity.CENTER_VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
                setOnClickListener {
                    selectedType = type
                    selectedLabel.text = label
                    updateSelectedEffect()
                    refreshEffectRows(effectNames)
                }
            }

            row.addView(check)
            row.addView(button)
            leftPanel.addView(row)
        }

        val rightPanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#111820"))
            layoutParams = ViewGroup.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1f
            )
            setPadding(20, 20, 20, 20)
        }

        val previewCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#1b2330"))
            setPadding(18, 18, 18, 18)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                420
            )
        }

        val previewHeader = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val previewTitle = TextView(this).apply {
            text = "Preview"
            textSize = 14f
            setTextColor(Color.parseColor("#dfe7f6"))
        }
        previewHeader.addView(previewTitle)

        selectedLabel = TextView(this).apply {
            text = "Drop Shadow"
            textSize = 12f
            setTextColor(Color.parseColor("#7db4ff"))
            gravity = Gravity.END
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        }
        previewHeader.addView(selectedLabel)
        previewCard.addView(previewHeader)

        val previewArea = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        fxView = FxPanelView(this).apply {
            layoutParams = ViewGroup.LayoutParams(300, 300)
            setBackgroundColor(Color.parseColor("#0f141d"))
            setSourceBitmap(createDemoBitmap())
        }
        previewArea.addView(fxView)

        // Add Text FX preview below the FX panel
        val textPreview = TextView(this).apply {
            text = "PS FX"
            textSize = 36f
            setTextColor(Color.WHITE)
            setPadding(0, 12, 0, 0)
        }

        val textBitmap = TextFx.renderTextBitmap(
            TextFxConfig(
                text = "PS FX",
                textSize = 64f,
                textColor = Color.WHITE,
                shadowColor = Color.argb(200, 0, 0, 0),
                shadowRadius = 18f,
                shadowDx = 6f,
                shadowDy = 6f,
                strokeColor = Color.BLACK,
                strokeWidth = 6f,
                innerGlowColor = Color.argb(120, 255, 255, 255),
                innerGlowRadius = 6f
            )
        )

        val tb = android.widget.ImageView(this).apply {
            setImageBitmap(textBitmap)
            setPadding(0, 12, 0, 0)
        }
        previewArea.addView(tb)
        previewCard.addView(previewArea)
        rightPanel.addView(previewCard)

        val controlsCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#171e2a"))
            setPadding(18, 18, 18, 18)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }

        val controlsTitle = TextView(this).apply {
            text = "Layer Style"
            textSize = 16f
            setTextColor(Color.WHITE)
            setPadding(0, 0, 0, 12)
        }
        controlsCard.addView(controlsTitle)

        val swatchRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, 8, 0, 8)
        }

        val swatches = listOf(
            Color.parseColor("#7db4ff"),
            Color.parseColor("#ff9d41"),
            Color.parseColor("#ff5ca8"),
            Color.parseColor("#7ef0c1")
        )
        swatches.forEach { color ->
            val swatch = View(this).apply {
                setBackgroundColor(color)
                layoutParams = LinearLayout.LayoutParams(36, 36).apply {
                    setMargins(0, 0, 12, 0)
                }
            }
            swatchRow.addView(swatch)
        }
        controlsCard.addView(swatchRow)

        val sliderGroup = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        sliderGroup.addView(makeSliderRow("Opacity", 0f, 100f, opacityValue * 100f) { value ->
            opacityValue = value / 100f
            updateSelectedEffect()
        })
        sliderGroup.addView(makeSliderRow("Distance", 0f, 60f, distanceValue) { value ->
            distanceValue = value
            updateSelectedEffect()
        })
        sliderGroup.addView(makeSliderRow("Size", 0f, 80f, sizeValue) { value ->
            sizeValue = value
            updateSelectedEffect()
        })
        controlsCard.addView(sliderGroup)

        val actionBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
            setPadding(0, 18, 0, 0)
        }
        val cancel = Button(this).apply {
            text = "Cancel"
            setBackgroundColor(Color.parseColor("#2a3340"))
            setTextColor(Color.WHITE)
        }
        val apply = Button(this).apply {
            text = "Apply"
            setBackgroundColor(Color.parseColor("#2a74ff"))
            setTextColor(Color.WHITE)
        }
        val textFxBtn = Button(this).apply {
            text = "Text FX"
            setBackgroundColor(Color.parseColor("#334a6b"))
            setTextColor(Color.WHITE)
            setOnClickListener {
                // show a simple text fx example in fxView
                val tb = TextFx.renderTextBitmap(
                    TextFxConfig(
                        text = "Hello",
                        textSize = 88f,
                        textColor = Color.parseColor("#ffd700"),
                        shadowColor = Color.argb(220, 0, 0, 0),
                        shadowRadius = 24f,
                        shadowDx = 10f,
                        shadowDy = 10f,
                        strokeColor = Color.parseColor("#6b4f00"),
                        strokeWidth = 8f,
                        innerGlowColor = Color.argb(120, 255, 255, 255),
                        innerGlowRadius = 6f
                    )
                )
                fxView.setSourceBitmap(tb)
            }
        }
        actionBar.addView(cancel)
        actionBar.addView(textFxBtn)
        actionBar.addView(apply)
        controlsCard.addView(actionBar)

        rightPanel.addView(controlsCard)
        content.addView(leftPanel)
        content.addView(rightPanel)
        root.addView(content)
        setContentView(root)

        VersionCheckService.checkForUpdate(this, 1, "1.0")

        refreshEffectRows(effectNames)
        updateSelectedEffect()
    }

    private fun refreshEffectRows(effectNames: List<Pair<String, FxType>>) {
        for (i in 0 until effectList.childCount) {
            val child = effectList.getChildAt(i) as? LinearLayout ?: continue
            val checkbox = child.getChildAt(0) as? CheckBox
            val button = child.getChildAt(1) as? Button
            val label = button?.text?.toString() ?: ""
            val match = effectNames.firstOrNull { it.first == label }?.second ?: selectedType
            child.setBackgroundColor(if (match == selectedType) Color.parseColor("#24314b") else Color.parseColor("#1d2531"))
            checkbox?.isChecked = match == selectedType
        }
    }

    private fun makeSliderRow(
        labelText: String,
        min: Float,
        max: Float,
        current: Float,
        onChange: (Float) -> Unit
    ): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 12, 0, 12)
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val label = TextView(this).apply {
            text = labelText
            textSize = 12f
            setTextColor(Color.parseColor("#dfe7f6"))
        }
        val value = TextView(this).apply {
            text = current.toInt().toString()
            textSize = 12f
            setTextColor(Color.parseColor("#7db4ff"))
            gravity = Gravity.END
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        header.addView(label)
        header.addView(value)
        row.addView(header)

        val seekBar = SeekBar(this).apply {
            max = 100
            progress = current.toInt().coerceIn(0, 100)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val v = min + (max - min) * (progress / 100f)
                    value.text = v.toInt().toString()
                    onChange(v)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            })
        }
        row.addView(seekBar)
        return row
    }

    private fun updateSelectedEffect() {
        val fx = FxConfig(
            type = selectedType,
            color = resolveColor(selectedType),
            opacity = opacityValue,
            distance = distanceValue,
            radius = sizeValue,
            angle = 315f,
            strokeWidth = 6f
        )
        selectedLabel.text = when (selectedType) {
            FxType.DROP_SHADOW -> "Drop Shadow"
            FxType.INNER_SHADOW -> "Inner Shadow"
            FxType.OUTER_GLOW -> "Outer Glow"
            FxType.INNER_GLOW -> "Inner Glow"
            FxType.BEVEL_EMBOSS -> "Bevel & Emboss"
            FxType.COLOR_OVERLAY -> "Color Overlay"
            FxType.GRADIENT_OVERLAY -> "Gradient Overlay"
            FxType.PATTERN_OVERLAY -> "Pattern Overlay"
            FxType.STROKE -> "Stroke"
        }
        fxView.setEffects(listOf(fx))
    }

    private fun createDemoBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(320, 320, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f,
                0f,
                320f,
                320f,
                intArrayOf(
                    Color.parseColor("#FF7A18"),
                    Color.parseColor("#FF3D81"),
                    Color.parseColor("#7A5CFF")
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(
            RectF(28f, 28f, 292f, 292f),
            48f,
            48f,
            paint
        )
        return bitmap
    }

    private fun resolveColor(type: FxType): Int {
        return when (type) {
            FxType.DROP_SHADOW -> Color.argb(180, 20, 20, 30)
            FxType.INNER_SHADOW -> Color.argb(180, 0, 0, 0)
            FxType.OUTER_GLOW -> Color.argb(190, 98, 139, 255)
            FxType.INNER_GLOW -> Color.argb(210, 255, 170, 0)
            FxType.BEVEL_EMBOSS -> Color.argb(180, 220, 220, 220)
            FxType.COLOR_OVERLAY -> Color.argb(170, 255, 127, 80)
            FxType.GRADIENT_OVERLAY -> Color.argb(180, 120, 80, 255)
            FxType.PATTERN_OVERLAY -> Color.argb(180, 39, 196, 198)
            FxType.STROKE -> Color.argb(220, 255, 255, 255)
        }
    }
}
