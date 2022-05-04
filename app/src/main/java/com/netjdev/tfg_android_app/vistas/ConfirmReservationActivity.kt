package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityConfirmReservationBinding
import kotlinx.android.synthetic.main.header.*

class ConfirmReservationActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityConfirmReservationBinding

    // Variable que guarda el estado de la reserva (correcto/incorrecto)
    private var estado = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("estado")?.let { estado = it }

        initComponents()
    }

    private fun initComponents() {
        // Ocultar action bar
        //supportActionBar?.hide()

        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.confirm_reservation)
        btnHeader.setOnClickListener { onBackPressed() }

        // Ocultar imagenes
        binding.imgCorrecto.visibility = View.INVISIBLE
        binding.imgIncorrecto.visibility = View.INVISIBLE

        // Botón cerrar
        binding.btnCloseConfirm.setOnClickListener { onBackPressed() }

        if (estado == "correcto") {
            binding.imgCorrecto.visibility = View.VISIBLE
            binding.txtConfirm.text = getString(R.string.class_reservation_completed)
        } else {
            binding.imgIncorrecto.visibility = View.VISIBLE
            binding.txtConfirm.text = getString(R.string.class_reservation_incompleted)
        }
    }

    // Funcion para volver atras, al menú principal
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}