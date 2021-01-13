package com.yrgv.akirademo.mainscreen

import android.os.Bundle
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.yrgv.akirademo.R
import com.yrgv.akirademo.data.network.placesapi.PlacesApi

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, MainScreenViewModelFactory(PlacesApi.getInstance()))
            .get(MainScreenViewModel::class.java)
    }

    private lateinit var searchView: AutoCompleteTextView
    private lateinit var searchViewAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        observerViewModel()
    }

    private fun setupViews() {
        searchViewAdapter = SearchResultsAdapter(this) { clickedItem ->
            viewModel.onSearchResultClicked(clickedItem)
        }
        searchView = findViewById(R.id.main_screen_search_view)
        searchView.apply {
            doAfterTextChanged { viewModel.onQueryChanged(it?.toString()) }
            setAdapter(searchViewAdapter)
        }
    }

    private fun observerViewModel() {
        viewModel.getSearchResults().observe(this, { results ->
            searchViewAdapter.showResults(results)
            searchView.showDropDown()
        })
    }

}