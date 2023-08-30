package com.example.intelteamproject.compose

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.intelteamproject.Screen
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



@Composable
fun ManageScreen(navController: NavController, fetchLocation: () -> Unit) {




    val authManager = FirebaseAuthenticationManager()
    val currentUser = authManager.getCurrentUser()
    val uid = currentUser?.uid

    val db = Firebase.firestore
    val uidDocumentRef = uid?.let { db.collection("users").document(it) }

    var qrCode = ""
    val currentTime = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("M월 d일 a h시 m분"))

    LaunchedEffect(Unit) {
        val uidDocument = uidDocumentRef?.get()?.await()
        val qr = uidDocument?.getString("qrCode")

        if (qr != null) {
            qrCode = qr
        }
    }

    //QR코드 스캔 결과 저장해줌
    var code by remember {
        mutableStateOf("")
    }
    //현재 context 가져옴
    val context = LocalContext.current
    //현재 라이프사이클 오너 가져옴
    val lifecycleOwner = LocalLifecycleOwner.current
    //카메라 프로바이더의 미래 인스턴스를 가져옴
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)

    }
    //카메라 권한 여부
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    //카메라 권한을 요청 하는 런처를 생성해줌
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            //권한 요청 결과가 저장됨
            hasCamPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }


    //gps
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasLocationPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            if (hasCamPermission) {
                AndroidView(
                    factory = { context ->
                        //카메라 미리 보기 화면 표시해줌
                        val previewView = PreviewView(context)
                        val preview = Preview.Builder().build()

                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(
                                Size(
                                    previewView.width,
                                    previewView.height
                                )
                            )
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QrCodeAnalyzer { result ->
                                code = result
                                if (result == qrCode) {
                                    fetchLocation()
                                    val updates = hashMapOf(
                                        "checked" to true,
                                        "checkedTime" to currentTime,

                                    )



                                    uidDocumentRef?.update(updates as Map<String, Any>)
                                        ?.addOnSuccessListener {
                                            // 업데이트 성공 시 처리
                                            Toast.makeText(
                                                context,
                                                "${currentTime} 출근 완료 되었습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack()
                                            navController.navigate(Screen.Main.route)
                                        }
                                        ?.addOnFailureListener { e ->
                                            // 업데이트 실패 시 처리
                                            Toast.makeText(
                                                context,
                                                "출근 실패 하였습니다.",
                                                Toast.LENGTH_SHORT


                                            ).show()

                                        }
                                }
                            }
                        )
                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                imageAnalysis

                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        previewView
                    },
                )

                Spacer(modifier = Modifier.padding(36.dp))



            }

        }


    }


}





