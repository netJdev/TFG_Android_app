package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.adapters.GroupClassAdapter
import com.netjdev.tfg_android_app.databinding.ActivityListOfGroupClassesBinding
import com.netjdev.tfg_android_app.modelos.GroupClass
import com.netjdev.tfg_android_app.util.EspressoIdlingResource
import kotlinx.android.synthetic.main.activity_list_of_group_classes.*
import kotlinx.android.synthetic.main.header.*

class ListOfGroupClassesActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityListOfGroupClassesBinding

    // Instancia de Firebase Firestore
    private var firestore = Firebase.firestore

    // Id de usuario (email)
    private var user_email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfGroupClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("user_email")?.let { user_email = it }

        initComponents()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.group_classes)
        btnHeader.setOnClickListener { onBackPressed() }

        listGroupClassesRecyclerView.layoutManager = LinearLayoutManager(this)
        listGroupClassesRecyclerView.adapter = GroupClassAdapter { groupClass ->
            groupClassSelected(groupClass)
        }
        // Llamada al metodo EspressoIdlingResource (test)
        EspressoIdlingResource.increment()
        // Referencia de almacenamiento desde la aplicacion
        firestore.collection("activities")
            .get()
            .addOnSuccessListener { activities ->
                val listGroupClasses = activities.toObjects(GroupClass::class.java)

                // Pasar al adapter del RecyclerView los datos que se cargaran (lista de classes)
                (listGroupClassesRecyclerView.adapter as GroupClassAdapter).setData(listGroupClasses)

                // Llamada al metodo EspressoIdlingResource (test)
                EspressoIdlingResource.decrement()
            }
    }

    private fun groupClassSelected(groupClass: GroupClass) {
        val intent = Intent(this, ClassReserveActivity::class.java)
        intent.putExtra("class_name", groupClass.name)
        intent.putExtra("user_email", user_email)
        startActivity(intent)
        finish()
    }
}