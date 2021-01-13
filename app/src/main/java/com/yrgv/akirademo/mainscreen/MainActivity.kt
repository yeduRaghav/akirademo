package com.yrgv.akirademo.mainscreen

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.yrgv.akirademo.R
import com.yrgv.akirademo.data.network.placesapi.PlacesApi
import com.yrgv.akirademo.mainscreen.AutocompleteViewState.Error.ErrorType
import com.yrgv.akirademo.utils.hide
import com.yrgv.akirademo.utils.setThrottledClickListener
import com.yrgv.akirademo.utils.show

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, MainScreenViewModelFactory(PlacesApi.getInstance()))
            .get(MainScreenViewModel::class.java)
    }

    private lateinit var rootView: ConstraintLayout
    private lateinit var searchView: MaterialAutoCompleteTextView
    private lateinit var clearButton: ImageButton
    private lateinit var searchViewAdapter: SearchResultsAdapter
    private var visibleSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        observerViewModel()
    }

    private fun setupViews() {
        rootView = findViewById(R.id.main_screen_root_view)
        clearButton = findViewById(R.id.main_screen_clear_button)
        clearButton.setThrottledClickListener {
            searchView.text = null
        }
        setupSearchView()
    }

    private fun setupSearchView() {
        searchViewAdapter = SearchResultsAdapter(this) { query ->
            viewModel.onQueryChanged(query)
        }
        searchView = findViewById(R.id.main_screen_search_view)
        searchView.apply {
            setAdapter(searchViewAdapter)
            setOnItemClickListener { _, _, position, _ ->
                searchViewAdapter.getItem(position)?.let {
                    viewModel.onPlaceClicked(it)
                }
            }
            doAfterTextChanged { updateClearButton(it?.toString()) }
        }
    }


    // todo: this should be a custom widget composed of autoCompleteView & clearButton, no time now.
    private fun updateClearButton(query: String?) {
        if (query.isNullOrEmpty()) {
            clearButton.hide()
        } else {
            clearButton.show()
        }
    }

    private fun observerViewModel() {
        viewModel.getAutocompleteViewState().observe(this, { state ->
            onAutoCompleteViewStateChanged(state)
        })
    }

    private fun onAutoCompleteViewStateChanged(state: AutocompleteViewState) {
        when (state) {
            is AutocompleteViewState.Success -> handleAutocompleteSuccessState(state)
            is AutocompleteViewState.Error -> handleAutocompleteErrorState(state)
        }
    }

    private fun handleAutocompleteSuccessState(state: AutocompleteViewState.Success) {
        searchViewAdapter.update(state.places)
        if (state.places.isEmpty()) {
            showNoResultsMessage()
        } else {
            hideVisibleSnackbar()
        }
    }

    private fun handleAutocompleteErrorState(state: AutocompleteViewState.Error) {
        searchViewAdapter.clear()
        when (state.type) {
            ErrorType.NETWORK_ISSUE -> showNetworkIssueMessage()
            ErrorType.OTHER -> showGenericErrorMessage()
        }
    }

    private fun hideVisibleSnackbar() {
        visibleSnackBar?.dismiss()
    }

    private fun showNoResultsMessage() {
        hideVisibleSnackbar()
        visibleSnackBar =
            Snackbar.make(rootView, R.string.toast_no_matching_results, Snackbar.LENGTH_LONG)
                .apply {
                    show()
                }
    }

    private fun showNetworkIssueMessage() {
        hideVisibleSnackbar()
        visibleSnackBar =
            Snackbar.make(rootView, R.string.toast_network_issue, Snackbar.LENGTH_SHORT).apply {
                show()
            }
    }

    private fun showGenericErrorMessage() {
        hideVisibleSnackbar()
        visibleSnackBar =
            Snackbar.make(rootView, R.string.toast_generic_error_message, Snackbar.LENGTH_SHORT)
                .apply {
                    show()
                }
    }
}