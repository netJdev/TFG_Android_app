package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityListOfDocCategoryBinding
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.netjdev.tfg_android_app.adapters.DocCategoryAdapter
import com.netjdev.tfg_android_app.modelos.DocCategory
import kotlinx.android.synthetic.main.activity_list_of_doc_category.*
import kotlinx.android.synthetic.main.header.*


class ListOfDocCategoryActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityListOfDocCategoryBinding

    // Instancia de Firebase Storage
    private var storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfDocCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.btn_docs)
        btnHeader.setOnClickListener { onBackPressed() }

        binding.listDocTypeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.listDocTypeRecyclerView.adapter = DocCategoryAdapter { docCategory ->
            docCategorySelected(docCategory)
        }

        // Referencia de almacenamiento desde la aplicacion
        var storageRef = storage.reference.child("documentos")

        storageRef.listAll()
            .addOnSuccessListener { (items, prefixes) ->
                val listDocs: MutableList<DocCategory> = mutableListOf()

                prefixes.forEach { prefix ->
                    val docType = DocCategory(
                        name = prefix.name
                    )
                    listDocs.add(docType)
                }
                (listDocTypeRecyclerView.adapter as DocCategoryAdapter).setData(listDocs)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Se ha producido un fallo en la carga del documento",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun docCategorySelected(docCategory: DocCategory) {
        val intent = Intent(this, ListOfDocumentsActivity::class.java)
        intent.putExtra("name", docCategory.name)
        startActivity(intent)
    }
}