package com.vrgsoft.remote.result

import com.vrgsoft.remote.error.BaseError

class ErrorResult<T>(
    val error: BaseError
) : BaseResult<T>()