package com.example.intelteamproject.compose

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

class QrCodeAnalyzer(
    private val onQrCodeScanned:(String)->Unit
    //이미지 분석 후 QR코드를 스캐하고 그 결과를 처리함
): ImageAnalysis.Analyzer {

    //리스트에 이미지 분석에 사용할 수 있는 이미지 포맷들이 정의됨
    private val supportedImageFormats=listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,

        )
    override fun analyze(image: ImageProxy) {

        //현재 이미지 포맷이 지원되는 포맷 중 하나인지 확인함
        if(image.format in supportedImageFormats){
            //이미지의 첫 번째 평면을 바이트 배열로 변환해주고 변환 데이터를 QR코드 분석에 활용함
            val bytes=image.planes.first().buffer.toByteArray()

            val source= PlanarYUVLuminanceSource(
                bytes,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false

            )
            //이미지를 이진화 해주고 비트맵으로 변환
            val binaryBmp= BinaryBitmap(HybridBinarizer(source))
            try {
                //MultiFormatReader를 생성해주고 스캔할 바코드 형식 설정
                val result = MultiFormatReader().apply {
                    setHints(
                        mapOf(
                            DecodeHintType.POSSIBLE_FORMATS to arrayListOf(
                                //큐알만 가능하도록 해줌
                                BarcodeFormat.QR_CODE
                            )

                        )
                    )
                    //decode 메서드를 호출,BinaryBitmap을 스캔, QR 코드를 디코드, 디코드한 결과를 result에 저장
                }.decode(binaryBmp)
                onQrCodeScanned(result.text)
            }catch (e:Exception){
                e.printStackTrace()

            }finally {
                image.close()
            }
        }

    }
    //ByteBuffer를 바이트 배열로 변환하는 기능 제공
    private fun ByteBuffer.toByteArray():ByteArray{
        //buffer위치 초기화해줌
        rewind()
        return ByteArray(remaining()).also{
            get(it)
        }
    }
}