package com.starmakerinteractive.thevoic.data

import androidx.lifecycle.MediatorLiveData
import com.starmakerinteractive.thevoic.data.apps_flyer.AppsFlyer
import com.starmakerinteractive.thevoic.data.facebook.Facebook

object DataHolder {
    val data = MediatorLiveData<Pair<MutableMap<String, Any>?, String>>().apply {
        var appsSourceTriggered = false
        var deepSourceTriggered = false

        var appsData: MutableMap<String, Any>? = null
        var deepData: String = ""

        addSource(AppsFlyer.data) {
            appsSourceTriggered = true
            appsData = it
            if (deepSourceTriggered) {
                value = Pair(appsData, deepData)
            }
        }
        addSource(Facebook.data) {
            deepSourceTriggered = true
            deepData = it
            if (appsSourceTriggered) {
                value = Pair(appsData, deepData)
            }
        }
    }
}