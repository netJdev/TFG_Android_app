package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.adapters.ChatAdapter
import com.netjdev.tfg_android_app.databinding.ActivityListOfChatsBinding
import com.netjdev.tfg_android_app.modelos.Chat
import com.netjdev.tfg_android_app.util.Utilities
import kotlinx.android.synthetic.main.activity_list_of_chats.*
import java.util.*

class ListOfChatsActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityListOfChatsBinding

    // Variable con el email de usuario que se recibe del intent
    private var user_email = ""

    // Variable Firestore
    private var firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("user_email")?.let { user_email = it }

        if (user_email.isNotEmpty()) {
            initComponents()
        }
    }

    private fun initComponents() {

        binding.btnNewChat.setOnClickListener { newChat() }

        // Ocultar componentes del activiti si el user no es admin
        if (user_email != "admin@email.com") {
            binding.newChatText.visibility = View.INVISIBLE
            binding.btnNewChat.visibility = View.INVISIBLE
            binding.listChatsRecyclerView.visibility = View.INVISIBLE
        }

        listChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        listChatsRecyclerView.adapter = ChatAdapter { chat ->
            chatSelected(chat)
        }

        // Referencia al usuario
        val user = firestore.collection("users").document(user_email)

        // Descargar los chats del usuario
        user.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                // Casting de los documentos descargados de la coleccion a objetos Chat
                val listChats = chats.toObjects(Chat::class.java)

                // Pasar al adapter del RecyclerView los datos que se cargaran (lista de chats)
                (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)

                // Si el user no es el admin, se salta este activity y pasa directamente al chat
                if (user_email != "admin@email.com") {
                    if (chats.size()!=0){
                        chatSelected(listChats[0])
                    }else{
                        // Si el chat del usuario con el Admin no existe se crea
                        newChatAdmin()
                    }
                }
            }
        // BORRAR - Modo revisión
        if (Utilities.getTestMode()){
            Thread.sleep(1000)
        }

        // Actualizacion en tiempo real de la lista de chats
        user.collection("chats").addSnapshotListener { chats, error ->
            if (error == null) {
                chats?.let {
                    val listChats = chats.toObjects(Chat::class.java)
                    // Pasar al adapter del RecyclerView los datos que se cargaran (lista de chats)
                    (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                }
            } else {
                Toast.makeText(baseContext, R.string.chats_load_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Carga la activity chat cuando se selecciona un chat
    private fun chatSelected(chat: Chat) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("user", user_email)
        startActivity(intent)
        finish()
    }

    private fun newChat() {
        // Crear una variable unica que sirve como identificador del chat
        val chatId = UUID.randomUUID().toString()
        // Email del otro usuario, el otro miembro del chat
        val otherUser = newChatText.text.toString()
        // Lista de usuarios (los dos miembros del chat)
        val users = listOf(user_email, otherUser)

        // Crear el chat
        val chat = Chat(
            id = chatId,
            name = "Chat con $otherUser",
            users = users
        )

        // Dar de alta el chat en Firestore, se da de alta en tres sitios, en la coleccion de chats
        // (donde estan todos los chats) y en los chats de cada uno de los dos usuarios que forman
        // el chat
        firestore.collection("chats").document(chatId).set(chat)
        firestore.collection("users").document(user_email).collection("chats").document(chatId)
            .set(chat)
        firestore.collection("users").document(otherUser).collection("chats").document(chatId)
            .set(chat)

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", user_email)
        startActivity(intent)
        // Si el usuario no es admin (netjdev@gmail.com) se finaliza esta activity
        if (user_email != "admin@email.com") {
            finish()
        }
    }

    private fun newChatAdmin() {
        // Crear una variable unica que sirve como identificador del chat
        val chatId = UUID.randomUUID().toString()
        // Email del otro usuario, el otro miembro del chat
        val otherUser = "admin@email.com"
        // Lista de usuarios (los dos miembros del chat)
        val users = listOf(user_email, otherUser)

        // Crear el chat
        val chat = Chat(
            id = chatId,
            name = "Chat con $otherUser",
            users = users
        )

        // Dar de alta el chat en Firestore, se da de alta en tres sitios, en la coleccion de chats
        // (donde estan todos los chats) y en los chats de cada uno de los dos usuarios que forman
        // el chat
        firestore.collection("chats").document(chatId).set(chat)
        firestore.collection("users").document(user_email).collection("chats").document(chatId)
            .set(chat)
        firestore.collection("users").document(otherUser).collection("chats").document(chatId)
            .set(chat)

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", user_email)
        startActivity(intent)
    }
}