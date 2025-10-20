package com.airtable.interview.airtableschedule.timeline.domain

import com.airtable.interview.airtableschedule.models.Event

/**
 * Contract for lane allocation in the domain layer.
 */
interface LaneAllocator {
    fun allocate(events: List<Event>): List<List<Event>>
}

/**
 * Greedy implementation that assigns events to the first available lane.
 * This consolidates the previous util.assignLanes logic into the domain.
 */
class GreedyLaneAllocator : LaneAllocator {
    override fun allocate(events: List<Event>): List<List<Event>> {
        val lanes = mutableListOf<MutableList<Event>>()

        // assume events may be unsorted — sort by start date for correctness
        events.sortedBy { it.startDate }
            .forEach { event ->
                val availableLane = lanes.find { lane ->
                    lane.last().endDate.time <= event.startDate.time
                }

                if (availableLane != null) {
                    availableLane.add(event)
                } else {
                    lanes.add(mutableListOf(event))
                }
            }

        return lanes
    }
}
