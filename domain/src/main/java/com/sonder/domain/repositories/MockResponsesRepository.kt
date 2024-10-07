package com.sonder.domain.repositories

import com.sonder.domain.models.SearchSectionResult
import kotlinx.coroutines.flow.Flow

interface MockResponsesRepository {
    fun getErrorResult(): Flow<Exception>
    fun getHorizontalCompactSearchResults(): Flow<SearchSectionResult>
    fun getVerticalCompactSearchResults(): Flow<SearchSectionResult>
    fun getHorizontalDetailedSectionResults(): Flow<SearchSectionResult>
    fun getVerticalDetailedSectionResults(): Flow<SearchSectionResult>
}