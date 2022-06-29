package com.starmakerinteractive.thevoic.utils

import android.content.Context
import androidx.core.net.toUri
import com.appsflyer.AppsFlyerLib
import com.starmakerinteractive.thevoic.R
import com.starmakerinteractive.thevoic.di.BaseUrl
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*

class UrlCreator @AssistedInject constructor(
    @Assisted private val appsData: MutableMap<String, Any>?,
    @Assisted("deep") private val deepData: String,
    @Assisted("gadid") private val gadid: String,
    @BaseUrl private val baseUrl: String,
    @ApplicationContext private val context: Context
) {
    fun create(): String {
        val prefix = "https://"
        val url = baseUrl.toUri().buildUpon().apply {
            appendQueryParameter(
                context.resources.getString(R.string.secure_get_parametr),
                context.resources.getString(R.string.secure_key)
            )
            appendQueryParameter(
                context.resources.getString(R.string.dev_tmz_key),
                TimeZone.getDefault().id
            )
            appendQueryParameter(context.resources.getString(R.string.gadid_key), gadid)
            appendQueryParameter(context.resources.getString(R.string.deeplink_key), deepData)
            appendQueryParameter(
                context.resources.getString(R.string.source_key),
                appsData?.get("media_source").toString()
            )
            appendQueryParameter(
                context.resources.getString(R.string.af_id_key),
                AppsFlyerLib.getInstance().getAppsFlyerUID(context)
            )
            appendQueryParameter(
                context.resources.getString(R.string.adset_id_key),
                appsData?.get("adset_id").toString()
            )
            appendQueryParameter(
                context.resources.getString(R.string.campaign_id_key),
                appsData?.get("campaign_id").toString()
            )
            appendQueryParameter(
                context.resources.getString(R.string.app_campaign_key),
                appsData?.get("campaign").toString()
            )
            appendQueryParameter(
                context.resources.getString(R.string.adset_key),
                appsData?.get("adset").toString()
            )
            appendQueryParameter(
                context.resources.getString(R.string.adgroup_key),
                appsData?.get("adgroup").toString()
            )
            appendQueryParameter(
                context.resources.getString(R.string.orig_cost_key),
                appsData?.get("orig_cost").toString()
            )
            appendQueryParameter(
                context.resources.getString(R.string.af_siteid_key),
                appsData?.get("af_siteid").toString()
            )
        }.toString()
        return prefix + url
    }
}