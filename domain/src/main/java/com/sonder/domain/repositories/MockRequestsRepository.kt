package com.sonder.domain.repositories

import com.sonder.domain.models.SearchRequestParams
import kotlinx.coroutines.flow.Flow

interface MockRequestsRepository {
    fun getErrorRequestParams(): Flow<SearchRequestParams>
    fun getHorizontalCompactRequestParams(): Flow<SearchRequestParams>
    fun getVerticalCompactRequestParams(): Flow<SearchRequestParams>
    fun getHorizontaDetailedRequestParams(): Flow<SearchRequestParams>
    fun getVerticalDetailedRequestParams(): Flow<SearchRequestParams>
}