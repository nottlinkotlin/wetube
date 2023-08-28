package com.example.intelteamproject.data

data class User(
    val uid: String,
    val photoUrl: String,
    val name: String,
    val email: String,
    val phone: String = "",
    val position: String = "",
    val isChecked: Boolean = false // 근태 관리
)
