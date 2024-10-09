package com.sonder.domain.usecases.request

import com.sonder.domain.mappers.RequestResponseMapper
import com.sonder.domain.models.SearchItemRequest
import com.sonder.domain.models.SearchItemViewType
import com.sonder.domain.models.SearchSectionResult
import com.sonder.domain.repositories.MockRequestsRepository
import com.sonder.domain.repositories.MockResponsesRepository
import com.sonder.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRequestUseCase @Inject constructor(
    private val getHorizontalCompactSearchResultsUseCase: GetHorizontalCompactSearchResultsUseCase,
    private val getVerticalCompactSearchResultsUseCase: GetVerticalCompactSearchResultsUseCase,
    private val getHorizontalDetailedSearchResultsUseCase: GetHorizontalDetailedSearchResultsUseCase,
    private val getVerticalDetailedSearchResultsUseCase: GetVerticalDetailedSearchResultsUseCase,
    private val getErrorSearchResultsUseCase: GetErrorSearchResultsUseCase
) : FlowUseCase<String, SearchItemRequest>() {
    override fun construct(params: String): Flow<SearchItemRequest> = flow {
        when (params) {
            "mock" -> {
                val viewTypes = listOf(
                    SearchItemViewType.HORIZONTAL_COMPACT,
                    SearchItemViewType.VERTICAL_COMPACT,
                    SearchItemViewType.HORIZONTAL_DETAILED,
                    SearchItemViewType.VERTICAL_DETAILED
                )

                for (viewType in viewTypes) {
                    fetchSection(viewType).collect { section ->
                        emit(SearchItemRequest(searchType = viewType, sections = section))
                        // Simulating a delay
                        delay(100)
                    }
                }
            }
            "error" -> {
                getErrorSearchResultsUseCase.execute(Unit)
                    .catch { throw it }.firstOrNull()
            }
            else -> {
                emit(SearchItemRequest(sections = null))
            }
        }
    }

    private fun fetchSection(viewType: SearchItemViewType): Flow<SearchSectionResult> {
        return when (viewType) {
            SearchItemViewType.HORIZONTAL_COMPACT -> getHorizontalCompactSearchResultsUseCase.execute(Unit)
            SearchItemViewType.VERTICAL_COMPACT -> getVerticalCompactSearchResultsUseCase.execute(Unit)
            SearchItemViewType.HORIZONTAL_DETAILED -> getHorizontalDetailedSearchResultsUseCase.execute(Unit)
            SearchItemViewType.VERTICAL_DETAILED -> getVerticalDetailedSearchResultsUseCase.execute(Unit)
        }
    }
}