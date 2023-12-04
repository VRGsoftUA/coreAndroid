package com.vrgsoft.core.presentation.common

import android.content.Context
import org.kodein.di.simpleErasedName
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

class LayoutResProcessor(
    private val context: Context,
    private var superClassGeneric: Type?,
    private var superClass: Class<*>?
) {

    fun getLayoutRes(): Int {
        while (superClassGeneric !is ParameterizedType) {
            if (superClass != null) {
                superClassGeneric = superClass?.genericSuperclass
                superClass = superClass?.superclass
            } else {
                throw Exception("maybe something with BaseFragment?")
            }
        }

        val fragmentLayoutName = (superClassGeneric as ParameterizedType).actualTypeArguments[0]
            .simpleErasedName()
            .replace("Binding", "")
            .split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])".toRegex())
            .joinToString(separator = "_")
            .toLowerCase(Locale.US)

        val resourceName = "${context.applicationContext?.packageName}:layout/$fragmentLayoutName"
        return context.resources.getIdentifier(resourceName, null, null)
    }
}
