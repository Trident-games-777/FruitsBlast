package com.starmakerinteractive.thevoic.utils

import android.content.Context
import com.onesignal.OneSignal
import com.starmakerinteractive.thevoic.di.OneSignalId
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext

class TagSender @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @OneSignalId private val id: String,
    @Assisted private val appsData: MutableMap<String, Any>?,
    @Assisted("deep") private val deepData: String,
    @Assisted("gadid") private val gadid: String,
) {
    fun send() {
        OneSignal.initWithContext(context)
        OneSignal.setAppId(id)
        OneSignal.setExternalUserId(gadid)
        val campaign = appsData?.get("campaign").toString()
        val key = "key2"

        when {
            campaign == "null" && deepData == "null" -> {
                OneSignal.sendTag(key, "organic")
            }
            deepData != "null" -> {
                OneSignal.sendTag(
                    key,
                    deepData.replace("myapp://", "").substringBefore("/")
                )
            }
            campaign != "null" -> {
                OneSignal.sendTag(key, campaign.substringBefore("_"))
            }
        }
    }
}