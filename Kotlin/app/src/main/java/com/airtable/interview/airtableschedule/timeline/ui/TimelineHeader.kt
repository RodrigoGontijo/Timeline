package com.airtable.interview.airtableschedule.timeline.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimelineHeader(
    minStartMillis: Long,
    totalDays: Long,
    dayWidth: Dp,
    scrollState: androidx.compose.foundation.ScrollState
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    // adapt header height when dayWidth is small so text can wrap/become readable
    val headerHeight = when {
        dayWidth < 50.dp -> 64.dp
        dayWidth < 80.dp -> 48.dp
        else -> 32.dp
    }
    Row(
        modifier = Modifier
            .height(headerHeight)
            .horizontalScroll(scrollState)
    ) {
        for (i in 0 until totalDays) {
            val dayMillis = minStartMillis + i * 24L * 60L * 60L * 1000L
            Box(modifier = Modifier.width(dayWidth), contentAlignment = Alignment.Center) {
                Text(
                    text = sdf.format(Date(dayMillis)),
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
