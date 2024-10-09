package com.sonder.domain.usecases.request

import app.cash.turbine.test
import com.sonder.domain.mappers.RequestResponseMapper
import com.sonder.domain.models.SearchItem
import com.sonder.domain.models.SearchRequestParams
import com.sonder.domain.models.SearchSectionResult
import com.sonder.domain.repositories.MockRequestsRepository
import com.sonder.domain.repositories.MockResponsesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetVerticalCompactSearchResultsUseCaseTest {
    private val mockRequestsRepository: MockRequestsRepository = mockk(relaxed = true)
    private val mockResponsesRepository: MockResponsesRepository = mockk(relaxed = true)
    private val requestResponseMapper: RequestResponseMapper = mockk(relaxed = true)

    private lateinit var getVerticalCompactSearchResultsUseCase: GetVerticalCompactSearchResultsUseCase

    @Before
    fun setup() {
        getVerticalCompactSearchResultsUseCase = GetVerticalCompactSearchResultsUseCase(
            mockRequestsRepository,
            mockResponsesRepository,
            requestResponseMapper
        )
    }

    @Test
    fun `WHEN usecase is executed THEN it fetches vertical compact search params and emits mapped result`() = runTest {
        val mockRequest = mockSearchRequestParams()
        val mockResult = mockSearchResult()
        val expectedMappedResult = SearchSectionResult("MockTitle", "MockDescription", emptyList())

        // Mocking the expected behavior of the repositories and the mapper
        coEvery { mockRequestsRepository.getVerticalCompactRequestParams() } returns flowOf(mockRequest)
        coEvery { mockResponsesRepository.getVerticalCompactSearchResults() } returns flowOf(mockResult)
        every { requestResponseMapper.map(mockRequest, mockResult) } returns expectedMappedResult

        // Execute the use case and test the emissions
        getVerticalCompactSearchResultsUseCase.execute(Unit).test {
            assertEquals(expectedMappedResult, awaitItem())
            awaitComplete()
        }

        // Verify that each dependency is called exactly once
        coVerify(exactly = 1) { mockRequestsRepository.getVerticalCompactRequestParams() }
        coVerify(exactly = 1) { mockResponsesRepository.getVerticalCompactSearchResults() }
        verify(exactly = 1) { requestResponseMapper.map(mockRequest, mockResult) }
    }

    // Mocking the request parameters for testing purposes
    fun mockSearchRequestParams(
        sectionTitle: String = "",
        query: String = "",
        size: Int = 0,
        contentTypes: List<String> = emptyList()
    ) = SearchRequestParams(
        sectionTitle = sectionTitle,
        query = query,
        size = size,
        contentTypes = contentTypes
    )

    // Mocking the search result for testing purposes
    fun mockSearchResult(
        sectionTitle: String = "",
        sectionDescription: String? = "",
        items: List<SearchItem> = emptyList(),
    ) = SearchSectionResult(
        sectionTitle = sectionTitle,
        sectionDescription = sectionDescription,
        items = items,
    )
}