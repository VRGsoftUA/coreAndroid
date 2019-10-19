package com.vrgsoft.retrofit.common

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptor(
    private val auth: Auth? = null
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        return chain?.request()?.newBuilder()?.let {
            it.addHeader(HEADER_AUTHORIZATION, auth?.createAuthHeaderString() ?: "")
            chain.proceed(it.build())
        } ?: Response.Builder().build()
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
    }
}