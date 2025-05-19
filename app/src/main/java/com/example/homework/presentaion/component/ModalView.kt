package com.example.homework.presentaion.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.max

@Composable
fun GraphView(values: List<Float>, modifier: Modifier = Modifier) {
    val lineColor = Color.Blue
    val fillColor = lineColor.copy(alpha = 0.3f)
    val axisColor = Color.Gray
    val textColor = Color.Black
    val padding = 40f
    val dotRadius = 5f

    Canvas(modifier = modifier) {
        val width = size.width - padding * 2
        val height = size.height - padding * 2
        val maxValue = values.maxOrNull() ?: 1f
        val scaledMax = max(1f, maxValue)
        val stepX = width / (values.size - 1)
        val zeroY = size.height - padding

        drawLine(
            color = axisColor,
            start = Offset(padding, padding),
            end = Offset(padding, zeroY),
            strokeWidth = 2f
        )
        drawLine(
            color = axisColor,
            start = Offset(padding, zeroY),
            end = Offset(size.width - padding / 2, zeroY),
            strokeWidth = 2f
        )

        val yStep = scaledMax / 5
        for (i in 0..5) {
            val yValue = i * yStep
            val yPos = zeroY - (yValue / scaledMax) * height
            drawLine(
                color = axisColor.copy(alpha = 0.3f),
                start = Offset(padding, yPos),
                end = Offset(size.width - padding, yPos),
                strokeWidth = 1f
            )
        }

        val path = Path()
        val fillPath = Path()

        values.forEachIndexed { index, value ->
            val x = padding + index * stepX
            val y = zeroY - (value / scaledMax) * height

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, zeroY)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }

            drawCircle(
                color = lineColor,
                radius = dotRadius,
                center = Offset(x, y)
            )
        }

        fillPath.lineTo(padding + (values.size - 1) * stepX, zeroY)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(fillColor, fillColor.copy(alpha = 0f)),
                startY = zeroY - (maxValue / scaledMax) * height,
                endY = zeroY
            )
        )

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )
    }
}