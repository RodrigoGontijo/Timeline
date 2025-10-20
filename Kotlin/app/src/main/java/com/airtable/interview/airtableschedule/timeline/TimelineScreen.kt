package com.airtable.interview.airtableschedule.timeline

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.timeline.ui.EventChip
import com.airtable.interview.airtableschedule.timeline.ui.TimelineHeader
import com.airtable.interview.airtableschedule.timeline.ui.LaneRow
import com.airtable.interview.airtableschedule.timeline.ui.ScaleControl
import com.airtable.interview.airtableschedule.timeline.utils.TimeUtils.daysBetweenMillis
import kotlin.math.max

/**
 * Minimal timeline screen: implementa apenas o necessário do README.
 * - mostra eventos em pistas (lanes) compactas
 * - largura proporcional à duração (em dias)
 * - comportamento read-only
 */
@Composable
fun TimelineScreen(viewModel: TimelineViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events = uiState.events
    val lanes = uiState.lanes

    TimelineSimpleView(events, lanes)
}

@Composable
private fun TimelineSimpleView(events: List<Event>, lanes: List<List<Event>>) {
    if (events.isEmpty()) {
        Text(text = "Nenhum evento")
        return
    }

    // calcular bounds e duração total em dias
    val minStart = events.minOf { it.startDate.time }
    val maxEnd = events.maxOf { it.endDate.time }
    val totalDays = max(1L, daysBetweenMillis(minStart, maxEnd))

    // width per day (zoomable) and scroll state
    val defaultDayWidth = 80.dp
    val minDayWidth = 40.dp
    val maxDayWidth = 200.dp

    val dayWidthState = remember { mutableStateOf(defaultDayWidth) }
    val dayWidth = dayWidthState.value

    val totalWidth = dayWidth * totalDays.toFloat()
    val scrollState = rememberScrollState()

    // header de datas sincronizado com o mesmo scrollState
    // track max measured EventChip height (in px) and convert to Dp
    val maxChipHeightPx = remember { mutableStateOf(0) }
    val density = LocalDensity.current

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {

        TimelineHeader(minStartMillis = minStart, totalDays = totalDays, dayWidth = dayWidth, scrollState = scrollState)

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        // compute laneHeight from measured chips (fallback to 56.dp until measured)
        val laneHeightDp: Dp = if (maxChipHeightPx.value == 0) 56.dp else with(density) { maxChipHeightPx.value.toDp() }

        // lanes are provided by ViewModel via LaneAllocator
        lanes.forEachIndexed { laneIndex, lane ->
            LaneRow(
                lane = lane,
                laneIndex = laneIndex,
                minStartMillis = minStart,
                dayWidth = dayWidth,
                totalWidth = totalWidth,
                scrollState = scrollState,
                eventChip = { ev, durationDays, dWidth, idx, mod ->
                    EventChip(
                        event = ev,
                        durationDays = durationDays,
                        dayWidth = dWidth,
                        laneIndex = idx,
                        modifier = mod,
                        onMeasured = { h -> if (h > maxChipHeightPx.value) maxChipHeightPx.value = h }
                    )
                }
            )
            HorizontalDivider()
        }

        // scale control below lanes
        ScaleControl(
            dayWidth = dayWidth,
            onIncrease = {
                val new = dayWidth.value + 10f
                val clamped = new.coerceAtMost(maxDayWidth.value).coerceAtLeast(minDayWidth.value)
                dayWidthState.value = clamped.dp
            },
            onDecrease = {
                val new = dayWidth.value - 10f
                val clamped = new.coerceAtMost(maxDayWidth.value).coerceAtLeast(minDayWidth.value)
                dayWidthState.value = clamped.dp
            }
        )
    }
}
