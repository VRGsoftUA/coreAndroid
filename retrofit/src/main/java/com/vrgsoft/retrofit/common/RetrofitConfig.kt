package com.vrgsoft.retrofit.common

import com.vrgsoft.retrofit.OkHttpConfig
import okhttp3.Interceptor

object RetrofitConfig {
    lateinit var baseUrl: String
    lateinit var auth: Auth

    internal var enableLogging = false

    internal lateinit var customAuthInterceptor: Interceptor
    internal var useCustomAuthInterceptor = false

    internal var okHttpConfig: OkHttpConfig? = null

    fun enableLogging() {
        enableLogging = true
    }

    fun useCustomAuthInterceptor(interceptor: Interceptor) {
        customAuthInterceptor = interceptor
        useCustomAuthInterceptor = true
    }

    fun configureOkHttp(block: OkHttpConfig.() -> Unit) {
        OkHttpConfig().apply(block).let {
            okHttpConfig = it
        }
    }
}