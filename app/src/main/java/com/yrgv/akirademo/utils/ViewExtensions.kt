package com.yrgv.akirademo.utils

import android.view.View

/**
 * Convenient Extension functions for the View classes
 */


fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
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
        this.isEnabled = false
        this.postDelayed({
            this.isEnabled = true
            this.isClickable = true
        }, delayInMillis)
        runWhenClicked()
    }
}

