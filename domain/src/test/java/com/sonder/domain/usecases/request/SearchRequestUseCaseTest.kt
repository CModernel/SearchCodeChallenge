package com.sonder.domain.usecases.request

import app.cash.turbine.test
import com.sonder.domain.models.SearchItemRequest
import com.sonder.domain.models.SearchItemViewType
import com.sonder.domain.models.SearchSectionResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SearchRequestUseCaseTest {
    private val getHorizontalCompactSearchResultsUseCase : GetHorizontalCompactSearchResultsUseCase = mockk(relaxed = true)
    private val getVerticalCompactSearchResultsUseCase : GetVerticalCompactSearchResultsUseCase = mockk(relaxed = true)
    private val getHorizontalDetailedSearchResultsUseCase : GetHorizontalDetailedSearchResultsUseCase = mockk(relaxed = true)
    private val getVerticalDetailedSearchResultsUseCase : GetVerticalDetailedSearchResultsUseCase = mockk(relaxed = true)
    private val getErrorSearchResultsUseCase : GetErrorSearchResultsUseCase = mockk(relaxed = true)

    private lateinit var searchRequestUseCase: SearchRequestUseCase

    @Before
    fun setup() {
        searchRequestUseCase = SearchRequestUseCase(
            getHorizontalCompactSearchResultsUseCase,
            getVerticalCompactSearchResultsUseCase,
            getHorizontalDetailedSearchResultsUseCase,
            getVerticalDetailedSearchResultsUseCase,
            getErrorSearchResultsUseCase
        )
    }

    @Test
    fun `WHEM query params is 'mock' THEN emit SearchItemRequest for all sections`() = runTest {
        val mockHorizontalCompact = SearchSectionResult("HorizontalCompact Title", "HorizontalCompact Description", emptyList())
        val mockVerticalCompact = SearchSectionResult("VerticalCompact Title", "VerticalCompact Description", emptyList())
        val mockHorizontalDetailed = SearchSectionResult("HorizontalDetailed Title", "HorizontalDetailed Description", emptyList())
        val mockVerticalDetailed = SearchSectionResult("VerticalDetailed Title", "VerticalDetailed Description", emptyList())
        coEvery { getHorizontalCompactSearchResultsUseCase.execute(Unit) }  returns (flowOf(mockHorizontalCompact))
        coEvery { getVerticalCompactSearchResultsUseCase.execute(Unit) }  returns (flowOf(mockVerticalCompact))
        coEvery { getHorizontalDetailedSearchResultsUseCase.execute(Unit) }  returns (flowOf(mockHorizontalDetailed))
        coEvery { getVerticalDetailedSearchResultsUseCase.execute(Unit) }  returns (flowOf(mockVerticalDetailed))


        searchRequestUseCase.execute("mock").test {
            assertEquals(
                SearchItemRequest(searchType = SearchItemViewType.HORIZONTAL_COMPACT, sections = mockHorizontalCompact),
                awaitItem()
            )
            coVerify(exactly = 1) {
                getHorizontalCompactSearchResultsUseCase.execute(Unit)
            }
            coVerify(exactly = 0){
                getVerticalCompactSearchResultsUseCase.execute(Unit)
                getHorizontalDetailedSearchResultsUseCase.execute(Unit)
                getVerticalDetailedSearchResultsUseCase.execute(Unit)
            }
            assertEquals(
                SearchItemRequest(searchType = SearchItemViewType.VERTICAL_COMPACT, sections = mockVerticalCompact),
                awaitItem()
            )
            coVerify(exactly = 1) {
                getVerticalCompactSearchResultsUseCase.execute(Unit)
            }
            coVerify(exactly = 0){
                getHorizontalDetailedSearchResultsUseCase.execute(Unit)
                getVerticalDetailedSearchResultsUseCase.execute(Unit)
            }
            assertEquals(
                SearchItemRequest(searchType = SearchItemViewType.HORIZONTAL_DETAILED, sections = mockHorizontalDetailed),
                awaitItem()
            )
            coVerify(exactly = 1) {
                getHorizontalDetailedSearchResultsUseCase.execute(Unit)
            }
            coVerify(exactly = 0){
                getVerticalDetailedSearchResultsUseCase.execute(Unit)
            }
            assertEquals(
                SearchItemRequest(searchType = SearchItemViewType.VERTICAL_DETAILED, sections = mockVerticalDetailed),
                awaitItem()
            )
            coVerify(exactly = 1) {
                getVerticalDetailedSearchResultsUseCase.execute(Unit)
            }
            awaitComplete()
        }
    }

    @Test
    fun `WHEN query params is 'error' THEN error exception is thrown`() = runTest {
        val error = Exception("404")
        coEvery { getErrorSearchResultsUseCase.execute(Unit) } throws error

        try {
            searchRequestUseCase.execute("error").test {
                awaitError()
            }
            coVerify(exactly = 1) {
                getErrorSearchResultsUseCase.execute(Unit)
            }
            coVerify(exactly = 0){
                getHorizontalCompactSearchResultsUseCase.execute(Unit)
                getVerticalCompactSearchResultsUseCase.execute(Unit)
                getHorizontalDetailedSearchResultsUseCase.execute(Unit)
                getVerticalDetailedSearchResultsUseCase.execute(Unit)
            }
        } catch (e: Exception) {
            assertEquals(error.message, e.message)
        }
    }

    @Test
    fun `WHEN query params is empty THEN it emits SearchItemRequest with null sections`() = runTest {
        searchRequestUseCase.execute("").test {
            assertEquals(
                SearchItemRequest(sections = null),
                awaitItem()
            )
            coVerify(exactly = 0){
                getErrorSearchResultsUseCase.execute(Unit)
                getHorizontalCompactSearchResultsUseCase.execute(Unit)
                getVerticalCompactSearchResultsUseCase.execute(Unit)
                getHorizontalDetailedSearchResultsUseCase.execute(Unit)
                getVerticalDetailedSearchResultsUseCase.execute(Unit)
            }
            awaitComplete()
        }
    }
}