package com.airtable.interview.airtableschedule.timeline.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.timeline.utils.TimeUtils
import kotlin.math.max

/**
 * LaneRow: renders a single lane (row) of events. Uses a shared ScrollState
 * so header and lanes scroll together. Each lane has a fixed height so multiple
 * lanes stack correctly in a Column instead of one taking full available height.
 */
@Composable
fun LaneRow(
    lane: List<Event>,
    laneIndex: Int,
    minStartMillis: Long,
    dayWidth: Dp,
    totalWidth: Dp,
    scrollState: ScrollState,
    eventChip: @Composable (Event, Long, Dp, Int, Modifier) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .horizontalScroll(scrollState)
            .width(totalWidth)
    ) {
        var cursor = minStartMillis
        lane.forEach { ev ->
            val gapDays = TimeUtils.daysBetweenMillis(cursor, ev.startDate.time)
            if (gapDays > 0) {
                Spacer(modifier = Modifier.width(dayWidth * gapDays.toFloat()))
            }

            val durationDays = max(1L, TimeUtils.daysBetweenMillis(ev.startDate.time, ev.endDate.time))

            // delegate rendering to provided eventChip composable
            eventChip(ev, durationDays, dayWidth, laneIndex, Modifier.width(dayWidth * durationDays.toFloat()))

            cursor = ev.endDate.time
        }
    }
}
