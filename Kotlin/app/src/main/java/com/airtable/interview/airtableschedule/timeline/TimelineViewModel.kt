package com.airtable.interview.airtableschedule.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airtable.interview.airtableschedule.repositories.EventDataRepository
import com.airtable.interview.airtableschedule.repositories.EventDataRepositoryImpl
import com.airtable.interview.airtableschedule.timeline.domain.GreedyLaneAllocator
import com.airtable.interview.airtableschedule.timeline.domain.LaneAllocator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel responsible for managing the state of the timeline screen.
 * Now accepts a LaneAllocator to compute lanes (dependency injection / SOLID).
 */
class TimelineViewModel(
    private val laneAllocator: LaneAllocator = GreedyLaneAllocator()
) : ViewModel() {
    private val eventDataRepository: EventDataRepository = EventDataRepositoryImpl()

    val uiState: StateFlow<TimelineUiState> = eventDataRepository
        .getTimelineItems()
        .map { events ->
            val lanes = laneAllocator.allocate(events)
            TimelineUiState(events = events, lanes = lanes)
        }
        .stateIn(
            viewModelScope,
            initialValue = TimelineUiState(),
            started = SharingStarted.WhileSubscribed()
        )
}
