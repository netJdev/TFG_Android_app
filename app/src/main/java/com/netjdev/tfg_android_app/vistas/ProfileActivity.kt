package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    private fun initComponents() {
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.profile)
    }
}