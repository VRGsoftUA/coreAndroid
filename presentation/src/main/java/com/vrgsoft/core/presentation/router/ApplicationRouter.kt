package com.vrgsoft.core.presentation.router

import android.app.Application
import android.content.Intent

abstract class ApplicationRouter(
    protected val application: Application
) {

    protected fun Intent.asNewTask(): Intent {
        return this.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    protected fun Intent.asTopTask(): Intent {
        return this.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    fun Intent.start() {
        application.startActivity(this)
    }

    protected inline fun <reified T> createIntent(): Intent = Intent(application, T::class.java)
}