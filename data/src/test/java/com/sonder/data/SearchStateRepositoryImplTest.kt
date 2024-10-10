package com.sonder.data

import app.cash.turbine.test
import com.sonder.domain.models.SearchState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchStateRepositoryImplTest {
    private lateinit var searchStateRepository: SearchStateRepositoryImpl

    @Before
    fun setup() {
        searchStateRepository = SearchStateRepositoryImpl()
    }

    @Test
    fun `WHEN repository is initialized THEN initial state is Idle`() = runTest {
        val initialState = searchStateRepository.searchState.first()

        assertEquals(SearchState.Idle, initialState)
    }

    @Test
    fun `WHEN updateSearchState is called THEN searchState is updated`() = runTest {
        val newState = SearchState.Searching

        searchStateRepository.updateSearchState(newState)

        searchStateRepository.searchState.test {
            assertEquals(newState, awaitItem())
        }
    }

    @Test
    fun `WHEN searchState goes to idle and back to searching THEN searchState emits correct sequence`() = runTest {
        val statesToEmit = listOf(
            SearchState.Searching,
            SearchState.Idle,
            SearchState.Searching,
        )

        searchStateRepository.searchState.test {
            assertEquals(SearchState.Idle, awaitItem()) // Assert the initial state first

            statesToEmit.forEach { state ->
                searchStateRepository.updateSearchState(state)
                assertEquals(state, awaitItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN searchState goes to searching and back to idle THEN searchState emits correct sequence`() = runTest {
        val statesToEmit = listOf(
            SearchState.Searching,
            SearchState.Idle,
        )

        searchStateRepository.searchState.test {
            assertEquals(SearchState.Idle, awaitItem()) // Assert the initial state

            statesToEmit.forEach { state ->
                searchStateRepository.updateSearchState(state)
                assertEquals(state, awaitItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}