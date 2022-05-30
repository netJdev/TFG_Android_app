package com.netjdev.tfg_android_app.vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.adapters.PaymentAdapter
import com.netjdev.tfg_android_app.databinding.ActivityListOfPaymentsBinding
import com.netjdev.tfg_android_app.modelos.Pago
import kotlinx.android.synthetic.main.activity_list_of_payments.*
import kotlinx.android.synthetic.main.header.*

class ListOfPaymentsActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityListOfPaymentsBinding

    // Instancia de Firebase Storage
    private var firestore = Firebase.firestore

    // Id de usuario (email)
    private var user_email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfPaymentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("user_email")?.let { user_email = it }

        initComponents()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.my_payments)
        btnHeader.setOnClickListener { onBackPressed() }

        binding.listPaymentsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.listPaymentsRecyclerView.adapter = PaymentAdapter { pago ->
        }

        firestore.collection("users").document(user_email).collection("payments")
            .orderBy("cuotaPagada", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { payments ->
                val listPayments = payments.toObjects(Pago::class.java)

                (listPaymentsRecyclerView.adapter as PaymentAdapter).setData(
                    listPayments
                )
            }
            .addOnFailureListener {
            }
    }
}