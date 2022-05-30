package com.netjdev.tfg_android_app.util

import java.util.*

class Utilities {
    companion object {
        fun cleanString(text: String): String {
            var textClean = ""
            textClean = text.replace("\\_".toRegex(), " ")
            textClean = textClean.replace("\\-".toRegex(), " ")
            textClean = textClean.lowercase()
            return textClean
        }

        // Devuelve el nombre del mes
        fun getMonthName(calendar: Calendar): String {
            return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)!!
        }

        // Devuelve la fecha que recibe por parámetro en formato String
        fun getStringDate(currentDate: Date): String {
            var dateString = ""
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            // Fijar formato de dos digitos para día y mes
            val dayMonth = String.format(
                "%02d-%02d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH)+1
            )
            dateString = "$dayMonth-${calendar.get(Calendar.YEAR)}"
            dateString += " ${calendar.get(Calendar.HOUR_OF_DAY)}"
            // Fijar formato de dos digitos para minutos
            val minutes = String.format("%02d", calendar.get(Calendar.MINUTE))
            dateString += ":$minutes"

            return dateString
        }

        // Devuelve true o false para controlar si la aplicación se comportará en modo revisión
        fun getTestMode(): Boolean {
            return false
        }
    }

}