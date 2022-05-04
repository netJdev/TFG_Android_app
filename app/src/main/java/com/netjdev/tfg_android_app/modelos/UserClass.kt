package com.netjdev.tfg_android_app.modelos

import com.google.firebase.firestore.DocumentId
import java.util.*

data class UserClass(
    var className: String = "",
    var day: String = "",
    var time: String = "",
    var date: Date ?= null,
    // Con esta anotación se agrega automaticamente el id del documento al recuperar los datos de
    // Firebase, este id se podrá usar para eliminar el documento
    @DocumentId
    val id: String? = null
)
