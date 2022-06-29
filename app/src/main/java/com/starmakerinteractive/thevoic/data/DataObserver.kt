package com.starmakerinteractive.thevoic.data

import androidx.lifecycle.Observer

class DataObserver(
    private val callBack: (Pair<MutableMap<String, Any>?, String>) -> Unit
) : Observer<Pair<MutableMap<String, Any>?, String>> {
    override fun onChanged(value: Pair<MutableMap<String, Any>?, String>?) {
        value?.let { callBack(it) }
    }
}