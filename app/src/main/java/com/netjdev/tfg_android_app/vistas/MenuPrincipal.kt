package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.databinding.ActivityMenuPrincipalBinding

class MenuPrincipal : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityMenuPrincipalBinding

    // Variable para acceder a la autenticacion de Firebase
    private lateinit var auth: FirebaseAuth

    // Variable con el email de usuario que se recibe del intent
    private var user_email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = Firebase.auth

        intent.getStringExtra("user_email")?.let { user_email = it }

        if (user_email.isNotEmpty()) {
            initComponents()
        }

    }

    private fun initComponents() {
        binding.btnLogout.setOnClickListener { logOut() }

        // Recuperar datos perfil de usuario

        // Chat
        binding.btnChat.setOnClickListener { chat() }
    }

    private fun chat() {
        val intent = Intent(this, ListOfChatsActivity::class.java)
        intent.putExtra("user_email", user_email)
        startActivity(intent)
    }

    private fun logOut() {
        // Hacer logout de la aplicacion (cuenta de Fierebase)
        auth.signOut()
        // Ir a pantalla de login
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Funcion para volver atras
    // Esta es la pantalla principal, si se pulsa volver, se sale de la app
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}