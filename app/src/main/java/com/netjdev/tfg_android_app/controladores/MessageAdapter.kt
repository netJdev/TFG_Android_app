package com.netjdev.tfg_android_app.controladores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.Message
import kotlinx.android.synthetic.main.item_message.view.*
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(private val user: String) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: List<Message> = emptyList()

    // Variable para operar con la fecha
    private lateinit var calendar: Calendar

    fun setData(list: List<Message>) {
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {
        val message = messages[position]

        // Ajustar al horario local
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))

        // Se muestra un layout u otro en funcion de quien envia el mensaje
        if (user == message.from) {
            holder.itemView.myMessageLayout.visibility = View.VISIBLE
            holder.itemView.otherMessageLayout.visibility = View.GONE
            holder.itemView.tvMyMsg.text = message.message
            //holder.itemView.tvDateMyMsg.text = message.date.toString()
            calendar.time = message.date
            val time = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
            holder.itemView.tvDateMyMsg.text = time
        } else {
            holder.itemView.myMessageLayout.visibility = View.GONE
            holder.itemView.otherMessageLayout.visibility = View.VISIBLE
            holder.itemView.tvOtherMsg.text = message.message
            //holder.itemView.tvDateOtherMsg.text = message.date.toString()
            calendar.time = message.date
            val time = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
            holder.itemView.tvDateOtherMsg.text = time
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}