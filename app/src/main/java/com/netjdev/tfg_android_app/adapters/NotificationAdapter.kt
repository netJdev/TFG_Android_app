package com.netjdev.tfg_android_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.Notification
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter() :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    // Lista que almacena las notificaciones
    var notifications: List<Notification> = emptyList()

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setData(list: List<Notification>) {
        notifications = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.itemView.txtNotificationName.text = notifications[position].name
        holder.itemView.txtNotificationDate.text = notifications[position].date
        holder.itemView.txtNotificationBody.text = notifications[position].body
        /*holder.itemView.setOnClickListener {
            notificationsClick(notifications[position])
        }*/
    }

    override fun getItemCount(): Int {
        return notifications.count()
    }
}