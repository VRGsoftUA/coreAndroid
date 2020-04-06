package com.vrgsoft.core.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleManager {

    var language: String? = null

    fun setLocale(context: Context): Context =
        setNewLocale(context, language ?: getLanguage(context))

    private fun setNewLocale(context: Context, language: String): Context =
        updateResources(context, language)

    private fun getLanguage(context: Context): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].language
        } else {
            context.resources.configuration.locale.language
        }

    private fun updateResources(context: Context, language: String): Context {
        var context = context

        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return context
    }
}