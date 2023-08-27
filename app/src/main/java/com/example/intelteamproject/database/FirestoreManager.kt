package com.example.intelteamproject.database

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.intelteamproject.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class FirestoreManager {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun getUser(uid: String): User? {
        return try {
            val userDocument = usersCollection.document(uid).get().result
            if (userDocument != null && userDocument.exists()) {
                userDocument.toObject(User::class.java)?.copy(uid = uid) // UID 추가
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun saveUser(user: User) {
        val userDocument = usersCollection.document(user.uid)
        userDocument.set(user)
            .addOnSuccessListener {
//                db.collection("users").add(user)
                Log.d(TAG, "User data saved successfully: $user")
                // 성공적으로 저장됨
            }
            .addOnFailureListener { e ->
                // 저장 실패
                Log.e(TAG, "Error saving user data", e)
            }
    }
}