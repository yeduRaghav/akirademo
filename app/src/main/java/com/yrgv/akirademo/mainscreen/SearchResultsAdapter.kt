package com.yrgv.akirademo.mainscreen

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes
import com.google.android.material.textview.MaterialTextView
import com.yrgv.akirademo.R
import com.yrgv.akirademo.utils.setThrottledClickListener


typealias SearchItemClickListener = (clickedItem: SearchResultUiModel) -> Unit

/**
 * Adapter to render the search results into a lists
 */

class SearchResultsAdapter private constructor(
    context: Context,
    private val onClickListener: SearchItemClickListener,
    @LayoutRes private val layout: Int,
) : ArrayAdapter<SearchResultUiModel>(context, layout) {

    constructor(context: Context, onClickListener: SearchItemClickListener) : this(
        context,
        onClickListener,
        R.layout.layout_search_result
    )

    fun showResults(newResults: List<SearchResultUiModel>) {
        clear()
        addAll(newResults)
        notifyDataSetChanged()
    }

    @SuppressLint("ViewHolder") // viewHolder pattern is implemented in first-line
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) return convertView

        val searchResult =
            getItem(position) ?: throw IllegalStateException("Check value for position $position")
        val listItem = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return listItem.apply {
            findViewById<MaterialTextView>(R.id.search_result_title)?.text = searchResult.name
            findViewById<MaterialTextView>(R.id.search_result_description)?.text =
                searchResult.address
            setThrottledClickListener { onClickListener(searchResult) }
        }
    }

    override fun getFilter(): Filter {
        return object:Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults()
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
               //dod nothing
            }

        }
    }
}
