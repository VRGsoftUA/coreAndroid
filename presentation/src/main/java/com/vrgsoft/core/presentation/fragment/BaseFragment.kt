package com.vrgsoft.core.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.annotations.TestOnly
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.simpleErasedName
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<B : ViewDataBinding> : Fragment(), KodeinAware {

    //region fields

    private var test: Boolean = false

    private val mainJob = Job()

    private val defaultScope = CoroutineScope(Dispatchers.Main)
    private val fragmentScope = CoroutineScope(Dispatchers.Main + mainJob)

    protected val mainScope: CoroutineScope
        get() {
            return if (test) {
                defaultScope
            } else {
                fragmentScope
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    lateinit var binding: B

    private var diEnabled = true

    //endregion

    //region Kodein

    private val _parentKodein: Kodein by closestKodein()
    override val kodein: Kodein = Kodein.lazy {
        extend(_parentKodein, true)
        with(parentFragment) {
            if (this is BaseFragment<*>) {
                extend(kodein, true)
            }
        }
        import(viewModelModule, true)
        import(kodeinModule, true)
    }

    open val kodeinModule: Kodein.Module = Kodein.Module("default") {}

    open val viewModelModule: Kodein.Module = Kodein.Module("default2") {}

    override val kodeinTrigger = KodeinTrigger()

    abstract val viewModel: BaseViewModel

    //endregion

    //region lifecycle

    override fun onAttach(context: Context) {
        if (diEnabled) {
            kodeinTrigger.trigger()
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)

        viewCreated(savedInstanceState)
    }

    override fun onStop() {
        mainJob.cancel()
        super.onStop()
    }

    //endregion

    //region abstracts

    abstract fun viewCreated(savedInstanceState: Bundle?)

    //endregion

    //region private methods

    open fun getLayoutRes(): Int {
        var superClassGeneric = this.javaClass.genericSuperclass
        var superClass = this.javaClass.superclass

        while (superClassGeneric !is ParameterizedType) {
            if (superClass != null) {
                superClassGeneric = superClass.genericSuperclass
                superClass = superClass.superclass
            } else {
                throw Exception("maybe something with BaseFragment?")
            }
        }

        val fragmentLayoutName = superClassGeneric.actualTypeArguments[0]
            .simpleErasedName()
            .replace("Binding", "")
            .split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])".toRegex())
            .joinToString(separator = "_")
            .toLowerCase()

        val resourceName = "${context?.applicationContext?.packageName}:layout/$fragmentLayoutName"
        return resources.getIdentifier(resourceName, null, null)
    }

    //endregion

    //region utils

    inline fun <reified VM : BaseViewModelImpl> vm(factory: ViewModelProvider.Factory): VM =
        ViewModelProviders.of(this, factory)[VM::class.java]

    //endregion

    //region test methods

    @TestOnly
    fun prepareForTest(disableDi: Boolean = false) {
        test = true
        diEnabled = !disableDi
    }

    //endregion
}
