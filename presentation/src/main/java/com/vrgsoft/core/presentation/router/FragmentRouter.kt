package com.vrgsoft.core.presentation.router

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent

abstract class FragmentRouter() : BaseRouter() {

    private var _fragment: Fragment? = null
    protected val fragment: Fragment
        get() = _fragment
            ?: throw IllegalStateException("Router is not attach to Fragment!")

    override val manager: FragmentManager
        get() = fragment.childFragmentManager

    fun attach(fragment: Fragment) {
        _fragment = fragment
        fragment.lifecycle.addObserver(this)
    }

    fun detach() {
        _fragment = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        detach()
    }

    fun finish() {
        fragment.requireActivity().finish()
    }
}