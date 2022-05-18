package com.netjdev.tfg_android_app.vistas

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.adapters.NotificationAdapter
import com.netjdev.tfg_android_app.databinding.ActivityListOfNotificationsBinding
import com.netjdev.tfg_android_app.modelos.Notification
import com.netjdev.tfg_android_app.sqlite.SQLite
import kotlinx.android.synthetic.main.activity_list_of_notifications.*
import kotlinx.android.synthetic.main.header.*

class ListOfNotificationsActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityListOfNotificationsBinding

    // Lista de notificaciones
    var notificationsList: MutableList<Notification> = ArrayList()

    // Variable que almacena el preference manager
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    /**
     * Cuando se recarga la actividad se recargan los datos de la base de datos, para el caso en el
     * que se recibe una notificación cuando la actividad ListOfNotificationsActivity está abierta.
     */
    override fun onResume() {
        super.onResume()
        loadBdData()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.notifications)
        btnHeader.setOnClickListener { onBackPressed() }

        listNotificationsRecyclerView.layoutManager = LinearLayoutManager(this)
        listNotificationsRecyclerView.adapter = NotificationAdapter()

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        // La variable actv_list_notification_open guarda true si esta abierta, para que no se abra de
        // nuevo al recibir una notificación nueva
        val editor = prefs.edit()
        editor.putBoolean("actv_list_notification_open", true)
        editor.apply()
    }

    private fun loadBdData() {
        notificationsList.clear()
        val conexion = SQLite(this, "bd_notifications", null, 1)
        val bd = conexion.writableDatabase
        val query = "select name, body, date from notifications order by rowid desc"
        val row = bd.rawQuery(query, null)

        // Copiar el resultado de la consulta en la lista de notificaciones
        if (row.moveToFirst()) {
            do {
                val notification =
                    Notification(row.getString(0), row.getString(1), row.getString(2))

                notificationsList.add(notification)
            } while (row.moveToNext())
        }
        // Pasar la lista de notificaciones al adapter del recycler view
        (listNotificationsRecyclerView.adapter as NotificationAdapter).setData(notificationsList)
    }

    /**
     * Se cambia el valor de la variable actv_list_notification_open al cerrar la actividad
     */
    override fun onDestroy() {
        super.onDestroy()
        val editor = prefs.edit()
        editor.putBoolean("actv_list_notification_open", false)
        editor.apply()
        Log.d("Sport", "onDestroy")
    }
}