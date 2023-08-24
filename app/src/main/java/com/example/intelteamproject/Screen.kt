package com.example.intelteamproject

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Board : Screen("board")
    object Messenger : Screen("messenger")
    object Manage : Screen("manage")
}