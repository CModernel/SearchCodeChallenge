package com.sonder.domain.mappers

import com.sonder.domain.models.SearchRequestParams
import com.sonder.domain.models.SearchSectionResult
import javax.inject.Inject

class RequestResponseMapper @Inject constructor() {
    fun map(request: SearchRequestParams, result: SearchSectionResult): SearchSectionResult {
        val filteredItems = result.items.filter { item ->
            request.contentTypes.contains(item.contentType?.value)
        }.take(request.size)

        return result.copy(items = filteredItems)
    }

}