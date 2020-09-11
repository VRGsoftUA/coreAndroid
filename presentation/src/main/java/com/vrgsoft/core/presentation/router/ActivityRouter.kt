package com.vrgsoft.core.presentation.router

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.vrgsoft.core.presentation.activity.BaseActivity
import com.vrgsoft.core.utils.ActivityResultProcessor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

abstract class ActivityRouter : BaseRouter() {

    private var _activity: BaseActivity? = null
    protected val activity: BaseActivity
        get() = _activity
            ?: throw IllegalStateException("Router is not attach to Activity!")

    override val manager: FragmentManager
        get() = activity.supportFragmentManager

    private val resultProcessor: ActivityResultProcessor
        get() = activity.resultProcessor

    fun attach(activity: BaseActivity) {
        _activity = activity
        activity.lifecycle.addObserver(this)
    }

    fun detach() {
        _activity = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        detach()
    }

    fun finish() {
        activity.finish()
    }

    protected suspend fun Activity.startForRawIntentResultAsync(
        intent: Intent,
        requestCode: Int
    ): Intent{
        val result = Channel<Intent>()

        resultProcessor.handleRawIntent(
            requestCode = requestCode,
            handler = {
                GlobalScope.launch {
                    result.send(it)
                }
            },
            onCancel = {
                GlobalScope.launch {
                    result.send(intent)
                }
            }
        )

        this.startActivityForResult(intent, requestCode)

        return result.receive()
    }

    protected suspend fun Activity.startForLongResultAsync(
        intent: Intent,
        requestCode: Int,
        extraKey: String
    ): Long {
        val result = Channel<Long>()

        resultProcessor.handleLongData(
            requestCode = requestCode,
            extraKey = extraKey,
            handler = {
                GlobalScope.launch {
                    result.send(it)
                }
            },
            onCancel = {
                GlobalScope.launch {
                    result.send(-1L)
                }
            }
        )

        this.startActivityForResult(intent, requestCode)

        return result.receive()
    }

    protected suspend fun Activity.startForStringResultAsync(
        intent: Intent,
        requestCode: Int,
        extraKey: String
    ): String {
        val result = Channel<String>()

        resultProcessor.handleStringData(
            requestCode = requestCode,
            extraKey = extraKey,
            handler = {
                GlobalScope.launch {
                    result.send(it)
                }
            },
            onCancel = {
                GlobalScope.launch {
                    result.send("")
                }
            }
        )

        this.startActivityForResult(intent, requestCode)

        return result.receive()
    }

    protected suspend fun <T : Parcelable> Activity.startForParcelableResultAsync(
        intent: Intent,
        requestCode: Int,
        extraKey: String
    ): T? {
        val result = Channel<T?>()

        resultProcessor.handleParcelableData<T>(
            requestCode = requestCode,
            extraKey = extraKey,
            handler = {
                GlobalScope.launch {
                    result.send(it)
                }
            },
            onCancel = {
                GlobalScope.launch {
                    result.send(null)
                }
            }
        )

        this.startActivityForResult(intent, requestCode)

        return result.receive()
    }
}