package com.vrgsoft.core.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    private val mainJob = Job()

    protected val mainScope = CoroutineScope(Dispatchers.Main + mainJob)

    private val _parentKodein by closestKodein()
    override val kodein: Kodein by retainedKodein {
        extend(_parentKodein)
        import(diModule())
    }
    override val kodeinTrigger = KodeinTrigger()

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        kodeinTrigger.trigger()
        super.onCreate(savedInstanceState)
    }

    override fun onStop() {
        mainJob.cancel()
        super.onStop()
    }

    abstract fun diModule(): Kodein.Module

    protected fun View.setAsDefaultBackPressed() {
        setOnClickListener {
            onBackPressed()
        }
    }

    inline fun <reified VM : BaseViewModelImpl> vm(factory: ViewModelProvider.Factory): VM =
        ViewModelProviders.of(this, factory)[VM::class.java]
}
