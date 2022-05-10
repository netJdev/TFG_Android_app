package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityMainBinding

    // Variable Firestore
    private var firestore = Firebase.firestore

    // Variable para acceder a la autenticacion de Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Fijar orientación vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        // Fijar tema principal tras la carga del splash sceen
        setTheme(R.style.Theme_TFG_Android_app)

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = Firebase.auth

        // Añadir funcionalidad a los botones
        binding.btnLogin.setOnClickListener { signIn() }
        binding.btnResetPassword.setOnClickListener { resetPassword() }

        checkUser()

    }

    private fun checkUser() {
        // Recuperar usuario registrado
        val usuarioActual = auth.currentUser

        // Si el usuario está logado accedemos a la pantalla del menu principal
        if (usuarioActual != null) {
            val intent = Intent(this, MenuPrincipal::class.java)
            // Pasar email del usuario registrado
            intent.putExtra("user_email", usuarioActual.email)
            // Iniciar actividad
            startActivity(intent)

            // Terminar actividad actual (actividad de login)
            finish()
        }
    }

    private fun signIn() {
        // Recoger email y contraseña de los text box
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                checkUser()
            } else {
                // Si falla el registro se muestra un mensaje por pantalla
                task.exception?.let {
                    //Toast.makeText(baseContext, it.message, Toast.LENGTH_SHORT).show()
                    Toast.makeText(baseContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun resetPassword() {
        // Inicia la activity para resetear contraseña
        val intent = Intent(this, RecuperarPassword::class.java)
        startActivity(intent)
        finish()
    }
}