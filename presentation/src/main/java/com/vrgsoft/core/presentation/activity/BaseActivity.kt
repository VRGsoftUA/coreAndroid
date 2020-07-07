package com.vrgsoft.core.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vrgsoft.core.presentation.common.AppConfigurator
import com.vrgsoft.core.presentation.viewModel.BaseViewModelImpl
import com.vrgsoft.core.utils.ActivityResultProcessor
import com.vrgsoft.core.utils.LocaleManager
import com.vrgsoft.core.utils.MyContextWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.annotations.TestOnly
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.instance

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    //region fields

    private var test: Boolean = false

    private val mainJob = Job()

    private val defaultScope = CoroutineScope(Dispatchers.Main)
    private val activityScope = CoroutineScope(Dispatchers.Main + mainJob)

    protected val mainScope: CoroutineScope
        get() {
            return if (test) {
                defaultScope
            } else {
                activityScope
            }
        }

    private val _parentKodein by closestKodein()
    override val kodein: Kodein by retainedKodein {
        extend(_parentKodein)
        import(kodeinModule, true)
        import(viewModelModule, true)
        import(routerModule, true)
    }

    open val kodeinModule: Kodein.Module = Kodein.Module("default") {}
    open val viewModelModule: Kodein.Module = Kodein.Module("BaseActivity.ViewModel") {}
    open val routerModule: Kodein.Module = Kodein.Module("BaseActivity.Router") {}

    override val kodeinTrigger = KodeinTrigger()

    private var fragmentContainer: Int? = null

    internal val resultProcessor: ActivityResultProcessor by instance()

    //endregion

    override fun attachBaseContext(newBase: Context?) {
        val context = MyContextWrapper.wrap(newBase, LocaleManager.language)

        super.attachBaseContext(context)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        AppConfigurator.customTheme?.let {
            setTheme(it)
        }
        kodeinTrigger.trigger()
        super.onCreate(savedInstanceState)
    }

    override fun onStop() {
        mainJob.cancel()
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        resultProcessor.onActivityResult(requestCode, resultCode, data)
    }

    protected fun View.setAsDefaultBackPressed() {
        setOnClickListener {
            onBackPressed()
        }
    }

    inline fun <reified VM : BaseViewModelImpl> vm(factory: ViewModelProvider.Factory): VM =
        ViewModelProviders.of(this, factory)[VM::class.java]

    //region test methods

    @TestOnly
    fun setFragment(fragment: Fragment) {
        fragmentContainer.let {
            checkNotNull(it) { ERROR_TEXT_CONTAINER_NULL }

            supportFragmentManager.beginTransaction()
                .replace(it, fragment)
                .commit()
        }
    }

    @TestOnly
    fun getCurrentFragment(): Fragment? =
        fragmentContainer.let {
            checkNotNull(it) { ERROR_TEXT_CONTAINER_NULL }

            supportFragmentManager.findFragmentById(it)
        }

    @TestOnly
    fun prepareForTest(containerId: Int) {
        fragmentContainer = containerId
        test = true
    }

    //endregion

    companion object {
        private const val ERROR_TEXT_CONTAINER_NULL =
            "Fragment container is null. Are you call prepareForTest?"
    }
}
