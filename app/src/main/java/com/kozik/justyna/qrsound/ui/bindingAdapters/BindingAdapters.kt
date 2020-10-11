package com.kozik.justyna.qrsound.ui.bindingAdapters

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @BindingAdapter("visible")
    @JvmStatic
    fun visible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}