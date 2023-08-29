package com.example.intelteamproject.data

data class User(
    val uid: String,
    val photoUrl: String,
    val name: String,
    val email: String,
    val phone: String = "",
    val position: String = "",
    val checked: Boolean = false, // 근태 관리
    val checkedTime: String = "",
    val qrCode: String = "em3j5h6fk44b5"
)
