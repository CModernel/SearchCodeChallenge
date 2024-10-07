package com.sonder.domain.models

data class SearchItemRequest (
    val searchType: SearchItemViewType? = null,
    val sections: SearchSectionResult? = null,
)