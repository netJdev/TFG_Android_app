package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityConfirmPayBinding

class ConfirmPayActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityConfirmPayBinding

    // Variable que guarda el estado del pago (correcto/incorrecto)
    private var estado = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("estado")?.let { estado = it }

        initComponents()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.confirm_reservation)

        // Ocultar imagenes
        binding.imgPayCorrecto.visibility = View.INVISIBLE
        binding.imgPayIncorrecto.visibility = View.INVISIBLE

        // Botón cerrar
        binding.btnClosePayConfirm.setOnClickListener { onBackPressed() }

        if (estado == "correcto") {
            binding.imgPayCorrecto.visibility = View.VISIBLE
            binding.txtPayConfirm.text = getString(R.string.payment_completed)
        } else {
            binding.imgPayIncorrecto.visibility = View.VISIBLE
            binding.txtPayConfirm.text = getString(R.string.payment_incomplete)
        }
    }

    // Funcion para volver atras, al menú principal
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}