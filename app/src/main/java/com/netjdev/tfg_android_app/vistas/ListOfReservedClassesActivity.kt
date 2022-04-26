package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.controladores.ReservedClassAdapter
import com.netjdev.tfg_android_app.databinding.ActivityListOfReservedClassesBinding
import com.netjdev.tfg_android_app.modelos.UserClass
import kotlinx.android.synthetic.main.activity_list_of_reserved_classes.*

class ListOfReservedClassesActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityListOfReservedClassesBinding

    // Instancia de Firebase Storage
    private var firestore = Firebase.firestore

    // Id de usuario (email)
    private var user_email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfReservedClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("user_email")?.let { user_email = it }

        initComponents()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.my_classes)

        binding.listReservedClasesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.listReservedClasesRecyclerView.adapter = ReservedClassAdapter { userClass ->
            reservedClassSelected(userClass)
        }

        firestore.collection("users").document(user_email).collection("classes")
            .orderBy("day", Query.Direction.ASCENDING)
            // Para añadir un segundo orderBy hay que añadir indices en Firestore
            //.orderBy("time", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { userClasses ->
                val listUserClasses = userClasses.toObjects(UserClass::class.java)
                Log.d("Sport", "ReservedClass: ${listUserClasses}")
                (listReservedClasesRecyclerView.adapter as ReservedClassAdapter).setData(
                    listUserClasses
                )
            }
            .addOnFailureListener {
                Log.d("Sport", "FAILURE: ${it}")
            }
    }

    private fun reservedClassSelected(userClass: UserClass) {
        Toast.makeText(this, "CLASE BORRADA", Toast.LENGTH_SHORT).show()
        // Borrar clase de las clases del usuario
        // Sumar una plaza a las plazas disponibles de la clase
    }
}