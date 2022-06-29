package com.starmakerinteractive.thevoic.data.facebook

import com.facebook.applinks.AppLinkData

object FacebookListener : AppLinkData.CompletionHandler {
    override fun onDeferredAppLinkDataFetched(appLinkData: AppLinkData?) {
        Facebook.data.postValue(appLinkData?.targetUri.toString())
    }
}