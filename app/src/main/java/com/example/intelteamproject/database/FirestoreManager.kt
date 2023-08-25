package com.example.intelteamproject.database

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.intelteamproject.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreManager {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun saveUser(user: User) {
        val userDocument = usersCollection.document(user.uid)
        userDocument.set(user)
            .addOnSuccessListener {
                db.collection("users").add(user)
                // 성공적으로 저장됨
//                Toast.makeText(context, "사용자 정보 저장 성공", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // 저장 실패
//                Toast.makeText(context, "사용자 정보 저장 실패 $e", Toast.LENGTH_SHORT).show()
            }
    }
}