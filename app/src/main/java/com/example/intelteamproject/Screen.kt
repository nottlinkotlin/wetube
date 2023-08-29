package com.example.intelteamproject

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object UserInfo : Screen("info")
    object Main : Screen("main")
    object Board : Screen("board")
    object Messenger : Screen("messenger")
//    object Message : Screen("message")
    object Message : Screen("message/{userUid}")
    object Manage : Screen("manage")

    object FeedBack:Screen("feedback")
    object Calendar:Screen("calendar")

    object Community:Screen("community")
}