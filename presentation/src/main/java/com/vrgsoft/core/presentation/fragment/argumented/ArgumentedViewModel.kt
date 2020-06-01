package com.vrgsoft.core.presentation.fragment.argumented

import com.vrgsoft.core.presentation.viewModel.BaseViewModel

interface ArgumentedViewModel<A : BaseArguments> :
    BaseViewModel {
    fun applyArguments(args: A)
}