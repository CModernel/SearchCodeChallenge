package com.sonder.domain.mappers

import com.sonder.domain.models.SearchContentType
import com.sonder.domain.models.SearchItem
import com.sonder.domain.models.SearchRequestParams
import com.sonder.domain.models.SearchSectionResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RequestResponseMapperTest {

    private lateinit var requestResponseMapper: RequestResponseMapper

    @Before
    fun setup() {
        requestResponseMapper = RequestResponseMapper()
    }

    @Test
    fun `WHEN map is called THEN it filters items based on request params`() {
        val requestParams = SearchRequestParams(
            sectionTitle = "Test",
            query = "query",
            size = 2,
            contentTypes = listOf(
                SearchContentType.SERIES.value,
                SearchContentType.ARTICLE.value
            )
        )

        val searchItems = listOf(
            SearchItem(contentType = SearchContentType.ACTION, title = "Item 1"),
            SearchItem(contentType = SearchContentType.CALL_PATHWAY, title = "Item 2"),
            SearchItem(contentType = SearchContentType.SERIES, title = "Item 3"),
            SearchItem(contentType = SearchContentType.ARTICLE, title = "Item 4")
        )

        val searchSectionResult = SearchSectionResult(
            sectionTitle = "Test Section",
            sectionDescription = "Description",
            items = searchItems
        )

        val result = requestResponseMapper.map(requestParams, searchSectionResult)

        val expectedItems = listOf(
            SearchItem(contentType = SearchContentType.SERIES, title = "Item 3"),
            SearchItem(contentType = SearchContentType.ARTICLE, title = "Item 4")
        )

        assertEquals(expectedItems, result.items)
    }

    @Test
    fun `WHEN map is called THEN it limits items to the request size`() {
        val requestParams = SearchRequestParams(
            sectionTitle = "Test",
            query = "query",
            size = 1,
            contentTypes = listOf(SearchContentType.ACTION.value, SearchContentType.CALL_PATHWAY.value)
        )

        val searchItems = listOf(
            SearchItem(contentType = SearchContentType.ACTION, title = "Item 1"),
            SearchItem(contentType = SearchContentType.CALL_PATHWAY, title = "Item 2"),
            SearchItem(contentType = SearchContentType.SERIES, title = "Item 3")
        )

        val searchSectionResult = SearchSectionResult(
            sectionTitle = "Test Section",
            sectionDescription = "Description",
            items = searchItems
        )
        
        val result = requestResponseMapper.map(requestParams, searchSectionResult)

        val expectedItems = listOf(
            SearchItem(contentType = SearchContentType.ACTION, title = "Item 1")
        )

        assertEquals(expectedItems, result.items)
    }
}