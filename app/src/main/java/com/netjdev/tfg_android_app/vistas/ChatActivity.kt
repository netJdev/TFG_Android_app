package com.netjdev.tfg_android_app.vistas

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.adapters.MessageAdapter
import com.netjdev.tfg_android_app.modelos.Message
import com.netjdev.tfg_android_app.databinding.ActivityChatBinding
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.header.*

class ChatActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityChatBinding

    private var chatId = ""
    private var user = ""

    private var firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar datos del intent
        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }

        if (chatId.isNotEmpty() && user.isNotEmpty()) {
            initComponents()
        }
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.btn_chat)
        btnHeader.setOnClickListener { onBackPressed() }

        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = MessageAdapter(user)

        btnSendMsg.setOnClickListener { sendMessage() }

        // Obtener referencia al chat (chatId)
        val chat = firestore.collection("chats").document(chatId)
        // Obtener mensajes del chat de Firestore, ordenados por fecha (campo date)
        chat.collection("messages").orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                (messagesRecyclerView.adapter as MessageAdapter).setData(listMessages)
                // Situarse al final del RecyclerView
                messagesRecyclerView.scrollToPosition(messages.size() - 1)
            }

        // Actialización de mensajes en tiempo real
        chat.collection("messages").orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if (error == null) {
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        (messagesRecyclerView.adapter as MessageAdapter).setData(listMessages)
                        // Situarse al final del RecyclerView
                        messagesRecyclerView.scrollToPosition(messages.size() - 1)
                    }
                }
            }
    }

    private fun sendMessage() {
        // Comprobar que la caja de texto no este vacia
        if (binding.tvMsg.text.toString() != "") {
            val message = Message(
                message = tvMsg.text.toString(),
                from = user
            )

            // Escribir el mensaje en la bd, no se le asigna id al documento de la coleccion messages
            // para que se asigne un id dinamicamente en Firestore
            firestore.collection("chats").document(chatId).collection("messages").document()
                .set(message)

            // Borrar la caja de texto
            binding.tvMsg.setText("")
        }
        // Cerrar el teclado
        val view = this.currentFocus
        view?.let { v ->
            val teclado = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            teclado?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    // Funcion para volver atras
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}