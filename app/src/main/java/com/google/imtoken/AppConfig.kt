package com.google.imtoken

import android.content.Context
import androidx.core.content.ContextCompat

object AppConfig {
    var context: Context? = null

    fun getColor(rid: Int): Int? {
        return context?.let { ContextCompat.getColor(it, rid) }
    }
}