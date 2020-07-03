package com.vrgsoft.core.presentation.common

import com.vrgsoft.core.presentation.activity.BaseActivity
import org.kodein.di.Kodein

sealed class ModuleProvider {
    abstract val moduleTag: String

    abstract class SimpleProvider : ModuleProvider() {

        fun get() = Kodein.Module(moduleTag) {
            applyModule()
        }

        abstract fun Kodein.Builder.applyModule()
    }

    abstract class ArgumentedProvider<T : Any> : ModuleProvider() {

        fun get(argument: T) = Kodein.Module(moduleTag) {
            applyModule(argument)
        }

        abstract fun Kodein.Builder.applyModule(argument: T)
    }

    abstract class ActivityModuleProvider<T : BaseActivity> : ModuleProvider() {

        private var activityName: String = DEFAULT_NAME

        override val moduleTag: String
            get() = activityName

        fun get(argument: T): Kodein.Module {
            activityName = argument.javaClass.simpleName
            return Kodein.Module(moduleTag) {
                applyModule(argument)
            }
        }

        fun getRouterModule() = Kodein.Module(moduleTag + ROUTER_MODULE_NAME_SUFFIX) {
            applyRouterModule()
        }

        abstract fun Kodein.Builder.applyModule(argument: T)
        abstract fun Kodein.Builder.applyRouterModule()

        companion object {
            private const val ROUTER_MODULE_NAME_SUFFIX = ".Router"
            private const val DEFAULT_NAME = ""
        }
    }
}