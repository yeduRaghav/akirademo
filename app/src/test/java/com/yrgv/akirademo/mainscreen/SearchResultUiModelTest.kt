package com.yrgv.akirademo.mainscreen

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests SearchResultUiModelTest class
 */
class SearchResultUiModelTest {

    @Test
    fun `toString returns name and address separated by comma followed by space`() {
        val name = "Lorem"
        val address = "olor sit amet, consectetur adipiscing elit."

        val searchResultUiModel = SearchResultUiModel(id = "fakeId", name = name, address = address)
        val generatedString = searchResultUiModel.toString()

        assertFalse(generatedString == "$name ,$address")
        assertFalse(generatedString == "$name,$address")
        assertFalse(generatedString == "$name $address")

        assertFalse(generatedString == "$address ,$name")
        assertFalse(generatedString == "$address,$name")
        assertFalse(generatedString == "$address $name")
        assertFalse(generatedString == "$address, $name")

        assertTrue(generatedString == "$name, $address")
    }

}