package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityPdfViewerBinding

class PdfViewerActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityPdfViewerBinding

    // Instancia de Firebase Storage
    private var storage = Firebase.storage

    // Variable de AndroidPdfViewer
    private lateinit var pdfView: PDFView

    // Variable para el nombre de la categoria
    private lateinit var categoryName: String
    // Variable para el nombre del documento
    private lateinit var documentName: String

    // Limite de tamaño del archivo pdf que se cargara (1 MB)
    private val pdfSize: Long = 1000000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("category_name")?.let { categoryName = it }
        intent.getStringExtra("document_name")?.let { documentName = it }

        initComponents()
    }

    private fun initComponents(){
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = documentName

        val storageRef = storage.reference.child("documentos").child(categoryName).child(documentName)

        storageRef.getBytes(pdfSize)
            .addOnSuccessListener { bytes ->
                binding.pdfView
                    .fromBytes(bytes)
                    .autoSpacing(true)
                    .load()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Fallo en la carga del archivo", Toast.LENGTH_SHORT).show()
            }
    }
}