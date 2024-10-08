package com.sonder.domain.usecases.search

import com.sonder.domain.repositories.SearchRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearSearchResultsUseCaseTest {
    private lateinit var useCase: ClearSearchResultsUseCase
    private val searchRepository: SearchRepository = mockk(relaxed = true)

    @Before
    fun setupTest()  {
        useCase = ClearSearchResultsUseCase(searchRepository = searchRepository)
    }

    @Test
    fun `WHEN useCase is executed THEN it clear search results`() = runTest {
        useCase.execute(Unit)

        coVerify(exactly = 1) {
            searchRepository.clearSectionSearchResults()
        }
    }
}