package com.netjdev.tfg_android_app.vistas

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityMenuPrincipalBinding
import kotlinx.android.synthetic.main.activity_menu_principal.*

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

        binding.textView6.text = user_email

        if (user_email.isNotEmpty()) {
            initComponents()
        }

    }

    private fun initComponents() {
        Log.d("Sportcenter","Init menú")
        binding.btnLogout.setOnClickListener { logOut() }

        // Recuperar datos perfil de usuario

        // Chat
        binding.btnChat.setOnClickListener { chat() }
    }

    private fun chat() {
        val intent = Intent(this, ListOfChatsActivity::class.java)
        intent.putExtra("user_email", user_email)
        startActivity(intent)
    }

    private fun logOut() {
        // Hacer logout de la aplicacion (cuenta de Fierebase)
        auth.signOut()
        // Ir a pantalla de login
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Funcion para volver atras
    // Esta es la pantalla principal, si se pulsa volver, se sale de la app
    override fun onBackPressed() {
        // Detectar si el menú lateral está abierto y cerrarlo
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
        finish()
    }

    // Carga de Activity
    private fun loadActivity(context: Activity, activityClass: Class<ProfileActivity>) {
        val intent = Intent(context, activityClass)
        startActivity(intent)
    }

    // Menú lateral
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("Sportcenter","logout")
        when(item.itemId){
            R.id.nav_item_profile->loadActivity(this, ProfileActivity::class.java)
            R.id.nav_item_classes->loadActivity(this, ProfileActivity::class.java)
            R.id.nav_item_payments->loadActivity(this, ProfileActivity::class.java)
            R.id.nav_item_four->logOut()
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
        Log.d("Sportcenter","onOptionsItemSelected")
        if (toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}