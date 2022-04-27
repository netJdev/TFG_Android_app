package com.netjdev.tfg_android_app.services

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.netjdev.tfg_android_app.vistas.NotificationsActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        /*Looper.prepare()

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(baseContext, message.notification?.title, Toast.LENGTH_LONG).show()
        }

        Looper.loop()*/

        /*val intent = Intent(this, NotificationsActivity::class.java)
        intent.putExtra("message_name", message.notification?.title.toString())
        intent.putExtra("message_content", message.notification?.body.toString())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        Log.d("Sport", "Get notification: ${message.notification?.title}")
        Log.d("Sport", "Get notification: ${message.notification?.body}")

        Log.d("Sport", "Get data: ${message.data["title"]}")
        Log.d("Sport", "Get data: ${message.data["body"]}")

        // Comprobar si el mensaje contiene datos
        if (message.data.isNotEmpty()) {
            Log.d("Sport", "data.isNotEmpty: ${message.data}")
        }*/

        // Crear notificacion

        val intent = Intent(this, NotificationsActivity::class.java)
        intent.putExtra("message_name", message.notification?.title.toString())
        intent.putExtra("message_content", message.notification?.body.toString())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }
}