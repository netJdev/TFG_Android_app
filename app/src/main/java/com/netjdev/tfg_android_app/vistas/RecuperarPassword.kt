package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityRecuperarPasswordBinding

class RecuperarPassword : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityRecuperarPasswordBinding

    // Variable para acceder a la autenticacion de Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = Firebase.auth

        binding.btnSend.setOnClickListener {
            checkEmail()
        }
    }

    private fun checkEmail() {
        val email = binding.txtEmailReset.text.toString().trim()

        // Comprobar que el email introducido es correcto
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtEmailReset.error = R.string.wrong_email.toString()
        }

        sendEmail(email)
    }

    private fun sendEmail(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(baseContext, R.string.email_sent, Toast.LENGTH_SHORT).show()
                // Volver a la activity de login
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Si el correo no se puede enviar se muestra un mensaje por pantalla
                Toast.makeText(baseContext, R.string.email_not_sent, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funcion para volver atras
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}