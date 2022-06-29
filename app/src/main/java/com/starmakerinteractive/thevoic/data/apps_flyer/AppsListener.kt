package com.starmakerinteractive.thevoic.data.apps_flyer

import com.appsflyer.AppsFlyerConversionListener

object AppsListener : AppsFlyerConversionListener {
    override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
        data?.let {
            AppsFlyer.data.postValue(data)
        }
    }

    override fun onConversionDataFail(p0: String?) {}
    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}
    override fun onAttributionFailure(p0: String?) {}
}