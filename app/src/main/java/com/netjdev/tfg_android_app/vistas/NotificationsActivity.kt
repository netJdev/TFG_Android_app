package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityNotificationsBinding
import kotlinx.android.synthetic.main.header.*

class NotificationsActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityNotificationsBinding

    private var message_name = ""
    private var message_content = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.notifications)
        btnHeader.setOnClickListener { onBackPressed() }

        intent.getStringExtra("message_name")?.let {
            message_name = it
            binding.txtMessageName.text = message_name
        }
        intent.getStringExtra("message_content")?.let {
            message_content = it
            binding.txtMessageContent.text = message_content
        }

        //Log.d("Sport", "Notification activity Nombre: ${message_name}")
        //Log.d("Sport", "Notificacion activity Cuerpo: ${message_content}")
    }
}