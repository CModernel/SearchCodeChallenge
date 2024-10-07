package com.sonder.data

import com.sonder.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: Flow<String>
        get() = _searchQuery

    override fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    override fun clearSectionSearchResults() {
        _searchQuery.value = ""
    }
}