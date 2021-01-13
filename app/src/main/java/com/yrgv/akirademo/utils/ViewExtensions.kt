package com.yrgv.akirademo.utils

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged

/**
 * Convenient Extension functions for the View classes
 */


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}


/**
 * Click listener for Views with debounce.
 * */
fun View.setThrottledClickListener(delayInMillis: Long = 500L, runWhenClicked: SimpleCallback) {
    setOnClickListener {
        this.isClickable = false
        this.postDelayed({ this.isClickable = true }, delayInMillis)
        runWhenClicked()
    }
}

