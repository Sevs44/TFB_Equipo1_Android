package com.saulhervas.easychat.utils

import android.os.SystemClock
import android.view.View

abstract class DebouncedOnClickListener(private val minimumInterval: Long = 1000) :
    View.OnClickListener {
    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - lastClickTime
        if (elapsedTime <= minimumInterval) {
            return
        }
        lastClickTime = currentClickTime
        onDebouncedClick(v)
    }

    abstract fun onDebouncedClick(v: View)
}