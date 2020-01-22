package com.vrgsoft.core.presentation.fragment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModelImpl : ViewModel(), BaseViewModel {

    private val ioJob = Job()
    private val mainJob = Job()

    protected val ioScope = CoroutineScope(Dispatchers.IO + ioJob)
    protected val mainScope = CoroutineScope(Dispatchers.Main + mainJob)

    override fun onCleared() {
        ioJob.cancel()
        mainJob.cancel()
        super.onCleared()
    }
}