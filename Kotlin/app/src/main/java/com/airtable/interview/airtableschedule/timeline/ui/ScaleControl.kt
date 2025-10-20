package com.airtable.interview.airtableschedule.timeline.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScaleControl(
    dayWidth: Dp,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = onDecrease) {
            Text(text = "-")
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(text = "Scale: ${dayWidth.value.toInt()} dp")

        Spacer(modifier = Modifier.width(12.dp))

        Button(onClick = onIncrease) {
            Text(text = "+")
        }
    }
}
