package com.netjdev.tfg_android_app.modelos

data class Chat(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList()
)