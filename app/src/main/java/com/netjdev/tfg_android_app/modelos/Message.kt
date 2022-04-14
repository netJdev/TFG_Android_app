package com.netjdev.tfg_android_app.modelos

import java.util.*

data class Message(
    var message: String = "",
    var from: String = "",
    var date: Date = Date()
)