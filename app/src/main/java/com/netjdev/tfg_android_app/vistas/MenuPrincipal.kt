package com.netjdev.tfg_android_app.vistas

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.netjdev.tfg_android_app.BuildConfig
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityMenuPrincipalBinding
import kotlinx.android.synthetic.main.activity_menu_principal.*
import kotlinx.android.synthetic.main.menu_principal_include.*

class MenuPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityMenuPrincipalBinding

    // Variable para acceder a la autenticacion de Firebase
    private lateinit var auth: FirebaseAuth

    // Variable con el email de usuario que se recibe del intent
    private var user_email = ""

    // Menu lateral
    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle

    // Variable TAG para Log
    private val TAG = "Sport"

    // Variables de botones
    private lateinit var btnNotifications: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = Firebase.auth

        // Menu
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        toogle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toogle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        // Traer la vista al frente
        //-- Esta instruccion es necesaria porque en el xml activity_main_drawer, el último hijo
        //-- de <DrawerLayout> debe ser <NavigationView/>, si no, no funciona
        navigationView.bringToFront()

        intent.getStringExtra("user_email")?.let { user_email = it }

        //binding.textView6.text = user_email
        //val txtview6 = findViewById<TextView>(R.id.textView6)
        //textView.text = user_email

        if (user_email.isNotEmpty()) {
            //notifications()
            initComponents()
        }

    }

    private fun notifications() {
        Log.d(TAG, "Entrada de notificación")

        if (intent.extras != null) {
            Log.d(TAG, "Notificación en segundo plano")
            Log.d(TAG, "Intent extra title: ${intent.extras!!.getString("title")}")
        }

        // Recibir notificacion dirigida a un solo usuario
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            // Log
            //Log.d(TAG, token)
        })
    }

    private fun initComponents() {
        // Id de la aplicación
        Log.d("Sport", "Package Name: ${BuildConfig.APPLICATION_ID}")

        // Recibir data de notificacion
        if (intent.extras != null) {
            val title = intent.extras!!["title"].toString()
            val body = intent.extras!!["body"].toString()

            Log.d(TAG, "Notificación title: $title")
            Log.d(TAG, "Notificación body: $body")
        }


        // Texto de la cabecera del menu lateral
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val userEmail: TextView = headerView.findViewById(R.id.nav_header_textView)
        userEmail.text = user_email

        // Botones
        //binding.btnChat.setOnClickListener { chat() }
        val btnChat = findViewById<Button>(R.id.btnChat)
        btnChat.setOnClickListener { chat() }
        //binding.btnDocs.setOnClickListener { documents() }
        val btnDocs = findViewById<Button>(R.id.btnDocs)
        btnDocs.setOnClickListener { documents() }
        //
        val btnActivities = findViewById<Button>(R.id.btnActivities)
        btnActivities.setOnClickListener { groupClasses() }
        //
        val btnPayments = findViewById<Button>(R.id.btnPayments)
        btnPayments.setOnClickListener { payments() }

        btnNotifications = findViewById(R.id.btnNotifications)
        btnNotifications.setOnClickListener { notificationsBTN() }
    }

    private fun chat() {
        val intent = Intent(this, ListOfChatsActivity::class.java)
        intent.putExtra("user_email", user_email)
        startActivity(intent)
    }

    private fun documents() {
        val intent = Intent(this, ListOfDocCategoryActivity::class.java)
        //intent.putExtra("user_email", user_email)
        startActivity(intent)
    }

    private fun notificationsBTN() {
        val intent = Intent(this, NotificationsActivity::class.java)
        intent.putExtra("message_name", "Este es el nombre")
        intent.putExtra("message_content", "Este es el mensaje")
        startActivity(intent)
    }

    private fun groupClasses() {
        val intent = Intent(this, ListOfGroupClassesActivity::class.java)
        intent.putExtra("user_email", user_email)
        startActivity(intent)
    }

    private fun payments() {
        val intent = Intent(this, StripeActivity::class.java)
        intent.putExtra("user_email", user_email)
        startActivity(intent)
    }

    private fun logOut() {
        // Hacer logout de la aplicacion (cuenta de Fierebase)
        auth.signOut()
        // Ir a pantalla de login
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Funcion para volver atras
    // Esta es la pantalla principal, si se pulsa volver, se sale de la app
    override fun onBackPressed() {
        // Detectar si el menú lateral está abierto y cerrarlo
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        finish()
    }

    // Carga de Activity
    private fun loadProfileActivity(context: Activity, activityClass: Class<ProfileActivity>) {
        val intent = Intent(context, activityClass)
        intent.putExtra("user_email", user_email)
        startActivity(intent)
    }

    // Carga de Activity
    private fun loadReservedClassesActivity(
        context: Activity,
        activityClass: Class<ListOfReservedClassesActivity>
    ) {
        val intent = Intent(context, activityClass)
        intent.putExtra("user_email", user_email)
        startActivity(intent)
    }

    // Menú lateral
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_profile -> loadProfileActivity(this, ProfileActivity::class.java)
            R.id.nav_item_classes -> loadReservedClassesActivity(
                this,
                ListOfReservedClassesActivity::class.java
            )
            R.id.nav_item_payments -> loadProfileActivity(this, ProfileActivity::class.java)
            R.id.nav_item_four -> logOut()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toogle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("Sportcenter", "onOptionsItemSelected")
        if (toogle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}