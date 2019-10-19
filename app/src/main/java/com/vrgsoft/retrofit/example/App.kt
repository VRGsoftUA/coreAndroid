package com.vrgsoft.retrofit.example

import android.app.Application
import com.vrgsoft.retrofit.core.Contexter

/**
 * Created by LazyLoop.
 */

class App:Application(){
    override fun onCreate() {
        super.onCreate()
        Contexter.init(applicationContext)
    }
}