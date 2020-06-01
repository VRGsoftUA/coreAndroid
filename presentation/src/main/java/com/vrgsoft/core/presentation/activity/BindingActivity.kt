package com.vrgsoft.core.presentation.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.vrgsoft.core.presentation.common.LayoutResProcessor
import com.vrgsoft.core.presentation.viewModel.BaseViewModel

abstract class BindingActivity<B : ViewDataBinding> : BaseActivity() {

    private lateinit var _binding: B
    protected val binding: B
        get() = _binding
    abstract val viewModel: BaseViewModel

    private val layoutResProcessor: LayoutResProcessor by lazy {
        LayoutResProcessor(
            context = this@BindingActivity,
            superClass = this.javaClass.superclass,
            superClassGeneric = this.javaClass.genericSuperclass
        )
    }

    /**
     * Retrieves a resource from a fragment class,
     * You can override to specify a resource manually
     */
    @LayoutRes
    open fun getLayoutRes(): Int = layoutResProcessor.getLayoutRes()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.inflate(LayoutInflater.from(this), getLayoutRes(), null, false)
        _binding.lifecycleOwner = this

        lifecycle.addObserver(viewModel)

        setContentView(_binding.root)
    }
}