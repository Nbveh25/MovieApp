package com.example.homework.presentaion.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.app.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import androidx.core.content.withStyledAttributes

class CircularSectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val sectorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
    }

    private val rect = RectF()
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var innerRadius = 0f

    private var sectorCount = 3
    private var sectorColors = mutableListOf(
        Color.parseColor("#E57373"),
        Color.parseColor("#64B5F6"),
        Color.parseColor("#FFD54F")
    )

    private var selectedSector = -1

    private val path = Path()

    init {
        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.CircularSectorView) {
                sectorCount = getInt(R.styleable.CircularSectorView_sectorCount, 3)
            }
        }

        while (sectorColors.size < sectorCount) {
            sectorColors.add(getRandomColor())
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerX = w / 2f
        centerY = h / 2f
        radius = min(w, h) / 2f * 0.9f
        innerRadius = radius * 0.6f

        rect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        textPaint.textSize = radius * 0.3f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val sweepAngle = 360f / sectorCount
        var startAngle = 180f

        for (i in 0 until sectorCount) {
            drawSectorWithRoundedEnds(canvas, startAngle, sweepAngle, i)
            startAngle -= sweepAngle
        }

        val text = sectorCount.toString()
        canvas.drawText(text, centerX, centerY + textPaint.textSize / 3, textPaint)
    }

    private fun drawSectorWithRoundedEnds(canvas: Canvas, startAngle: Float, sweepAngle: Float, sectorIndex: Int) {
        val baseColor = sectorColors[sectorIndex % sectorColors.size]

        val sectorColor = if (sectorIndex == selectedSector) {
            lightenColor(baseColor)
        } else {
            baseColor
        }

        sectorPaint.color = sectorColor
        sectorPaint.style = Paint.Style.FILL

        val endAngle = startAngle - sweepAngle

        val thickness = radius - innerRadius
        val semicircleRadius = thickness / 2

        path.reset()

        val startRadialVectorX = cos(Math.toRadians(startAngle.toDouble())).toFloat()
        val startRadialVectorY = sin(Math.toRadians(startAngle.toDouble())).toFloat()
        val endRadialVectorX = cos(Math.toRadians(endAngle.toDouble())).toFloat()
        val endRadialVectorY = sin(Math.toRadians(endAngle.toDouble())).toFloat()

        val startSemicircleCenterX = centerX + (innerRadius + semicircleRadius) * startRadialVectorX
        val startSemicircleCenterY = centerY + (innerRadius + semicircleRadius) * startRadialVectorY
        val endSemicircleCenterX = centerX + (innerRadius + semicircleRadius) * endRadialVectorX
        val endSemicircleCenterY = centerY + (innerRadius + semicircleRadius) * endRadialVectorY

        val startSemicircleRect = RectF(
            startSemicircleCenterX - semicircleRadius,
            startSemicircleCenterY - semicircleRadius,
            startSemicircleCenterX + semicircleRadius,
            startSemicircleCenterY + semicircleRadius
        )

        val endSemicircleRect = RectF(
            endSemicircleCenterX - semicircleRadius,
            endSemicircleCenterY - semicircleRadius,
            endSemicircleCenterX + semicircleRadius,
            endSemicircleCenterY + semicircleRadius
        )

        path.moveTo(
            startSemicircleCenterX + semicircleRadius * startRadialVectorX,
            startSemicircleCenterY + semicircleRadius * startRadialVectorY
        )

        path.arcTo(startSemicircleRect, startAngle, 180f)

        val outerRect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        path.arcTo(outerRect, startAngle, -sweepAngle)

        path.arcTo(endSemicircleRect, endAngle, 180f)

        val innerRect = RectF(
            centerX - innerRadius,
            centerY - innerRadius,
            centerX + innerRadius,
            centerY + innerRadius
        )
        path.arcTo(innerRect, endAngle, sweepAngle)

        path.close()

        canvas.drawPath(path, sectorPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchX = event.x
                val touchY = event.y

                val dx = touchX - centerX
                val dy = touchY - centerY
                val distance = sqrt(dx.pow(2) + dy.pow(2))

                if (distance >= innerRadius && distance <= radius) {
                    val angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                    val positiveAngle = (angle + 360) % 360

                    val normalizedAngle = (180 - positiveAngle + 360) % 360
                    val sectorAngle = 360f / sectorCount
                    val touchedSector = (normalizedAngle / sectorAngle).toInt()

                    if (selectedSector != touchedSector) {
                        selectedSector = touchedSector
                        invalidate()
                    }
                    return true
                } else {
                    if (selectedSector != -1) {
                        selectedSector = -1
                        invalidate()
                    }
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun lightenColor(color: Int): Int {
        val factor = 0.6f
        val a = Color.alpha(color)
        val r = (Color.red(color) * (1 - factor) + 255 * factor).toInt()
        val g = (Color.green(color) * (1 - factor) + 255 * factor).toInt()
        val b = (Color.blue(color) * (1 - factor) + 255 * factor).toInt()
        return Color.argb(a, r.coerceAtMost(255), g.coerceAtMost(255), b.coerceAtMost(255))
    }

    private fun getRandomColor(): Int {
        val colors = arrayOf(
            "#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5",
            "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50",
            "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800"
        )
        return Color.parseColor(colors.random())
    }

    fun setSectorCount(count: Int) {
        if (count > 0 && count != sectorCount) {
            sectorCount = count
            // Ensure we have enough colors
            while (sectorColors.size < sectorCount) {
                sectorColors.add(getRandomColor())
            }
            selectedSector = -1
            invalidate()
        }
    }

}