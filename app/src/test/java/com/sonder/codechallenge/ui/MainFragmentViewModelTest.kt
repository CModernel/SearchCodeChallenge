package com.sonder.codechallenge.ui

import app.cash.turbine.test
import com.sonder.codechallenge.ui.models.SearchFragmentStates
import com.sonder.domain.models.SearchItem
import com.sonder.domain.models.SearchItemRequest
import com.sonder.domain.models.SearchItemViewType
import com.sonder.domain.models.SearchSectionResult
import com.sonder.domain.models.SearchState
import com.sonder.domain.repositories.SearchRepository
import com.sonder.domain.usecases.request.SearchRequestUseCase
import com.sonder.domain.usecases.search.SubscribeToSearchQueryUseCase
import com.sonder.domain.usecases.search.UpdateSearchStateUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainFragmentViewModelTest {
    private lateinit var viewModel: MainFragmentViewModel

    private val searchRequestUseCase: SearchRequestUseCase = mockk(relaxed = true)
    private val searchRepository: SearchRepository = mockk(relaxed = true)
    private lateinit var subscribeToSearchQueryUseCase: SubscribeToSearchQueryUseCase
    private val updateSearchStateUseCase: UpdateSearchStateUseCase = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        subscribeToSearchQueryUseCase = SubscribeToSearchQueryUseCase(searchRepository)

        viewModel = MainFragmentViewModel(
            searchRequestUseCase,
            subscribeToSearchQueryUseCase,
            updateSearchStateUseCase
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN query is provided THEN state transitions from Started to ResultsLoaded`() = runTest(UnconfinedTestDispatcher()) {
        val searchQuery = "mock"

        val mockSearchRequests = listOf(
            createMockSearchRequest(SearchItemViewType.HORIZONTAL_COMPACT),
            createMockSearchRequest(SearchItemViewType.VERTICAL_COMPACT),
            createMockSearchRequest(SearchItemViewType.HORIZONTAL_DETAILED),
            createMockSearchRequest(SearchItemViewType.VERTICAL_DETAILED)
        )

        coEvery { subscribeToSearchQueryUseCase.execute(Unit) } returns flow {
            emit(searchQuery)
        }

        coEvery { searchRequestUseCase.execute(searchQuery) } returns flow {
            mockSearchRequests.forEach { emit(it) }
        }

        coEvery { searchRepository.searchQuery } returns flow { emit(searchQuery) }
        coJustRun { searchRepository.updateSearchQuery(searchQuery) }
        coJustRun { updateSearchStateUseCase.execute(any()) }

        viewModel.state.test {
            assertEquals(SearchFragmentStates.Started, awaitItem())

            advanceUntilIdle()

            val expectedResults = mutableListOf<SearchItemRequest>()
            mockSearchRequests.forEach { mockRequest ->
                expectedResults.add(mockRequest)
                assertEquals(SearchFragmentStates.ResultsLoaded(expectedResults), awaitItem())
            }

            coVerify { updateSearchStateUseCase.execute(SearchState.Searching) }
            coVerify { updateSearchStateUseCase.execute(SearchState.Idle) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN search request fails THEN state transitions to Error`() = runTest(UnconfinedTestDispatcher()) {
        val searchQuery = "error"
        val errorMessage = "404"

        coEvery { subscribeToSearchQueryUseCase.execute(Unit) } returns flow { emit(searchQuery) }
        coEvery { searchRequestUseCase.execute(searchQuery) } returns flow { throw Exception(errorMessage) }
        coEvery { searchRepository.searchQuery } returns flow { emit(searchQuery) }
        coJustRun { searchRepository.updateSearchQuery(searchQuery) }
        coJustRun { updateSearchStateUseCase.execute(any()) }

        viewModel.state.test {
            assertEquals(SearchFragmentStates.Started, awaitItem())
            advanceUntilIdle()
            searchRequestUseCase.execute(searchQuery)
            advanceTimeBy(2000)
            assertEquals(SearchFragmentStates.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN search request returns no results THEN state transitions to NoResults`() = runTest(UnconfinedTestDispatcher()) {
        val searchQuery = "noResults"
        val mockSearchRequest = SearchItemRequest(
            searchType = SearchItemViewType.VERTICAL_COMPACT,
            sections = null,
        )

        coEvery { subscribeToSearchQueryUseCase.execute(Unit) } returns flow { emit(searchQuery) }
        coEvery { searchRequestUseCase.execute(searchQuery) } returns flow { emit(mockSearchRequest) }
        coEvery { searchRepository.searchQuery } returns flow { emit(searchQuery) }
        coJustRun { searchRepository.updateSearchQuery(searchQuery) }
        coJustRun { updateSearchStateUseCase.execute(any()) }

        viewModel.state.test {
            assertEquals(SearchFragmentStates.Started, awaitItem())
            advanceUntilIdle()
            searchRequestUseCase.execute(searchQuery)
            advanceTimeBy(2000)
            assertEquals(SearchFragmentStates.NoResults, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    fun createMockSearchRequest(
        searchType: SearchItemViewType,
        sectionTitle: String = "mockTitle",
        sectionDescription: String = "mockDescription",
        items: List<SearchItem> = listOf(SearchItem())
    ): SearchItemRequest {
        return SearchItemRequest(
            searchType = searchType,
            sections = SearchSectionResult(
                sectionTitle = sectionTitle,
                sectionDescription = sectionDescription,
                items = items
            )
        )
    }
}