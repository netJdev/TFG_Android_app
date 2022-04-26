package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.Query
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.controladores.TimeAdapter
import com.netjdev.tfg_android_app.databinding.ActivityClassReserveBinding
import com.netjdev.tfg_android_app.modelos.ClassReserveDay
import com.netjdev.tfg_android_app.modelos.ClassReserveTime
import com.netjdev.tfg_android_app.modelos.UserClass
import kotlinx.android.synthetic.main.activity_class_reserve.*
import java.text.SimpleDateFormat
import java.util.*

class ClassReserveActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityClassReserveBinding

    // Instancia de Firebase Storage
    private var firestore = Firebase.firestore

    // Id de usuario (email)
    private var user_email = ""

    // Id de la clase
    private var className = ""

    // Lista de dias
    private var listDays: List<ClassReserveDay> = emptyList()

    // Lista de horarios
    private var listTime: List<ClassReserveTime> = emptyList()

    // Fecha actual
    private val currentDate: Date = Date()
    private lateinit var calendar: Calendar

    // Día de la semana seleccionado
    private var weekdaySelected = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassReserveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("class_name")?.let { className = it }
        intent.getStringExtra("user_email")?.let { user_email = it }

        initComponents()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = className

        // Inicializar calendar
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        calendar.time = currentDate

        printDate(calendar.get(Calendar.DAY_OF_WEEK))

        //Log.d("Sport", "WEEKDAY: ${calendar.get(Calendar.DAY_OF_WEEK)}")
        //Log.d("Sport", "WEEKDAY: ${calendar.time}")

        // Deshabilitar botones de selección de dia
        btnMonday.isEnabled = false
        btnTuesday.isEnabled = false
        btnWednesday.isEnabled = false
        btnThursday.isEnabled = false
        btnFriday.isEnabled = false
        btnSaturday.isEnabled = false

        listTimeRecyclerView.layoutManager = LinearLayoutManager(this)
        listTimeRecyclerView.adapter = TimeAdapter { time ->
            timeSelected(time)
        }

        // Referencia de almacenamiento desde la aplicacion
        var firestoreRef = firestore.collection("activities").document(className).collection("days")
            .orderBy("id", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { classes ->
                listDays = classes.toObjects(ClassReserveDay::class.java)

                // Activar los botones en funcion de la lista de dias
                showBtnDays()
            }
    }

    // Activa los botones de día de la semana en función de los días en los que se imparte la clase
    private fun showBtnDays() {

        listDays.forEach { day ->
            /*
            Los dias de la semana empiezan por el domingo (1)
            1 -> Sunday
            2 -> Monday
            3 -> Tuesday
            ...
             */

            // Sumar dias para hacer pruebas
            //calendar.add(Calendar.DATE, 1)
            val weekday = calendar.get(Calendar.DAY_OF_WEEK)

            //Log.d("Sport", "Date: ${currentDate}")
            //Log.d("Sport", "Weekday: ${weekday}")

            when (day.name) {
                btnMonday.tag -> {
                    if (weekday <= day.id.toInt())
                        btnMonday.isEnabled = true
                    btnMonday.setOnClickListener {
                        loadTime("Monday", day.id.toInt())
                    }
                }
                btnTuesday.tag -> {
                    if (weekday <= day.id.toInt())
                        btnTuesday.isEnabled = true
                    btnTuesday.setOnClickListener {
                        loadTime("Tuesday", day.id.toInt())
                    }
                }
                btnWednesday.tag -> {
                    if (weekday <= day.id.toInt()) {
                        btnWednesday.isEnabled = true
                    }
                    btnWednesday.setOnClickListener {
                        loadTime("Wednesday", day.id.toInt())
                    }
                }
                btnThursday.tag -> {
                    if (weekday <= day.id.toInt())
                        btnThursday.isEnabled = true
                    btnThursday.setOnClickListener {
                        loadTime("Thursday", day.id.toInt())
                    }
                }
                btnFriday.tag -> {
                    if (weekday <= day.id.toInt())
                        btnFriday.isEnabled = true
                    btnFriday.setOnClickListener {
                        loadTime("Friday", day.id.toInt())
                    }
                }
                btnSaturday.tag -> {
                    if (weekday <= day.id.toInt())
                        btnSaturday.isEnabled = true
                    btnSaturday.setOnClickListener {
                        loadTime("Saturday", day.id.toInt())
                    }
                }
            }

        }
    }

    private fun loadTime(dia: String, diaNumero: Int) {
        // Cambiar fecha del txt
        printDate(diaNumero)

        // Guardar día de la semana seleccionado para usarlo en la selección de horario (timeSelected)
        weekdaySelected = dia

        // Referencia de almacenamiento desde la aplicacion
        firestore.collection("activities").document(className).collection("days")
            .document(dia).collection("time")
            .orderBy("id", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { classes ->
                listTime = classes.toObjects(ClassReserveTime::class.java)
                //listTime = classes.toObjects(ClassReserveTime::class.java)
                //Log.d("Sport", "Lista de horarios ${listTime.toString()}")

                (listTimeRecyclerView.adapter as TimeAdapter).setData(listTime, diaNumero)
            }
    }

    private fun printDate(diaSeleccionado: Int) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formato: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        }else{
            //VERSION.SDK_INT < O
        }*/

        // Añadri día con letra
        var diaLetra = ""
        when (diaSeleccionado) {
            1 -> {
                diaLetra = getString(R.string.sunday)
            }
            2 -> {
                diaLetra = getString(R.string.monday)
            }
            3 -> {
                diaLetra = getString(R.string.tuesday)
            }
            4 -> {
                diaLetra = getString(R.string.wednesday)
            }
            5 -> {
                diaLetra = getString(R.string.thursday)
            }
            6 -> {
                diaLetra = getString(R.string.friday)
            }
            7 -> {
                diaLetra = getString(R.string.saturday)
            }
        }

        val txtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        txtCalendar.time = currentDate
        txtCalendar.add(Calendar.DATE, diaSeleccionado - calendar.get(Calendar.DAY_OF_WEEK))

        val formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val dia = formatter.format(txtCalendar.time)
        //Log.d("Sport", "Fecha formateada: ${dia}")
        txtCurrenDay.text = "$diaLetra - $dia"
    }

    private fun timeSelected(time: ClassReserveTime) {
        // Hacer reserva de clase
        //Toast.makeText(this, "Reserva de clase", Toast.LENGTH_SHORT).show()

        // Restar uno al número de plazas disponibles de la clase
        Log.d("Sport", "Time selected: ${time}")
        val numPlazas = time.plazas.toInt() - 1
        firestore.collection("activities").document(className).collection("days")
            .document(weekdaySelected).collection("time").document(time.name)
            .update("plazas", numPlazas.toString())

        // Objeto que guarda las clases reservadas de un usuario
        val userClass = UserClass(
            className = className,
            day = weekdaySelected,
            time = time.name
        )
        //Log.d("Sport", "User ID: ${user_email}")
        // Guardar la clase en el perfil del usuario
        firestore.collection("users").document(user_email).collection("classes")
            .document()
            .set(userClass)
            .addOnSuccessListener {
                val intent = Intent(this, ConfirmReservationActivity::class.java)
                intent.putExtra("estado", "correcto")
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                val intent = Intent(this, ConfirmReservationActivity::class.java)
                intent.putExtra("estado", "incorrecto")
                startActivity(intent)
                finish()
            }
    }
}