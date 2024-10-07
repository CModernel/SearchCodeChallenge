package com.sonder.domain.repositories

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    val searchQuery: Flow<String>
    fun updateSearchQuery(query: String)
    fun clearSectionSearchResults()
}