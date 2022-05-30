package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.header.*
import java.util.*

class ListOfReservedClassesActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityListOfReservedClassesBinding

    // Instancia de Firebase Storage
    private var firestore = Firebase.firestore

    // Id de usuario (email)
    private var user_email = ""

    // Fecha actual
    private val currentDate: Date = Date()
    private lateinit var fechaActual: Calendar

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
        btnHeader.setOnClickListener { onBackPressed() }

        binding.listReservedClasesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.listReservedClasesRecyclerView.adapter = ReservedClassAdapter { userClass ->
            deleteReservedClassSelected(userClass)
        }

        fechaActual = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        fechaActual.time = currentDate
        // Se suman 3 horas, 2 por la zona horaria (GMT+2:00)
        fechaActual.add(Calendar.HOUR, 2)
        fechaActual.set(Calendar.MINUTE, 0)
        fechaActual.set(Calendar.SECOND, 0)
        fechaActual.set(Calendar.MILLISECOND, 0)

        firestore.collection("users").document(user_email).collection("classes")
            .orderBy("day", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { userClasses ->
                val listUserClasses = userClasses.toObjects(UserClass::class.java)
                val listaClasesPendientes: MutableList<UserClass> = mutableListOf()
                listUserClasses.forEach {
                    val compararFecha = it.date?.compareTo(fechaActual.time)

                    if (compararFecha != null) {
                        if (compararFecha >= 0) {
                            listaClasesPendientes.add(it)
                        }
                    }
                }
                (listReservedClasesRecyclerView.adapter as ReservedClassAdapter).setData(
                    listaClasesPendientes
                )
            }
            .addOnFailureListener {
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