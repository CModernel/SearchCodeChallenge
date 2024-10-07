package com.sonder.domain.usecases.request

import com.sonder.domain.mappers.RequestResponseMapper
import com.sonder.domain.models.SearchSectionResult
import com.sonder.domain.repositories.MockRequestsRepository
import com.sonder.domain.repositories.MockResponsesRepository
import com.sonder.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHorizontalDetailedSearchResultsUseCase @Inject constructor(
    private val mockRequestsRepository: MockRequestsRepository,
    private val mockResponsesRepository: MockResponsesRepository,
    private val requestResponseMapper: RequestResponseMapper,
) : FlowUseCase<Unit, SearchSectionResult>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun construct(params: Unit): Flow<SearchSectionResult> {
        return mockRequestsRepository.getHorizontaDetailedRequestParams()
            .flatMapLatest { request ->
                mockResponsesRepository.getHorizontalDetailedSectionResults().map { result ->
                    requestResponseMapper.map(request, result)
                }
            }
    }
}