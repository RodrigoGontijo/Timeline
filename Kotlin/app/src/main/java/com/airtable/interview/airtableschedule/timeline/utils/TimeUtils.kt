package com.airtable.interview.airtableschedule.timeline.utils

object TimeUtils {
    fun daysBetweenMillis(startMillis: Long, endMillis: Long): Long {
        val diff = endMillis - startMillis
        val dayMs = 24L * 60L * 60L * 1000L
        return diff / dayMs
    }
}

