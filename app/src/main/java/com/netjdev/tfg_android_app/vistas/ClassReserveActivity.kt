package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.Query
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.adapters.ReservedClassAdapter
import com.netjdev.tfg_android_app.adapters.TimeAdapter
import com.netjdev.tfg_android_app.databinding.ActivityClassReserveBinding
import com.netjdev.tfg_android_app.modelos.ClassReserveDay
import com.netjdev.tfg_android_app.modelos.ClassReserveTime
import com.netjdev.tfg_android_app.modelos.UserClass
import com.netjdev.tfg_android_app.util.EspressoIdlingResource
import com.netjdev.tfg_android_app.util.Utilities
import kotlinx.android.synthetic.main.activity_class_reserve.*
import kotlinx.android.synthetic.main.activity_list_of_reserved_classes.*
import kotlinx.android.synthetic.main.header.*
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
    private lateinit var selectedDay: Calendar

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
        btnHeader.setOnClickListener { onBackPressed() }

        // Inicializar calendar
        //calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        calendar = Calendar.getInstance()
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

        // Llamada al metodo EspressoIdlingResource (test)
        EspressoIdlingResource.increment()

        // Referencia de almacenamiento desde la aplicacion
        var firestoreRef = firestore.collection("activities").document(className).collection("days")
            .orderBy("id", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { classes ->
                listDays = classes.toObjects(ClassReserveDay::class.java)

                // Llamada al metodo EspressoIdlingResource (test)
                EspressoIdlingResource.decrement()

                Log.d("Sport", "Lista de dias: ${listDays}")
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

            //Log.d("Sport", "Dia id: ${day.id}")
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

        // Llamada al metodo EspressoIdlingResource (test)
        EspressoIdlingResource.increment()

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

                // Llamada al metodo EspressoIdlingResource (test)
                EspressoIdlingResource.decrement()
            }
    }

    private fun timeSelected(time: ClassReserveTime) {
        // Fijar parametros del campo date
        selectedDay.set(Calendar.HOUR_OF_DAY, time.id.toInt())
        selectedDay.set(Calendar.MINUTE, 0)
        selectedDay.set(Calendar.SECOND, 0)
        selectedDay.set(Calendar.MILLISECOND, 0)

        // Variable que indica si la clase seleccionada ya está reservada
        var claseYaReservada = false

        // Llamada al metodo EspressoIdlingResource (test)
        EspressoIdlingResource.increment()

        // Comprobar si la clase que quiere reservar el usuario ya la ha reservado previamente
        firestore
            .collection("users")
            .document(user_email)
            .collection("classes")
            //.orderBy("day", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { userClasses ->
                val listUserClasses = userClasses.toObjects(UserClass::class.java)
                listUserClasses.forEach { clase ->
                    if (clase.className == className) {
                        if (clase.date?.equals(selectedDay.time) == true) {
                            claseYaReservada = true
                        }
                    }
                }
                // Si la clase no ha sido reservada se procede a la reserva
                if (!claseYaReservada) {

                    // Restar uno al número de plazas disponibles de la clase
                    val numPlazas = time.plazas.toInt() - 1

                    // Llamada al metodo EspressoIdlingResource (test)
                    EspressoIdlingResource.increment()

                    // Disminuir en 1 el número de plazas disponibles
                    firestore.collection("activities").document(className).collection("days")
                        .document(weekdaySelected).collection("time").document(time.name)
                        .update("plazas", numPlazas.toString())

                    // Objeto que guarda las clases reservadas de un usuario
                    val userClass = UserClass(
                        className = className,
                        day = weekdaySelected,
                        time = time.name,
                        date = selectedDay.time
                    )

                    // Guardar la clase en el perfil del usuario
                    firestore.collection("users").document(user_email).collection("classes")
                        .document()
                        .set(userClass)
                        .addOnSuccessListener {
                            val intent = Intent(this, ConfirmReservationActivity::class.java)
                            intent.putExtra("estado", "correcto")
                            startActivity(intent)
                            finish()
                            // Llamada al metodo EspressoIdlingResource (test)
                            EspressoIdlingResource.decrement()
                        }
                        .addOnFailureListener {
                            val intent = Intent(this, ConfirmReservationActivity::class.java)
                            intent.putExtra("estado", "incorrecto")
                            startActivity(intent)
                            finish()
                            // Llamada al metodo EspressoIdlingResource (test)
                            EspressoIdlingResource.decrement()
                        }
                }else{
                    // Si la clase ya ha sido reservada, se le comunica al usuario con un mensaje
                    Toast.makeText(this, getString(R.string.class_already_booked), Toast.LENGTH_SHORT).show()
                }
                // Llamada al metodo EspressoIdlingResource (test)
                EspressoIdlingResource.decrement()
            }
            .addOnFailureListener {
                Log.d("Sport", "FAILURE: ${it}")
            }
        // BORRAR - Modo revisión
        /*if (Utilities.getTestMode()){
            Thread.sleep(2000)
        }*/
    }

    private fun printDate(diaSeleccionado: Int) {
        // Añadir día con letra
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

        //val txtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        selectedDay = Calendar.getInstance()
        selectedDay.time = currentDate
        selectedDay.add(Calendar.DATE, diaSeleccionado - calendar.get(Calendar.DAY_OF_WEEK))

        val formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val dia = formatter.format(selectedDay.time)
        //Log.d("Sport", "Fecha formateada: ${dia}")
        txtCurrenDay.text = "$diaLetra - $dia"
    }
}