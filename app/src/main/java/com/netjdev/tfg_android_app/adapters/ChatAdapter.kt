package com.netjdev.tfg_android_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.Chat
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter(val chatClick: (Chat) -> Unit) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // Lista que almacena los chats del usuario, será uno, a no ser que se trate del user admin
    var chats: List<Chat> = emptyList()

    // Funcion para guardar los datos (chats) en la variable chats
    fun setData(list: List<Chat>) {
        chats = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            //
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_chat, viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        //holder.itemView.chatNameText.text = chats[position].id
        holder.itemView.docTypeNameText.text = chats[position].users.toString()

        holder.itemView.setOnClickListener {
            chatClick(chats[position])
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}