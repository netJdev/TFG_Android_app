package com.netjdev.tfg_android_app.modelos

import java.util.*

data class Pago(
    var fechaPago: Date = Date(),
    var cuotaPagada: Date ?= null
)
