package com.starmakerinteractive.thevoic.di

import android.content.Context
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.starmakerinteractive.thevoic.utils.TagSender
import com.starmakerinteractive.thevoic.utils.UrlCreator
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile("link_file") }
        )
    }

    @Provides
    fun provideGadidScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.Default)
    }

    @Singleton
    @Provides
    @BaseUrl
    fun provideDefaultUrl(): String = "lkoydtdxfchgj.xyz/blaast.php"

    @Singleton
    @Provides
    @OneSignalId
    fun provideOneSignalId(): String = "93825965-6db7-4c6c-a164-8445370808d4"

    @Singleton
    @Provides
    @UrlPrefKey
    fun provideUrlKey(): String = "url_key"
}

@AssistedFactory
interface UrlCreatorFactory {
    fun create(
        appsData: MutableMap<String, Any>?,
        @Assisted("deep") deepData: String,
        @Assisted("gadid") gadid: String
    ): UrlCreator
}

@AssistedFactory
interface TagSenderFactory {
    fun create(
        appsData: MutableMap<String, Any>?,
        @Assisted("deep") deepData: String,
        @Assisted("gadid") gadid: String
    ): TagSender
}