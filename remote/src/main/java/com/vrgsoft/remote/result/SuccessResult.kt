package com.vrgsoft.remote.result

class SuccessResult<T>(
    val data: T
) : BaseResult<T>()