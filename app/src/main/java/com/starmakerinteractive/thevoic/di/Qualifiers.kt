package com.starmakerinteractive.thevoic.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UrlPrefKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OneSignalId