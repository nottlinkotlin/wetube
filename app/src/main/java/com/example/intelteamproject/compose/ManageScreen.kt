package com.example.intelteamproject.compose

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.intelteamproject.database.FirebaseAuthenticationManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun ManageScreen(navController: NavController, fetchLocation: () -> Unit) {

    val authManager = FirebaseAuthenticationManager()
    val currentUser = authManager.getCurrentUser()
    val uid = currentUser?.uid

    val db = Firebase.firestore
    val uidDocument = uid?.let { db.collection("users").document(it) }

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
    //카메라 권한을 요청하는 런처를 생성해줌
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



    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            if (hasCamPermission) {
                AndroidView(
                    factory = { context ->
                        //카메라 미리보기 화면 표시해줌
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
                                if (result == "em3j5h6fk44b5") {
//                                    uidDocument.("출근")
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
                Text(
                    text = code,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                )

                Spacer(modifier = Modifier.padding(36.dp))

                Button(onClick = { fetchLocation() }) {
                    Text(text = "위치")

                }


            }

        }


    }


}




