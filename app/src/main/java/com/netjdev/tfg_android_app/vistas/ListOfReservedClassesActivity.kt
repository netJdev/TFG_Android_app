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
import com.netjdev.tfg_android_app.adapters.ReservedClassAdapter
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
            deleteReservedClassSelected(userClass)
        }

        firestore.collection("users").document(user_email).collection("classes")
            .orderBy("day", Query.Direction.ASCENDING)
            // Para añadir un segundo orderBy hay que añadir indices en Firestore
            //.orderBy("time", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { userClasses ->
                val listUserClasses = userClasses.toObjects(UserClass::class.java)
                //Log.d("Sport", "ReservedClass: ${listUserClasses}")
                //Log.d("Sport", "ReservedClass: ${userClasses.documents[0].id}")

                (listReservedClasesRecyclerView.adapter as ReservedClassAdapter).setData(
                    listUserClasses
                )

            }
            .addOnFailureListener {
                Log.d("Sport", "FAILURE: ${it}")
            }

        // Actualización de la lista de clases en tiempo real
        firestore.collection("users").document(user_email).collection("classes")
            .orderBy("day", Query.Direction.ASCENDING)
            .addSnapshotListener { userClasses, error ->
                if (error == null) {
                    userClasses?.let {
                        val listUserClasses = userClasses.toObjects(UserClass::class.java)
                        (listReservedClasesRecyclerView.adapter as ReservedClassAdapter).setData(
                            listUserClasses
                        )
                    }
                }
            }
    }

    private fun deleteReservedClassSelected(userClass: UserClass) {
        //var texto = "Clase borrada -> ${userClass.id}"
        //Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()

        // Borrar clase de las clases del usuario
        firestore.collection("users").document(user_email).collection("classes")
            .document(userClass.id!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    getString(R.string.class_reservation_removed),
                    Toast.LENGTH_SHORT
                ).show()
                // Sumar una plaza a las plazas disponibles de la clase
                restorePlace(userClass)

            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.error_ocurred),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun restorePlace(userClass: UserClass) {
        // Obtener numero de plazas disponibles de la clase
        firestore.collection("activities").document(userClass.className).collection("days")
            .document(userClass.day).collection("time").document(userClass.time)
            .get()
            .addOnSuccessListener { doc ->
                val numPlazas = doc["plazas"].toString()
                // Incrementar en 1 el número de plazas disponibles
                val numPlazasInt: Int = numPlazas.toInt() + 1
                firestore.collection("activities").document(userClass.className).collection("days")
                    .document(userClass.day).collection("time").document(userClass.time)
                    .update("plazas", numPlazasInt.toString())
            }
    }
}