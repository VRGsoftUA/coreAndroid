package com.vrgsoft.retrofit.example

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.vrgsoft.retrofit.core.bind

class MainActivity : AppCompatActivity() {

    val button by bind<Button>(R.id.click)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
