package com.netjdev.tfg_android_app.modelos

data class Notification (
    var name: String = "",
    var body: String = "",
    // SQLite no tiene el tipo de dato date, se usa String
    var date: String = ""
)