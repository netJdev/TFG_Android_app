package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.netjdev.tfg_android_app.databinding.ActivityMenuPrincipalBinding
import com.netjdev.tfg_android_app.databinding.ActivityNotificationsBinding

class NotificationsActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityNotificationsBinding

    private var message_name = ""
    private var message_content = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("message_name")?.let {
            message_name = it
            binding.txtMessageName.text = message_name
        }
        intent.getStringExtra("message_content")?.let {
            message_content = it
            binding.txtMessageContent.text = message_content
        }



        binding.editTextMessageName.setText(message_content)

        Log.d("Sport", "Notification activity Nombre: ${message_name}")
        Log.d("Sport", "Notificacion activity Cuerpo: ${message_content}")
    }
}