package com.netjdev.tfg_android_app.modelos

import com.google.firebase.firestore.DocumentId

data class UserClass(
    var className: String = "",
    var day: String = "",
    var time: String = "",
    // Con esta anotación se agrega automaticamente el id del documento al recuperar los datos de
    // Firebase, este id se podrá usar para eliminar el documento
    @DocumentId
    val id: String? = null
)
