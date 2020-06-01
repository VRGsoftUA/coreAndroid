package com.vrgsoft.core.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.annotations.TestOnly

abstract class BaseViewModelImpl : ViewModel(),
    BaseViewModel {

    private var test: Boolean = false

    private val ioJob = Job()
    private val mainJob = Job()

    private val defaultScope = CoroutineScope(Dispatchers.Main)
    private val viewModelMainScope = CoroutineScope(Dispatchers.Main + mainJob)
    private val viewModelIoScope = CoroutineScope(Dispatchers.Main + ioJob)

    protected val ioScope: CoroutineScope
        get() {
            return if (test) {
                defaultScope
            } else {
                viewModelIoScope
            }
        }

    protected val mainScope: CoroutineScope
        get() {
            return if (test) {
                defaultScope
            } else {
                viewModelMainScope
            }
        }

    override fun onCleared() {
        ioJob.cancel()
        mainJob.cancel()
        super.onCleared()
    }

    //region test methods

    @TestOnly
    fun prepareForTest() {
        test = true
    }

    //endregion
}