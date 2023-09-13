package com.vrgsoft.core.utils

import android.app.Activity
import android.content.Intent
import android.os.Parcelable

/**
 * Class for processing data received from [Activity.onActivityResult]
 *
 * For processing [Activity.RESULT_CANCELED] set the value for [onCanceled]
 *
 * For processing [Activity.RESULT_OK] set the handler for specified request code and extra key
 * using [handleLongData] for extra data with type [Long]
 */
internal class ActivityResultProcessor {

    /**
     * Class containing data about intent extra that needs to be
     * obtained from [Intent] and passed to the specified handler
     */
    private class RequestData<T : Any>(
        val requestCode: Int,
        private val handler: (T) -> Unit,
        private val onCancel: () -> Unit,
        private val getExtra: (Intent) -> T?
    ) {
        fun handle(data: Intent) {
            getExtra(data)?.let(handler)
        }

        fun sendCancelEvent() {
            onCancel()
        }
    }

    private var dataHandlers: List<RequestData<out Any>> = emptyList()

    /**
     * Caches a handler for the result with the specified [requestCode] and [extraKey]
     *
     * @param requestCode request code for [Activity.onActivityResult]
     * @param extraKey key for intent extra from [Activity.onActivityResult]
     * @param handler handler for data received from intent
     */
    fun handleLongData(
        requestCode: Int,
        extraKey: String,
        handler: (Long) -> Unit,
        onCancel: () -> Unit = {}
    ) {
        RequestData(
            requestCode = requestCode,
            handler = handler,
            getExtra = {
                it.getLongExtra(extraKey, -1L)
            },
            onCancel = onCancel
        ).let {
            dataHandlers = dataHandlers + it
        }
    }

    fun handleRawIntent(
        requestCode: Int,
        handler: (Intent) -> Unit,
        onCancel: () -> Unit = {}
    ) {
        RequestData(
            requestCode = requestCode,
            handler = handler,
            getExtra = {
                it
            },
            onCancel = onCancel
        ).let {
            dataHandlers = dataHandlers + it
        }
    }

    /**
     * Caches a handler for the result with the specified [requestCode] and [extraKey]
     *
     * @param requestCode request code for [Activity.onActivityResult]
     * @param extraKey key for intent extra from [Activity.onActivityResult]
     * @param handler handler for data received from intent
     */
    fun handleStringData(
        requestCode: Int,
        extraKey: String,
        handler: (String) -> Unit,
        onCancel: () -> Unit = {}
    ) {
        RequestData(
            requestCode = requestCode,
            handler = handler,
            getExtra = {
                it.getStringExtra(extraKey).orEmpty()
            },
            onCancel = onCancel
        ).let {
            dataHandlers = dataHandlers + it
        }
    }

    /**
     * Caches a handler for the result with the specified [requestCode] and [extraKey]
     *
     * @param requestCode request code for [Activity.onActivityResult]
     * @param extraKey key for intent extra from [Activity.onActivityResult]
     * @param handler handler for data received from intent
     */
    fun <T : Parcelable> handleParcelableData(
        requestCode: Int,
        extraKey: String,
        handler: (T) -> Unit,
        onCancel: () -> Unit = {}
    ) {
        val data: RequestData<T> = RequestData(
            requestCode = requestCode,
            handler = handler,
            getExtra = {
                it.getParcelableExtra(extraKey)
            },
            onCancel = onCancel
        )

        data.let {
            dataHandlers = dataHandlers + it
        }
    }


    /**
     * Processes data received from [Activity.onActivityResult]
     *
     * @throws IllegalArgumentException if result code is not [Activity.RESULT_OK] or [Activity.RESULT_CANCELED]
     */
    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        when (resultCode) {
            Activity.RESULT_OK -> handleResultOk(requestCode, data)
            Activity.RESULT_CANCELED -> handleResultCanceled(requestCode)
            else -> {
                throw IllegalArgumentException("result code [$resultCode] not supported")
            }
        }
    }

    /**
     * Process intent receiver from [Activity.onActivityResult] with result code [Activity.RESULT_OK]
     *
     * @param requestCode request code received from [ActivityResultProcessor.onActivityResult]
     * @param data intent received from [ActivityResultProcessor.onActivityResult]
     *
     * @throws IllegalArgumentException if [data] is __null__
     */
    private fun handleResultOk(requestCode: Int, data: Intent?) {
        requireNotNull(data)

        dataHandlers.filter {
            it.requestCode == requestCode
        }.forEach {
            it.handle(data)
            dataHandlers = dataHandlers.minus(it)
        }
    }

    /**
     * Process intent receiver from [Activity.onActivityResult] with result code [Activity.RESULT_CANCELED]
     *
     * @param requestCode request code received from [ActivityResultProcessor.onActivityResult]
     */
    private fun handleResultCanceled(requestCode: Int) {
        dataHandlers.filter {
            it.requestCode == requestCode
        }.forEach {
            it.sendCancelEvent()
            dataHandlers = dataHandlers.minus(it)
        }
    }
}