package com.sonder.domain.usecases.search

import com.sonder.domain.models.SearchState
import com.sonder.domain.repositories.SearchStateRepository
import com.sonder.domain.usecases.base.NoResultUseCase
import javax.inject.Inject

class UpdateSearchStateUseCase @Inject constructor(
    private val searchStateRepository: SearchStateRepository,
) : NoResultUseCase<SearchState>() {

    override suspend fun construct(params: SearchState) {
        searchStateRepository.updateSearchState(params)
    }
}