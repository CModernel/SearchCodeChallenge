package com.sonder.codechallenge.ui

import app.cash.turbine.test
import com.sonder.codechallenge.ui.models.SearchActivityStates
import com.sonder.domain.models.SearchState
import com.sonder.domain.usecases.search.ClearSearchResultsUseCase
import com.sonder.domain.usecases.search.SubscribeToSearchStateUseCase
import com.sonder.domain.usecases.search.UpdateSearchQueryUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityViewModelTest {
    private val updateSearchQueryUseCase: UpdateSearchQueryUseCase = mockk(relaxed = true)
    private val clearSearchResultsUseCase: ClearSearchResultsUseCase = mockk(relaxed = true)
    private val subscribeToSearchStateUseCase: SubscribeToSearchStateUseCase = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = MainActivityViewModel(
            updateSearchQueryUseCase,
            clearSearchResultsUseCase,
            subscribeToSearchStateUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN updateSearchQuery is called THEN state transitions to Searching`() = runTest {
        val query = "test query"

        coJustRun { updateSearchQueryUseCase.execute(query) }

        viewModel.state.test {
            viewModel.updateSearchQuery(query)

            assertEquals(SearchActivityStates.Started, awaitItem())
            assertEquals(SearchActivityStates.Searching(query), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN clearSearchQuery is called THEN state transitions to Started`() = runTest {
        coJustRun { clearSearchResultsUseCase.execute(Unit) }

        viewModel.state.test {
            viewModel.clearSearchQuery()

            assertEquals(SearchActivityStates.Started, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN search state is set to Idle after searching THEN state transitions to Results`() = runTest(testDispatcher) {
        val searchQuery = "mock"

        coEvery { subscribeToSearchStateUseCase.execute(Unit) } returns flow {
            emit(SearchState.Searching)
            delay(2000)
            emit(SearchState.Idle)
        }

        val viewModel = MainActivityViewModel(
            updateSearchQueryUseCase,
            clearSearchResultsUseCase,
            subscribeToSearchStateUseCase
        )

        viewModel.updateSearchQuery(searchQuery)

        viewModel.state.test {
            assertEquals(SearchActivityStates.Started, awaitItem())

            advanceUntilIdle()
            assertEquals(SearchActivityStates.Searching(searchQuery), awaitItem())

            advanceUntilIdle()
            assertEquals(SearchActivityStates.Results(searchQuery), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}