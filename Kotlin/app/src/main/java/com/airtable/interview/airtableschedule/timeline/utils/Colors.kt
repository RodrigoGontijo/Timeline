package com.airtable.interview.airtableschedule.timeline.utils

import androidx.compose.ui.graphics.Color

object Colors {
    private val palette = listOf(
        Color(0xFF2196F3), // blue
        Color(0xFF4CAF50), // green
        Color(0xFFFF9800), // orange
        Color(0xFFE91E63), // pink
        Color(0xFF9C27B0), // purple
        Color(0xFF607D8B)  // blue grey
    )

    fun colorForLane(index: Int): Color = palette[index % palette.size]
}

