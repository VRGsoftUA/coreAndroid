package com.vrgsoft.core.remote.error

class NetworkError(
    val code: Int,
    val message: String
) : BaseError()