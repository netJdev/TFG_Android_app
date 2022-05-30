package com.netjdev.tfg_android_app.vistas

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.netjdev.tfg_android_app.databinding.ActivityNotificationsBinding
import com.netjdev.tfg_android_app.sqlite.SQLite
import com.netjdev.tfg_android_app.util.Utilities
import java.util.*

class NotificationsActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityNotificationsBinding

    // Variable que almacena el preference manager
    private lateinit var prefs: SharedPreferences

    private var notification_name = ""
    private var notification_body = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        intent.getStringExtra("message_name")?.let {
            notification_name = it

        }
        intent.getStringExtra("message_content")?.let {
            notification_body = it
        }

        initComponents()
    }

    private fun initComponents() {
        binding.txtMessageName.text = notification_name
        binding.txtCloseNotification.setOnClickListener { onBackPressed() }
        binding.txtShowNotification.setOnClickListener { showNotifications() }

        // Insertar notificación en SQLite
        insertNotificationSQLite()
    }

    private fun insertNotificationSQLite() {
        val conexion = SQLite(this, "bd_notifications", null, 1)
        val bd = conexion.writableDatabase

        val notification = ContentValues()
        notification.put("name", notification_name)
        notification.put("body", notification_body)
        notification.put("date", Utilities.getStringDate(Date()))
        bd.insert("notifications", null, notification)
    }

    private fun showNotifications() {
        Log.d("Sport", "showNotifications: " + prefs.getBoolean("actv_list_notification_open",false))
        // Se comprueba que la actividad ListOfNotificationsActivity no está abierta, para que no
        // se abra dos veces.
        if (!prefs.getBoolean("actv_list_notification_open", false)) {
            Log.d("Sport", "shared: false")
            val intent = Intent(this, ListOfNotificationsActivity::class.java)
            startActivity(intent)
            //Finalizar activity
            finish()
            //Elimina la animación de cerrar ventana
            overridePendingTransition(0, 0)
        } else {
            Log.d("Sport", "shared: true")
            //Finalizar activity
            finish()
            //Elimina la animación de cerrar ventana
            overridePendingTransition(0, 0)
        }
    }

    // Funcion para volver atras
    override fun onBackPressed() {
        super.onBackPressed()
        //Finalizar activity
        finish()
        //Elimina la animación de cerrar ventana
        overridePendingTransition(0, 0)
    }
}