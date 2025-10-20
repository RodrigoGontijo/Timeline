package com.airtable.interview.airtableschedule.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.timeline.domain.GreedyLaneAllocator
import com.airtable.interview.airtableschedule.timeline.domain.LaneAllocator
import kotlin.math.max

/**
 * Minimal timeline screen: implementa apenas o necessário do README.
 * - mostra eventos em pistas (lanes) compactas
 * - largura proporcional à duração (em dias)
 * - comportamento read-only
 */
@Composable
fun TimelineScreen(viewModel: TimelineViewModel = viewModel(), allocator: LaneAllocator = GreedyLaneAllocator()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events = uiState.events

    TimelineSimpleView(events, allocator)
}

@Composable
private fun TimelineSimpleView(events: List<Event>, allocator: LaneAllocator) {
    if (events.isEmpty()) {
        Text(text = "Nenhum evento")
        return
    }

    // calcular bounds e duração total em dias
    val minStart = events.minOf { it.startDate.time }
    val maxEnd = events.maxOf { it.endDate.time }
    val totalDays = max(1L, daysBetweenMillis(minStart, maxEnd))

    // width per day and scroll state
    val dayWidth: Dp = 80.dp
    val totalWidth = dayWidth * totalDays.toFloat()
    val scrollState = rememberScrollState()

    val sorted = events.sortedBy { it.startDate }
    val lanes = allocator.allocate(sorted)

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        lanes.forEachIndexed { laneIndex, lane ->
            // each lane scrolls horizontally to reveal full timeline
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .horizontalScroll(scrollState)
                    .width(totalWidth),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                var cursor = minStart
                lane.forEach { ev ->
                    val gapDays = daysBetweenMillis(cursor, ev.startDate.time)
                    if (gapDays > 0) {
                        Spacer(modifier = Modifier.width(dayWidth * gapDays.toFloat()))
                    }

                    val durationDays = max(1L, daysBetweenMillis(ev.startDate.time, ev.endDate.time))

                    Box(modifier = Modifier
                        .width(dayWidth * durationDays.toFloat())
                        .fillMaxHeight()
                        .padding(4.dp)
                        .background(colorForLane(laneIndex))
                    ) {
                        Text(text = ev.name, modifier = Modifier.padding(6.dp), color = Color.White)
                    }

                    cursor = ev.endDate.time
                }
            }
            HorizontalDivider()
        }
    }
}

// Helpers locais para simplicidade — implementações pequenas e testáveis

private fun daysBetweenMillis(startMillis: Long, endMillis: Long): Long {
    val diff = endMillis - startMillis
    val dayMs = 24L * 60L * 60L * 1000L
    return diff / dayMs
}

private fun colorForLane(index: Int): Color {
    val palette = listOf(
        Color(0xFF2196F3), // blue
        Color(0xFF4CAF50), // green
        Color(0xFFFF9800), // orange
        Color(0xFFE91E63), // pink
        Color(0xFF9C27B0), // purple
        Color(0xFF607D8B)  // blue grey
    )
    return palette[index % palette.size]
}
