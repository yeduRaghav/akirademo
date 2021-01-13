package com.yrgv.akirademo.mainscreen

/**
 * Defines the Place Model for UI
 */
data class PlaceUiModel(
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
