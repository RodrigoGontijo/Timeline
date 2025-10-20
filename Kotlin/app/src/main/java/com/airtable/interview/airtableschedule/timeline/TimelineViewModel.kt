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
 * TimelineViewModel: receives a LaneAllocator by DI to compute lanes (SOLID).
 */
class TimelineViewModel(
    //replace with DI framework in future
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
