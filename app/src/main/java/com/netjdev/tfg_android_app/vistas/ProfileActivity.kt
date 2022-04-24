package com.netjdev.tfg_android_app.vistas

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityProfileBinding
import com.netjdev.tfg_android_app.modelos.User

class ProfileActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityProfileBinding

    // Variable Firestore
    private var firestore = Firebase.firestore

    // Variable con el email de usuario que se recibe del intent
    private var user_email = ""

    // Variable que almacena el preference manager
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("user_email")?.let { user_email = it }

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        initComponents()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.profile)

        binding.btnEditar.setOnClickListener { enableEditData() }
        binding.btnCancelar.setOnClickListener { onBackPressed() }
        binding.btnGuardar.setOnClickListener { editFirestoreUserData() }

        // Si Shared preferences esta vacío, se carga de Firestore
        if (prefs.getString("email", "empty")!! != "empty") {
            loadComponents()
        } else {
            loadFirestoreUserData()
        }

        // **BORRAR
        //removeSharedPreferences()
    }

    private fun loadFirestoreUserData() {
        firestore.collection("users").document(user_email).collection("profile")
            .document("user_profile")
            .get()
            .addOnSuccessListener { documento ->
                Log.d("Usuario", documento.toString())
                // Si el documento no existe en Firestore se crea, si existe se guarda en shared preferences
                if (documento.exists()) {
                    Log.d("Usuario", "EXISTE")
                    val user_data = documento.toObject(User::class.java)
                    // Guardar los datos en Shared preferences
                    saveSharedPreferences(user_data!!)
                } else {
                    Log.d("Usuario", "NO EXISTE")
                    createUserData(user_email)
                }
            }
    }

    private fun createUserData(user: String) {
        val profile = User(
            email = user_email,
            name = "",
            surname1 = "",
            surname2 = "",
            tlf = 0,
            address = "",
            num_socio = 0
        )
        firestore.collection("users").document(user).collection("profile").document("user_profile")
            .set(profile)
        //firestore.collection("users").document(user).set(profile)

        saveSharedPreferences(profile)
    }

    private fun loadComponents() {
        binding.editTextEmail.setText(prefs.getString("email", getString(R.string.empty)))
        binding.editTextNumSocio.setText(prefs.getInt("numSocio", 0).toString())
        binding.editTextName.setText(prefs.getString("name", getString(R.string.empty)))
        binding.editTextSurname1.setText(prefs.getString("surname1", getString(R.string.empty)))
        binding.editTextSurname2.setText(prefs.getString("surname2", getString(R.string.empty)))
        binding.editTextPhone.setText(prefs.getInt("tlf", 0).toString())
        binding.editTextAddress.setText(prefs.getString("address", getString(R.string.empty)))
    }

    private fun saveSharedPreferences(user_data: User) {
        // Habilitar modo edicion del preference manager
        val editor = prefs.edit()
        editor.putString("email", user_data.email)
        editor.putInt("numSocio", user_data.num_socio)
        editor.putString("name", user_data.name)
        editor.putString("surname1", user_data.surname1)
        editor.putString("surname2", user_data.surname2)
        editor.putInt("tlf", user_data.tlf)
        editor.putString("address", user_data.address)
        // Se guardan los datos en el momento
        editor.apply()

        loadComponents()
    }

    private fun editFirestoreUserData() {
        val profile = User(
            email = binding.editTextEmail.text.toString(),
            name = binding.editTextName.text.toString(),
            surname1 = binding.editTextSurname1.text.toString(),
            surname2 = binding.editTextSurname2.text.toString(),
            // Antes de guargar el número se eliminan los espacios en blanco para evitar el
            tlf = binding.editTextPhone.text.toString().replace("\\s".toRegex(), "").toInt(),
            address = binding.editTextAddress.text.toString(),
            num_socio = binding.editTextNumSocio.text.toString().toInt()
        )
        firestore.collection("users").document(user_email).collection("profile")
            .document("user_profile")
            .set(profile)

        disableEditData()

        loadFirestoreUserData()
    }

    private fun enableEditData() {
        // Habilitar edit texts
        binding.editTextName.isEnabled = true
        binding.editTextSurname1.isEnabled = true
        binding.editTextSurname2.isEnabled = true
        binding.editTextPhone.isEnabled = true
        binding.editTextAddress.isEnabled = true

        // Mostrar botones cancelar y guardar y ocultar boton editar
        binding.btnCancelar.visibility = View.VISIBLE
        binding.btnGuardar.visibility = View.VISIBLE
        binding.btnEditar.visibility = View.INVISIBLE
    }

    private fun disableEditData() {
        // Deshabilitar edit texts
        binding.editTextName.isEnabled = false
        binding.editTextSurname1.isEnabled = false
        binding.editTextSurname2.isEnabled = false
        binding.editTextPhone.isEnabled = false
        binding.editTextAddress.isEnabled = false

        // Ocultar botones cancelar y guardar y mostrar boton editar
        binding.btnCancelar.visibility = View.INVISIBLE
        binding.btnGuardar.visibility = View.INVISIBLE
        binding.btnEditar.visibility = View.VISIBLE
    }

    private fun removeSharedPreferences() {
        // Habilitar modo edicion del preference manager
        val editor = prefs.edit()
        editor.remove("email")
        editor.remove("numSocio")
        editor.remove("name")
        editor.remove("surname1")
        editor.remove("surname2")
        editor.remove("tlf")
        editor.remove("address")
        // Se guardan los datos en el momento
        editor.apply()
    }

    // Funcion para volver atras
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}