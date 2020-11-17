package com.vrgsoft.retrofit.common

import com.vrgsoft.retrofit.OkHttpConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient

object RetrofitConfig {
    lateinit var baseUrl: String
    lateinit var auth: Auth

    internal var enableLogging = false

    @Deprecated("unused")
    internal lateinit var customAuthInterceptor: Interceptor

    @Deprecated("use useDefaultAuthInterceptor")
    internal var useCustomAuthInterceptor = false

    internal var useDefaultAuthInterceptor = false

    internal var okHttpConfig: OkHttpConfig? = null

    internal var customOkHttpConfiguration: ((OkHttpClient.Builder) -> Unit)? = null

    fun enableLogging() {
        enableLogging = true
    }

    @Deprecated("use useDefaultAuthInterceptor()")
    fun useCustomAuthInterceptor(interceptor: Interceptor) {
    }

    fun useDefaultAuthInterceptor(){
        useDefaultAuthInterceptor = true
    }

    fun configureOkHttp(block: OkHttpConfig.() -> Unit) {
        OkHttpConfig().apply(block).let {
            okHttpConfig = it
        }
    }

    fun customHttpConfiguration(block: (OkHttpClient.Builder) -> Unit) {
        customOkHttpConfiguration = block
    }
}