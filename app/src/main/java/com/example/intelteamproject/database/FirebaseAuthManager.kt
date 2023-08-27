package com.example.intelteamproject.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthenticationManager {

    private val mAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? {
        return mAuth.currentUser
    }
}