package com.starmakerinteractive.thevoic.fruit

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starmakerinteractive.thevoic.di.BaseUrl
import com.starmakerinteractive.thevoic.di.UrlPrefKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FruitWebViewModel @Inject constructor(
    private val preferences: DataStore<Preferences>,
    @UrlPrefKey private val urlKey: String,
    @BaseUrl private val baseUrl: String
) : ViewModel() {

    fun saveUrl(url: String) = viewModelScope.launch {
        val currentUrl =
            preferences.data.map { it[stringPreferencesKey(urlKey)] ?: baseUrl }.first()
        if (currentUrl.contains(baseUrl)) {
            preferences.edit {
                it[stringPreferencesKey(urlKey)] = url
            }
        }
    }
}