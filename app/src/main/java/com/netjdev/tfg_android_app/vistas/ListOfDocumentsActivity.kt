package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.storage
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.adapters.DocumentAdapter
import com.netjdev.tfg_android_app.databinding.ActivityListOfDocumentsBinding
import com.netjdev.tfg_android_app.modelos.Document
import com.netjdev.tfg_android_app.util.EspressoIdlingResource
import com.netjdev.tfg_android_app.util.Utilities
import kotlinx.android.synthetic.main.activity_list_of_documents.*
import kotlinx.android.synthetic.main.header.*

class ListOfDocumentsActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityListOfDocumentsBinding

    // Instancia de Firebase Storage
    private var storage = Firebase.storage

    private var categoryName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("name")?.let { categoryName = it }

        initComponents()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = categoryName
        btnHeader.setOnClickListener { onBackPressed() }

        binding.listDocumentsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.listDocumentsRecyclerView.adapter = DocumentAdapter { document ->
            documentSelected(document)
        }

        // Llamada al metodo EspressoIdlingResource (test)
        EspressoIdlingResource.increment()

        // Referencia de almacenamiento desde la aplicacion a la categoria seleccionada
        var storageRef = storage.reference.child("documentos/${categoryName}")
        Log.d("Documento", storageRef.toString())
        storageRef.listAll()
            .addOnSuccessListener { (items, prefixes) ->
                Log.d("Documento", "On success")
                val listDocs: MutableList<Document> = mutableListOf()
                items.forEach { item ->
                    val document = Document(
                        name = item.name,
                        cleanName = Utilities.cleanString(item.name)
                    )
                    listDocs.add(document)
                }
                //listDocs.sortedByDescending { it.name }
                val listaOrdenada = listDocs.sortedBy { it.cleanName }
                //val listaOrdenada2 = listDocs.map { it.name }
                (listDocumentsRecyclerView.adapter as DocumentAdapter).setData(listaOrdenada)

                // Llamada al metodo EspressoIdlingResource (test)
                EspressoIdlingResource.decrement()
            }
            .addOnFailureListener {
                Log.d("Documento", "fail")
            }

    }

    private fun documentSelected(document: Document) {
        val intent = Intent(this, PdfViewerActivity::class.java)
        intent.putExtra("category_name", categoryName)
        intent.putExtra("document_name", document.name)
        startActivity(intent)
    }
}