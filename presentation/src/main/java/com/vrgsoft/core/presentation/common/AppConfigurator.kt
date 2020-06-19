package com.vrgsoft.core.presentation.common

object AppConfigurator {

    internal var customTheme: Int? = null

    fun useCustomTheme(resId: Int) {
        customTheme = resId
    }
}