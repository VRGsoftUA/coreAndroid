package com.vrgsoft.core.presentation

import com.vrgsoft.core.presentation.common.ModuleProvider
import com.vrgsoft.core.utils.ActivityResultProcessor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

object CoreModule : ModuleProvider.SimpleProvider() {

    override fun Kodein.Builder.applyModule() {
        bind() from singleton {
            ActivityResultProcessor()
        }
    }

    override val moduleTag: String
        get() = TAG

    private const val TAG = "Core"
}