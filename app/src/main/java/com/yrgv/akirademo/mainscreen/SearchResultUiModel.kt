package com.yrgv.akirademo.mainscreen

/**
 * Defines the 'view' level search result
 */
data class SearchResultUiModel(
    val id: String,
    val name: String,
    val address: String
) {

    override fun toString(): String {
        return StringBuilder()
            .append(name)
            .append(", ")
            .append(address)
            .toString()
    }
}
