package com.sonder.domain.usecases.request

import app.cash.turbine.test
import com.sonder.domain.models.SearchRequestParams
import com.sonder.domain.repositories.MockRequestsRepository
import com.sonder.domain.repositories.MockResponsesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetErrorSearchResultsUseCaseTest {
    private val mockRequestsRepository: MockRequestsRepository = mockk(relaxed = true)
    private val mockResponsesRepository: MockResponsesRepository = mockk(relaxed = true)

    private lateinit var getErrorSearchResultsUseCase: GetErrorSearchResultsUseCase

    @Before
    fun setup() {
        getErrorSearchResultsUseCase = GetErrorSearchResultsUseCase(
            mockRequestsRepository,
            mockResponsesRepository
        )
    }

    @Test
    fun `WHEN usecase is executed THEN it fetches error request params and throws an error`() = runTest {
        val mockRequest = mockSearchRequestParams()
        val mockError = Exception("404")

        coEvery { mockRequestsRepository.getErrorRequestParams() } returns flowOf(mockRequest)
        coEvery { mockResponsesRepository.getErrorResult() } returns flowOf(mockError)

        getErrorSearchResultsUseCase.execute(Unit).test {
            val thrownError = awaitError()
            assertEquals(mockError.message, thrownError.message)
        }

        coVerify(exactly = 1) { mockRequestsRepository.getErrorRequestParams() }
        coVerify(exactly = 1) { mockResponsesRepository.getErrorResult() }
    }

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
}