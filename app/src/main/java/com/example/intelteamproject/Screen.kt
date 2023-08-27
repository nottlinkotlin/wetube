package com.example.intelteamproject

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object UserInfo : Screen("info")
    object Main : Screen("main")
    object Board : Screen("board")
    object Messenger : Screen("messenger")
    object Message : Screen("message")
    object Manage : Screen("manage")

    object FeedBack:Screen("feedback")
}