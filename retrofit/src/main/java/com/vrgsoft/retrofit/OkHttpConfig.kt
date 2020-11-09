package com.vrgsoft.retrofit

import java.util.concurrent.TimeUnit

class OkHttpConfig {
    @Deprecated("use [connectionTimeout]")
    var timeout: Timeout? = null

    var connectionTimeout: Timeout? = null
    var writeTimeout: Timeout? = null
    var readTimeout: Timeout? = null

    data class Timeout(
        val time: Long,
        val timeUnit: TimeUnit
    )
}