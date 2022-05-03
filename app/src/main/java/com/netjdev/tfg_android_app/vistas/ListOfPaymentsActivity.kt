package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.databinding.ActivityListOfPaymentsBinding

class ListOfPaymentsActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityListOfPaymentsBinding

    // Instancia de Firebase Storage
    private var firestore = Firebase.firestore

    // Id de usuario (email)
    private var user_email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfPaymentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}