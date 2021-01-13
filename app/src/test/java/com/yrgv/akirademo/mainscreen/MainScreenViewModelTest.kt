package com.yrgv.akirademo.mainscreen

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.stubbing
import com.nhaarman.mockitokotlin2.whenever
import com.yrgv.akirademo.data.network.EndpointError
import com.yrgv.akirademo.data.network.placesapi.endpoint.AutoCompleteEndpoint
import com.yrgv.akirademo.mainscreen.AutocompleteViewState.Error.ErrorType
import com.yrgv.akirademo.utils.Either
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Tests for MainScreenViewModel
 */
@ExperimentalCoroutinesApi
class MainScreenViewModelTest {

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun getSpiedViewModel(): MainScreenViewModel {
        val mockedEndpoint = mock<AutoCompleteEndpoint>()
        return spy(MainScreenViewModel(mockedEndpoint))
    }

    @Test
    fun `isAcceptableQuery return true only for non-blank param with length 2 or more`() {
        val viewModel = getSpiedViewModel()
        with(viewModel) {
            assertFalse(null.isAcceptableQuery())
            assertFalse("".isAcceptableQuery())
            assertFalse("   ".isAcceptableQuery())
            assertFalse("1".isAcceptableQuery())

            assertTrue("12".isAcceptableQuery())
            assertTrue("123".isAcceptableQuery())
        }
    }

    @Test
    fun `isAcceptableQuery returns false if same as current query in viewModel`() {
        val viewModel = getSpiedViewModel()
        with(viewModel) {
            query = "value1"
            assertFalse("value1".isAcceptableQuery())
            assertTrue("value2".isAcceptableQuery())
        }
    }

    @Test
    fun `onQueryChanged(null) must attempt to cancel existing searchJob`() {
        val viewModel = getSpiedViewModel()
        viewModel.searchJob = GlobalScope.launch { delay(1000L) }
        viewModel.onQueryChanged(null)
        assert(viewModel.searchJob?.isActive == false)
    }

    @Test
    fun `onQueryChanged(non-null) must attempt to cancel existing searchJob`() {
        val viewModel = getSpiedViewModel()

        val newQuery = "random value"
        stubbing(viewModel.performSearch(newQuery), {})
        viewModel.searchJob = GlobalScope.launch { delay(1000L) }
        viewModel.onQueryChanged(newQuery)
        assert(viewModel.searchJob?.isActive == false)
    }

    @Test
    fun `performSearch() must assign create and assign searchJob`() {
        val mockedEndpoint = mock<AutoCompleteEndpoint>()
        val apiResponse = Either.Error(EndpointError.ClientError.IAmATeapot(null))
        runBlocking {
            stubbing(mockedEndpoint.execute(), {})
            whenever(mockedEndpoint.execute()).thenReturn(apiResponse)
        }
        val viewModel = spy(MainScreenViewModel(mockedEndpoint))
        whenever(viewModel.onApiSearchFailure(apiResponse.error)).then { }

        viewModel.searchJob = null
        viewModel.performSearch("random value")
        assertTrue(viewModel.searchJob != null)
    }


    @Test
    fun `getSearchResultErrorType return NETWORK_ISSUE for EndpointError-Unreachable`() {
        val vm = getSpiedViewModel()
        val errorType = vm.getSearchResultErrorType(EndpointError.Unreachable(null))
        assertTrue(errorType == ErrorType.NETWORK_ISSUE)
    }

    //this test is not at all extensive to truly cover all cases where ErrorType.OTHER will be returned
    @Test
    fun `getSearchResultErrorType return OTHER for non-EndpointError-Unreachable`() {
        val vm = getSpiedViewModel()
        val errorType = vm.getSearchResultErrorType(EndpointError.ClientError.IAmATeapot(null))
        assertTrue(errorType == ErrorType.OTHER)
    }


}