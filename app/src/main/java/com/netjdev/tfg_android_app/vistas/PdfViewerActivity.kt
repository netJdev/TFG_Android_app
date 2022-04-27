package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnTapListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityPdfViewerBinding
import kotlinx.android.synthetic.main.activity_pdf_viewer.*

class PdfViewerActivity : AppCompatActivity(), OnTapListener, OnPageChangeListener {

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

    // Limite de tamaño del archivo pdf que se cargara (10 MB)
    private val pdfSize: Long = 10000000

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
                    .onTap(this)
                    .onPageChange(this)
                    .autoSpacing(true)
                    .load()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Fallo en la carga del archivo", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onTap(e: MotionEvent?): Boolean {
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        //Log.d("Sport", "Page: ${page}")
        //Log.d("Sport", "PageCount: ${pageCount}")
        txtNumPages.text = "${page+1}/${pageCount}"
    }
}