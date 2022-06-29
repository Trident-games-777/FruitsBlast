package com.starmakerinteractive.thevoic.utils

import android.content.Context
import android.provider.Settings
import java.io.File

object Settings {
    fun checkConditions(context: Context): Boolean = firstCondition && secondCondition(context)

    private val firstCondition: Boolean
        get() {
            val dirsArray: List<String> = listOf(
                "/sbin/",
                "/system/bin/",
                "/system/xbin/",
                "/data/local/xbin/",
                "/data/local/bin/",
                "/system/sd/xbin/",
                "/system/bin/failsafe/",
                "/data/local/"
            )
            try {
                for (dir in dirsArray) {
                    if (File(dir + "su").exists()) return false
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            return true
        }

    private fun secondCondition(app: Context) = Settings.Global.getString(
        app.contentResolver,
        Settings.Global.ADB_ENABLED
    ) != "1"
}