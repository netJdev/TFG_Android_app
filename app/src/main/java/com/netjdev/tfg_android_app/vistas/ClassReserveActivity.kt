package com.netjdev.tfg_android_app.vistas

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.Query
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.controladores.TimeAdapter
import com.netjdev.tfg_android_app.databinding.ActivityClassReserveBinding
import com.netjdev.tfg_android_app.modelos.ClassReserveDay
import com.netjdev.tfg_android_app.modelos.ClassReserveTime
import kotlinx.android.synthetic.main.activity_class_reserve.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class ClassReserveActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityClassReserveBinding

    // Instancia de Firebase Storage
    private var firestore = Firebase.firestore

    // Id de la clase
    private var className = ""

    // Lista de dias
    private var listDays: List<ClassReserveDay> = emptyList()

    // Lista de horarios
    private var listTime: List<ClassReserveTime> = emptyList()

    // Fecha actual
    private val currentDate: Date = Date()
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassReserveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("class_name")?.let { className = it }

        initComponents()
    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.group_classes)

        // Inicializar calendar
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        calendar.time = currentDate

        printDate()

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
                //listTime = classes.toObjects(ClassReserveTime::class.java)
                //Log.d("Sport", "Lista de horarios ${listTime.toString()}")
                //(listTimeRecyclerView.adapter as TimeAdapter).setData(listTime)
                // Activar los botones en funcion de la lista de dias
                showBtnDays()
            }
    }

    private fun showBtnDays() {

        listDays.forEach { day ->
            /*
            Los dias de la semana empiezan por el domingo (1)
            1 -> Sunday
            2 -> Monday
            3 -> Tuesday
            ...
             */
            //val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
            //calendar.time = currentDate
            // Sumar dias para hacer pruebas
            //calendar.add(Calendar.DATE, 1)
            val weekday = calendar.get(Calendar.DAY_OF_WEEK)

            //Log.d("Sport", "Date: ${currentDate}")
            //Log.d("Sport", "Day week: ${calendar.get(Calendar.DAY_OF_WEEK)}")

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

    private fun printDate() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formato: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        }else{
            //VERSION.SDK_INT < O
        }*/
        val formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val dia = formatter.format(calendar.time)
        //Log.d("Sport", "Fecha formateada: ${dia}")
        txtCurrenDay.text = dia
    }

    private fun timeSelected(time: ClassReserveTime) {
        // Hacer reserva de clase

    }
}