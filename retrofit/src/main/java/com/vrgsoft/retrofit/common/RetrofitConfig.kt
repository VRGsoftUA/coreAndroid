package com.vrgsoft.retrofit.common

import okhttp3.Interceptor

object RetrofitConfig {
    lateinit var baseUrl: String
    lateinit var auth: Auth

    internal var enableLogging = false

    internal lateinit var customAuthInterceptor: Interceptor
    internal var useCustomAuthInterceptor = false

    fun enableLogging() {
        enableLogging = true
    }

    fun useCustomAuthInterceptor(interceptor: Interceptor) {
        customAuthInterceptor = interceptor
        useCustomAuthInterceptor = true
    }
}