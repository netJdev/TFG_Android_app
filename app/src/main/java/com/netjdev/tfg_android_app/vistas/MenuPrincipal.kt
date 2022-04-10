package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityMenuPrincipalBinding

class MenuPrincipal : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityMenuPrincipalBinding

    // Variable para acceder a la autenticacion de Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = Firebase.auth

        initComponents()

    }

    private fun initComponents(){
        binding.btnLogout.setOnClickListener { logOut() }
    }

    private fun logOut(){
        // Hacer logout de la aplicacion (cuenta de Fierebase)
        auth.signOut()
        // Ir a pantalla de login
        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)
    }

    // Funcion para volver atras
    // Esta es la pantalla principal, si se pulsa volver, se sale de la app
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}