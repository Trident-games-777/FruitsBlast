package com.starmakerinteractive.thevoic.app

import android.app.Application
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.starmakerinteractive.thevoic.data.apps_flyer.AppsListener
import com.starmakerinteractive.thevoic.data.facebook.FacebookListener
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FruitApp : Application() {
    private val appsID = "mjrC4EHmLrF4bX9b86w8HS"

    override fun onCreate() {
        super.onCreate()
        AppsFlyerLib.getInstance().init(appsID, AppsListener, this)
        AppsFlyerLib.getInstance().start(this)
        AppLinkData.fetchDeferredAppLinkData(this, FacebookListener)
    }
}