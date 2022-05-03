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

        fun getMonthName(calendar: Calendar): String {
            return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)!!
        }
    }

}