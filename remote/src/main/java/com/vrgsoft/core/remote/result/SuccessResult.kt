package com.vrgsoft.core.remote.result

class SuccessResult<T>(
    val data: T
) : BaseResult<T>()