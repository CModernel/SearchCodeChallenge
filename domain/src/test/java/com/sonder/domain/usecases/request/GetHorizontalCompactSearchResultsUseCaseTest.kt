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

class GetHorizontalCompactSearchResultsUseCaseTest {
    private val mockRequestsRepository: MockRequestsRepository = mockk(relaxed = true)
    private val mockResponsesRepository: MockResponsesRepository = mockk(relaxed = true)
    private val requestResponseMapper: RequestResponseMapper = mockk(relaxed = true)

    private lateinit var getHorizontalCompactSearchResultsUseCase: GetHorizontalCompactSearchResultsUseCase

    @Before
    fun setup() {
        getHorizontalCompactSearchResultsUseCase = GetHorizontalCompactSearchResultsUseCase(
            mockRequestsRepository,
            mockResponsesRepository,
            requestResponseMapper
        )
    }

    @Test
    fun `WHEN usecase is executed THEN it fetches horizontal compact search params and emits mapped result`() = runTest {
        val mockRequest = mockSearchRequestParams()
        val mockResult = mockSearchResult()
        val expectedMappedResult = SearchSectionResult("MockTitle", "MockDescription", emptyList())

        coEvery { mockRequestsRepository.getHorizontalCompactRequestParams() } returns flowOf(mockRequest)
        coEvery { mockResponsesRepository.getHorizontalCompactSearchResults() } returns flowOf(mockResult)
        every { requestResponseMapper.map(mockRequest, mockResult) } returns expectedMappedResult

        getHorizontalCompactSearchResultsUseCase.execute(Unit).test {
            assertEquals(expectedMappedResult, awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { mockRequestsRepository.getHorizontalCompactRequestParams() }
        coVerify(exactly = 1) { mockResponsesRepository.getHorizontalCompactSearchResults() }
        verify(exactly = 1) { requestResponseMapper.map(mockRequest, mockResult) }
    }

    fun mockSearchRequestParams(
        sectionTitle: String = "",
        query: String = "",
        size: Int = 0,
        contentTypes: List<String> = emptyList()
    )=
        SearchRequestParams(
            sectionTitle = sectionTitle,
            query = query,
            size = size,
            contentTypes = contentTypes
        )

    fun mockSearchResult(
       sectionTitle: String = "",
       sectionDescription: String? = "",
       items: List<SearchItem> = emptyList(),
    )=
      SearchSectionResult(
          sectionTitle = sectionTitle,
          sectionDescription = sectionDescription,
          items = items,
      )
}