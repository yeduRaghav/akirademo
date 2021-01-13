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


typealias QueryChangeListener = (query: String?) -> Unit
/**
 * Adapter to render the search results into a lists
 */

class SearchResultsAdapter private constructor(
    context: Context,
    private val queryChangeListener: QueryChangeListener,
    @LayoutRes private val layout: Int,
) : ArrayAdapter<PlaceUiModel>(context, layout) {

    constructor(context: Context, queryChangeListener: QueryChangeListener) : this(
        context,
        queryChangeListener,
        R.layout.layout_search_result
    )
    
    fun update(newResults: List<PlaceUiModel>) {
        clear()
        addAll(newResults)
        notifyDataSetChanged()
    }

    @SuppressLint("ViewHolder") //Ignore, viewHolder pattern is implemented in first-line
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) return convertView

        val place =
            getItem(position) ?: throw IllegalStateException("Check value for position $position")

        val listItem = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return listItem.apply {
            findViewById<MaterialTextView>(R.id.search_result_title)?.text = place.name
            findViewById<MaterialTextView>(R.id.search_result_description)?.text = place.address
        }
    }


    override fun getFilter(): Filter {
        return object:Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults()
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                queryChangeListener(constraint?.toString())
            }
        }
    }

}
