package com.vrgsoft.remote.error

class NetworkError(
    val code: Int,
    val message: String
) : BaseError()