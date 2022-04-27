package com.netjdev.tfg_android_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.ClassReserveTime
import kotlinx.android.synthetic.main.item_time.view.*
import java.util.*

class TimeAdapter(val timeClick: (ClassReserveTime) -> Unit) :
    RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    // Lista que almacena los documentos de una categoria
    var timeReserves: List<ClassReserveTime> = emptyList()

    // Día de la semana
    var weekday = 0

    // Fecha actual
    private val currentDate: Date = Date()
    private lateinit var calendar: Calendar

    fun setData(list: List<ClassReserveTime>, day: Int) {
        timeReserves = list
        weekday = day
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeAdapter.TimeViewHolder {
        return TimeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_time, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimeAdapter.TimeViewHolder, position: Int) {
        holder.itemView.txtTime.text = timeReserves[position].name

        val plazas = timeReserves[position].plazas.toInt()

        // Comprobar si quedan plazas libres
        // Comprobar que no se ha pasado el día de la clase
        // Comprobar que no se ha pasado la hora de la clase (margen de 1 hora)
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        calendar.time = currentDate
        // Sumar dias para hacer pruebas
        //calendar.add(Calendar.DATE, 1)

        val hora = calendar.get(Calendar.HOUR_OF_DAY).toInt()
        // Marcar horario limite en una hora menos
        val horaLimite = timeReserves[position].id.toInt() - 1
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)

        //Log.d("Sport", "WEEKDAY: ${weekday}")
        //Log.d("Sport", "CURRENTDAY: ${currentDay}")

        // Los botones por defecto se deshabilitan, solo se habilitan si se cumplen las condiciones siguientes
        holder.itemView.btnAddReserve.isEnabled = false

        // Si el día actual es menor que el día seleccionado, no es necesario comprobar el horario
        if (currentDay < weekday) {
            // Comprobar que hay plazas disponibles
            if (plazas > 0) {
                holder.itemView.btnAddReserve.isEnabled = true
                holder.itemView.btnAddReserve.setOnClickListener {
                    timeClick(timeReserves[position])
                }
            }
        } else {
            // Si el dia actual es igual que el día seleccionado hay que revisar los horarios
            if (currentDay == weekday) {
                if (hora < horaLimite) {
                    if (plazas > 0) {
                        holder.itemView.btnAddReserve.isEnabled = true
                        holder.itemView.btnAddReserve.setOnClickListener {
                            timeClick(timeReserves[position])
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return timeReserves.count()
    }

    class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}