package com.example.homework.presentaion.screens.graphicScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.homework.presentaion.component.GraphView

@Composable
fun GraphScreen() {
    var pointCount by remember { mutableStateOf("") }
    var values by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var showGraph by remember { mutableStateOf(false) }
    var parsedValues by remember { mutableStateOf<List<Float>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = pointCount,
            onValueChange = {
                pointCount = it
                showGraph = false
                error = ""
            },
            label = {
                Text(
                    text = stringResource(R.string.number_of_points)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = values,
            onValueChange = {
                values = it
                showGraph = false
                error = ""
            },
            label = {
                Text(
                    text = stringResource(R.string.values)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {
                try {
                    val count = pointCount.toInt()
                    if (count <= 0) {
                        error = "Кол-во точек должно быть положительным"
                        return@Button
                    }

                    val valueList = values.split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .map { it.toFloat() }

                    if (valueList.any { it < 0 }) {
                        error = "Значения должны быть положительными"
                        return@Button
                    }

                    if (valueList.size != count) {
                        error = "Кол-во точек и значений должно совпадать"
                        return@Button
                    }

                    parsedValues = valueList
                    showGraph = true
                    error = ""
                } catch (e: Exception) {
                    error = "Неправильный формат ввода"
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.draw_graph))
        }

        if (showGraph) {
            GraphView(
                values = parsedValues,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(top = 16.dp)
            )
        }

    }
}

