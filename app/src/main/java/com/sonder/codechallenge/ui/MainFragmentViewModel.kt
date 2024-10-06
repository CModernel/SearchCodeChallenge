package com.sonder.codechallenge.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonder.codechallenge.ui.models.SearchFragmentStates
import com.sonder.domain.models.SearchAction
import com.sonder.domain.models.SearchActionType
import com.sonder.domain.models.SearchContentType
import com.sonder.domain.models.SearchItem
import com.sonder.domain.models.SearchSectionResult
import com.sonder.domain.usecases.search.SubscribeToSearchQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val subscribeToSearchQueryUseCase: SubscribeToSearchQueryUseCase
) : ViewModel() {

    private var query: String? = null

    // Define fragment states class
    private val _state: MutableStateFlow<SearchFragmentStates> = MutableStateFlow(SearchFragmentStates.Started)
    val state = _state.asStateFlow()

    init {
        subscribeToSearchQuery()
    }

    private fun subscribeToSearchQuery() {
        viewModelScope.launch {
            subscribeToSearchQueryUseCase.execute(Unit).collect {
                if (it.isNotEmpty()) {
                    query = it

                    delay(2000)

                    // Call UseCase to get results
                }
            }
        }
    }

    fun getSectionSearchResults(sectionTitle: String?) {
        sectionTitle?.let { title ->
            viewModelScope.launch {
                // Load results
            }
        }
    }
}