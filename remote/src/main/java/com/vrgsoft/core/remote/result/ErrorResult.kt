package com.vrgsoft.core.remote.result

import com.vrgsoft.core.remote.error.BaseError

class ErrorResult<T>(
    val error: BaseError
) : BaseResult<T>()