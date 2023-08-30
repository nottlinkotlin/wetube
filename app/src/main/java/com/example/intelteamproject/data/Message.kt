package com.example.intelteamproject.data

data class Message(
    val text: String? = null,
    val sender: String = "",
    val senderUid: String = "",
    val senderPhotoUrl: String = "",
    val receiverUid: String = "",
    val timestamp: Any? = null
)
