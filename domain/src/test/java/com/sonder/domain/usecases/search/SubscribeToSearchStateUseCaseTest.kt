package com.sonder.domain.usecases.search

import com.sonder.domain.models.SearchState
import com.sonder.domain.repositories.SearchStateRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SubscribeToSearchStateUseCaseTest {
    private lateinit var useCase: SubscribeToSearchStateUseCase
    private val searchStateRepository: SearchStateRepository = mockk(relaxed = true)

    private fun setupTest(state: SearchState)  {
        coEvery { searchStateRepository.searchState } returns flowOf(state)
        useCase = SubscribeToSearchStateUseCase(searchStateRepository = searchStateRepository)
    }

    @Test
    fun `WHEN useCase is executed THEN it returns search state in idle`() = runTest {
        val searchState = SearchState.Idle

        setupTest(searchState)

        val expectedValue = useCase.execute(Unit)

        coVerify(exactly = 1) {
            searchStateRepository.searchState
        }

        assertEquals(searchState, expectedValue.first())
    }

    @Test
    fun `WHEN useCase is executed THEN it returns search state in searching`() = runTest {
        val searchState = SearchState.Searching
        setupTest(searchState)

        val expectedValue = useCase.execute(Unit)

        coVerify(exactly = 1) {
            searchStateRepository.searchState
        }

        assertEquals(searchState, expectedValue.first())
    }
}