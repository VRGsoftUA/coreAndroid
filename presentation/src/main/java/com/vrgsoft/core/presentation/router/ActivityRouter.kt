package com.vrgsoft.core.presentation.router

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent

abstract class ActivityRouter() : BaseRouter() {

    private var _activity: AppCompatActivity? = null
    protected val activity: AppCompatActivity
        get() = _activity
            ?: throw IllegalStateException("Router is not attach to Activity!")

    override val manager: FragmentManager
        get() = activity.supportFragmentManager

    fun attach(activity: AppCompatActivity) {
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
}