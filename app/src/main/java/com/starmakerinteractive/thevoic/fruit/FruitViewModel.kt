package com.starmakerinteractive.thevoic.fruit

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.starmakerinteractive.thevoic.data.DataHolder
import com.starmakerinteractive.thevoic.data.DataObserver
import com.starmakerinteractive.thevoic.di.BaseUrl
import com.starmakerinteractive.thevoic.di.TagSenderFactory
import com.starmakerinteractive.thevoic.di.UrlCreatorFactory
import com.starmakerinteractive.thevoic.di.UrlPrefKey
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FruitViewModel @Inject constructor(
    private val preferences: DataStore<Preferences>,
    private val urlCreatorFactory: UrlCreatorFactory,
    private val tagSenderFactory: TagSenderFactory,
    private val gadidScope: CoroutineScope,
    @ApplicationContext context: Context,
    @BaseUrl private val baseUrl: String,
    @UrlPrefKey private val urlKey: String
) : ViewModel() {

    private val _url: MutableLiveData<String> = MutableLiveData()
    val url: LiveData<String> = _url

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val savedUrl =
                preferences.data.map { it[stringPreferencesKey(urlKey)] ?: baseUrl }.first()
            if (savedUrl.contains(baseUrl)) {
                withContext(Dispatchers.Main.immediate) {
                    DataHolder.data.observeForever(dataObserver)
                }
            } else {
                _url.postValue(savedUrl)
            }
        }
    }

    private val dataObserver = DataObserver { (appsData, deepData) ->
        gadidScope.launch {
            @Suppress("BlockingMethodInNonBlockingContext")
            val gadid = AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString()
            val tagSender = tagSenderFactory.create(appsData, deepData, gadid)
            val urlCreator = urlCreatorFactory.create(appsData, deepData, gadid)
            val url = urlCreator.create()
            _url.postValue(url)
            tagSender.send()
        }
    }

    override fun onCleared() {
        super.onCleared()
        DataHolder.data.removeObserver(dataObserver)
    }
}