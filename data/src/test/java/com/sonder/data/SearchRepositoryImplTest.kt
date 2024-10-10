package com.sonder.data

import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {
    private lateinit var searchRepository: SearchRepositoryImpl

    @Before
    fun setup() {
        searchRepository = SearchRepositoryImpl()
    }

    @Test
    fun `WHEN repository is initialized THEN initial query is empty`() = runTest {
        val initialQuery = searchRepository.searchQuery.first()

        assertEquals("", initialQuery)
    }

    @Test
    fun `WHEN updateSearchQuery is called with newQuery THEN searchQuery is updated`() = runTest {
        val newQuery = "mock"

        searchRepository.updateSearchQuery(newQuery)

        searchRepository.searchQuery.test {
            assertEquals(newQuery, awaitItem())
        }
    }

    @Test
    fun `WHEN searchQuery goes to non-empty and back to empty THEN searchQuery emits correct sequence`() = runTest {
        val queriesToEmit = listOf(
            "error",
            "",
            "mock"
        )

        searchRepository.searchQuery.test {
            assertEquals("", awaitItem()) // Assert the initial empty state

            queriesToEmit.forEach { query ->
                searchRepository.updateSearchQuery(query)
                assertEquals(query, awaitItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN searchQuery goes from empty to non-empty THEN searchQuery emits correct sequence`() = runTest {
        val queriesToEmit = listOf(
            "test",
            ""
        )

        searchRepository.searchQuery.test {
            assertEquals("", awaitItem()) // Assert the initial empty state

            queriesToEmit.forEach { query ->
                searchRepository.updateSearchQuery(query)
                assertEquals(query, awaitItem())
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN clearSectionSearchResults is called THEN searchQuery is cleared`() = runTest {
        val initialQuery = "testQuery"

        searchRepository.updateSearchQuery(initialQuery)
        searchRepository.clearSectionSearchResults()

        searchRepository.searchQuery.test {
            assertEquals("", awaitItem())
        }
    }
}