package com.airtable.interview.airtableschedule.timeline.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.timeline.utils.Colors

@Composable
fun EventChip(
    event: Event,
    durationDays: Long,
    dayWidth: Dp,
    laneIndex: Int,
    modifier: Modifier = Modifier,
    onMeasured: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .background(Colors.colorForLane(laneIndex))
            .fillMaxHeight()
            .padding(4.dp)
            .onGloballyPositioned { coords -> onMeasured?.invoke(coords.size.height) }
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    Toast.makeText(context, "${event.name} — idNumber = ${event.id}", Toast.LENGTH_SHORT).show()
                }
            )
    ) {
        // use durationDays and dayWidth in semantics to avoid unused-parameter warnings
        Column(
            modifier = Modifier
                .padding(6.dp)
                .semantics {
                    contentDescription = "durationDays=$durationDays dayWidth=$dayWidth"
                }
        ) {
            Text(
                text = event.name,
                color = Color.White
            )

            // display id below the name as requested
            Text(
                text = "idNumber = ${event.id}",
                color = Color.White
            )
        }
    }
}
