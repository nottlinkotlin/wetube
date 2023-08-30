package com.example.intelteamproject.compose

import android.net.Uri
import android.view.MotionEvent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.mlkit.vision.digitalink.Ink

@Composable
fun SignatureScreen(navController: NavController){
    Column {
        selectImage()
        Signature()

    }

}

@Composable
fun selectImage() {
    var selectUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectUri = uri
        }
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(data = selectUri),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 20.dp)
                .width(700.dp)
                .height(500.dp)
        )
        Button(
            onClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC91D11),
                contentColor = Color.White
            ),

            modifier = Modifier
                .width(120.dp)
                .height(50.dp)
                .padding(top = 12.dp),

            ) {
            Text(
                "서류 등록",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Signature() {

    var changeFlag by remember { mutableStateOf(0) }
    val inkBuilder = remember(changeFlag) { Ink.builder() }
    var currentStroke: Ink.Stroke.Builder? by remember { mutableStateOf(null) }
    var isDraw by remember { mutableStateOf(false) }
    val ink = remember(isDraw, changeFlag) { inkBuilder.build() }
    Column {
        Button(onClick = {
            changeFlag++
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC91D11),
                contentColor = Color.White
            ),
        ) {
            Text(text = "clear")
        }
        Column(
            modifier = Modifier
                .pointerInteropFilter { event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            isDraw = true
                            currentStroke = Ink.Stroke.builder()
                            currentStroke?.addPoint(
                                Ink.Point.create(
                                    event.x,
                                    event.y,
                                    System.currentTimeMillis()
                                )
                            )
                        }

                        MotionEvent.ACTION_MOVE -> currentStroke?.addPoint(
                            Ink.Point.create(
                                event.x,
                                event.y,
                                System.currentTimeMillis()
                            )
                        )

                        MotionEvent.ACTION_UP -> {
                            currentStroke?.addPoint(
                                Ink.Point.create(
                                    event.x,
                                    event.y,
                                    System.currentTimeMillis()
                                )
                            )
                            currentStroke?.let {
                                inkBuilder.addStroke(it.build())
                            }
                            isDraw = false
                        }
                    }
                    true
                }
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
//            val ink = inkBuilder.build()
                for (stroke in ink.strokes) {
                    drawPath(
                        path = Path().apply {
                            stroke.points.forEachIndexed { index, point ->
                                if (index == 0) moveTo(point.x.toFloat(), point.y.toFloat())
                                else lineTo(point.x.toFloat(), point.y.toFloat())
                            }
                        },
                        color = androidx.compose.ui.graphics.Color.Black,
                        alpha = 0.8f,
                        style = Stroke(width = 5.dp.toPx())
                    )
                }
            }

        }
    }

}