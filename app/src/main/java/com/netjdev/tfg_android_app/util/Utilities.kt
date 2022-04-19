package com.netjdev.tfg_android_app.util

import com.netjdev.tfg_android_app.modelos.Document

class Utilities {
    companion object {
        fun cleanString(text: String): String {
            var textClean = ""
            textClean = text.replace("\\_".toRegex(), " ")
            textClean = textClean.replace("\\-".toRegex(), " ")
            textClean = textClean.lowercase()
            return textClean
        }
    }

}