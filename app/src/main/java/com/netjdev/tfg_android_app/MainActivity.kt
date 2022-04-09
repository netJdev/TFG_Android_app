package com.netjdev.tfg_android_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Fijar tema principal tras la carga del splash sceen
        Thread.sleep(2000)
        setTheme(R.style.Theme_TFG_Android_app)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}