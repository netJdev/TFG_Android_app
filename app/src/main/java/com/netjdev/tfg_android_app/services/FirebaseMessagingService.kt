package com.netjdev.tfg_android_app.services

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.netjdev.tfg_android_app.vistas.NotificationsActivity

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, NotificationsActivity::class.java)
        intent.putExtra("message_name", message.notification?.title.toString())
        intent.putExtra("message_content", message.notification?.body.toString())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }
}