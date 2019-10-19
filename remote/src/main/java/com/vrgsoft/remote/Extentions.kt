package com.vrgsoft.remote

import com.vrgsoft.remote.result.BaseResult
import com.vrgsoft.remote.result.SuccessResult

fun <T, M> BaseResult<T>.mapDataIfSuccess(mapper: ((item: T) -> M)): M? {
    if (this !is SuccessResult<T>) {
        return null
    }

    return mapper.invoke(this.data)
}

fun <T> BaseResult<T>.getDataIfSuccess(): T? {
    if (this !is SuccessResult<T>) {
        return null
    }

    return this.data
}