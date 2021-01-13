package com.yrgv.akirademo.mainscreen

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.yrgv.akirademo.R
import com.yrgv.akirademo.data.network.placesapi.PlacesApi
import com.yrgv.akirademo.utils.hide
import com.yrgv.akirademo.utils.setThrottledClickListener
import com.yrgv.akirademo.utils.show

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, MainScreenViewModelFactory(PlacesApi.getInstance()))
            .get(MainScreenViewModel::class.java)
    }

    private lateinit var searchView: MaterialAutoCompleteTextView
    private lateinit var clearButton: ImageButton

    private lateinit var searchViewAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        observerViewModel()
    }

    private fun setupViews() {
        clearButton = findViewById(R.id.main_screen_clear_button)
        clearButton.setThrottledClickListener {
            searchView.text = null
        }
        searchViewAdapter = SearchResultsAdapter(this) { query ->
            viewModel.onQueryChanged(query)
        }
        searchView = findViewById(R.id.main_screen_search_view)
        searchView.apply {
            setAdapter(searchViewAdapter)
            setOnItemClickListener { _, _, position, _ ->
                searchViewAdapter.getItem(position)?.let {
                    viewModel.onSearchResultClicked(it)
                }
            }
            doAfterTextChanged { setClearButtonVisibility(it?.toString()) }
        }
    }

    // todo: this should be a custom widget with the autoCompleteView & clearButton, no time now
    private fun setClearButtonVisibility(query: String?) {
        if (query.isNullOrEmpty()) {
            clearButton.hide()
        } else {
            clearButton.show()
        }
    }

    private fun observerViewModel() {
        viewModel.getSearchResults().observe(this, { results ->
            searchViewAdapter.showResults(results)
        })
    }

}