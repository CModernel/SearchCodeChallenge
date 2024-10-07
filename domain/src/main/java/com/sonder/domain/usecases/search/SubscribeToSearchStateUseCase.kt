package com.sonder.domain.usecases.search

import com.sonder.domain.models.SearchState
import com.sonder.domain.repositories.SearchRepository
import com.sonder.domain.repositories.SearchResultRepository
import com.sonder.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeToSearchStateUseCase @Inject constructor(
    private val searchResultRepository: SearchResultRepository,
) : FlowUseCase<Unit, SearchState>() {

    override fun construct(params: Unit): Flow<SearchState> {
        return searchResultRepository.searchState
    }
}