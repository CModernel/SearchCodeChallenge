package com.sonder.domain.models

sealed class SearchState {
    data object Searching : SearchState()
    data object Idle : SearchState()

}