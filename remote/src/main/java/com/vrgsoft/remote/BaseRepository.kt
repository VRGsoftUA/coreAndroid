package com.vrgsoft.remote

import androidx.annotation.VisibleForTesting
import com.vrgsoft.remote.error.ConnectionError
import com.vrgsoft.remote.error.NetworkError
import com.vrgsoft.remote.result.BaseResult
import com.vrgsoft.remote.result.ErrorResult
import com.vrgsoft.remote.result.SuccessResult
import kotlinx.coroutines.Deferred
import retrofit2.Response

abstract class BaseRepository {
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    suspend fun <T> execute(call: (() -> Deferred<Response<T>>)): BaseResult<T> {
        try {
            val result = call.invoke().await()

            return if (result.isSuccessful) {
                SuccessResult(result.body()!!)
            } else {
                ErrorResult(
                    NetworkError(
                        result.code(),
                        result.message()
                    )
                )
            }
        } catch (e: Exception) {
            return ErrorResult(ConnectionError())
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun <T, M> SuccessResult<T>.transform(mapper: ((item: T) -> M)): SuccessResult<M> {
        return SuccessResult(
            mapper.invoke(this.data)
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun <T, M> BaseResult<T>.transformIsSuccess(mapper: ((item: T) -> M)): BaseResult<M> {
        return if (this is SuccessResult<*>) {
            (this as SuccessResult<T>).transform(mapper)
        } else {
            ErrorResult(
                (this as ErrorResult<T>).error
            )
        }
    }
}