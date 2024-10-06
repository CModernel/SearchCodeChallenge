package com.sonder.domain.usecases.search

import com.sonder.domain.repositories.SearchRepository
import com.sonder.domain.usecases.base.NoResultUseCase
import javax.inject.Inject

class UpdateSearchQueryUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : NoResultUseCase<String>() {

    override suspend fun construct(params: String) {
        searchRepository.updateSearchQuery(params)
    }
}