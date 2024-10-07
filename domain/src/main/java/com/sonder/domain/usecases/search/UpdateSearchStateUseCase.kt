package com.sonder.domain.usecases.search

import com.sonder.domain.models.SearchState
import com.sonder.domain.repositories.SearchRepository
import com.sonder.domain.repositories.SearchResultRepository
import com.sonder.domain.usecases.base.NoResultUseCase
import javax.inject.Inject

class UpdateSearchStateUseCase @Inject constructor(
    private val searchResultRepository: SearchResultRepository,
) : NoResultUseCase<SearchState>() {

    override suspend fun construct(params: SearchState) {
        searchResultRepository.updateSearchState(params)
    }
}