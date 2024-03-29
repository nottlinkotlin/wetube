package com.example.intelteamproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.intelteamproject.compose.AttendanceScreen
import com.example.intelteamproject.compose.BoardScreen
import com.example.intelteamproject.compose.CalendarScreen
import com.example.intelteamproject.compose.CommunityScreen
import com.example.intelteamproject.compose.FeedbackScreen
import com.example.intelteamproject.compose.LoginScreen
import com.example.intelteamproject.compose.MainScreen
import com.example.intelteamproject.compose.ManageScreen
import com.example.intelteamproject.compose.MessageScreen
import com.example.intelteamproject.compose.MessengerScreen
import com.example.intelteamproject.compose.SignatureScreen
import com.example.intelteamproject.compose.UserInfoScreen
import com.example.intelteamproject.data.User
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.example.intelteamproject.database.FirestoreManager
import com.example.intelteamproject.ui.theme.IntelTeamProjectTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mAuth = FirebaseAuth.getInstance()
        // 구글 로그인 구현
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // default_web_client_id 에러 시 rebuild
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        setContent {
            IntelTeamProjectTheme {
//                 A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    val navController = rememberNavController() // navigation
                    //firebase authentication
                    val user: FirebaseUser? = mAuth.currentUser
                    val startDestination = remember {
                        if (user == null) {
                            Screen.Login.route
                        } else {
                            Screen.Main.route
                        }
                    }
                    val signInIntent = googleSignInClient.signInIntent
                    val launcher =
                        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
                            val data = result.data
                            // result returned from launching the intent from GoogleSignInApi.getSignInIntent()
                            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                            val exception = task.exception
                            if (task.isSuccessful) {
                                try {
                                    // Google SignIn was successful, authenticate with firebase
                                    val account = task.getResult(ApiException::class.java)!!
                                    firebaseAuthWithGoogle(
                                        account.idToken!!,
                                        navController = navController
                                    )
                                } catch (e: Exception) {
                                    // Google SignIn failed
                                    Log.d("SignIn", "로그인 실패")
                                }
                            } else {
                                Log.d("SignIn", exception.toString())
                            }
                        }

                    //navigation
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable(Screen.Login.route) {
                            LoginScreen {
                                launcher.launch(signInIntent)
                            }
                        }
                        composable(Screen.UserInfo.route) {
                            UserInfoScreen(
                                navController,
                                onSignOutClicked = { signOut(navController) })
                        }
                        composable(Screen.Main.route) { MainScreen(navController) }
                        composable(Screen.Board.route) { BoardScreen(navController) }
                        composable(Screen.Messenger.route) { MessengerScreen(navController) }
                        composable(Screen.Manage.route) { ManageScreen(navController) { fetchLocation() } }
                        composable(Screen.FeedBack.route) { FeedbackScreen(navController) }
                        composable(Screen.Community.route) { CommunityScreen(navController) }
                        composable(Screen.Calendar.route) { CalendarScreen(navController) }
                        composable(Screen.Message.route,
                            arguments = listOf(navArgument("userUid") { type = NavType.StringType })
                        ) { backStackEntry ->
                            MessageScreen(
                                userUid = backStackEntry.arguments?.getString("userUid") ?: "",
                                navController = navController
                            )
                        }
                        composable(Screen.Signature.route){SignatureScreen(navController)}
                        composable(Screen.Attendance.route){ AttendanceScreen(navController)}
                    }
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, navController: NavController) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // SignIn Successful
                    val currentUser = mAuth.currentUser
                    currentUser?.let {
                        navController.popBackStack()
                        navController.navigate(Screen.UserInfo.route)
                    }
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signOut(navController: NavController) {
        val db = Firebase.firestore
        val authManager = FirebaseAuthenticationManager()
        val currentUser = authManager.getCurrentUser()
        val uid = currentUser?.uid
        val docRef = uid?.let { db.collection("users").document(it) }

        // get the google account
        val googleSignInClient: GoogleSignInClient

        // configure Google SignIn
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Sign Out of all accounts
        mAuth.signOut()
        googleSignInClient.signOut().addOnSuccessListener {
            docRef?.delete()
                ?.addOnSuccessListener {
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
                ?.addOnFailureListener { e ->
                    // 삭제 실패 시 동작
                    Log.e("Firestore", "Error deleting document", e)
                }
            navController.navigate(Screen.Login.route)
        }.addOnFailureListener {
            Toast.makeText(this, "로그아웃 실패", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchLocation() {
        val task: Task<Location> = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED

        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                Toast.makeText(
                    applicationContext,
                    "${it.latitude} ${it.longitude},",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }


}
