package com.way.code.mygallery

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaActionSound
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity() {

    private  val REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 권한 여부 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { // 권한이 허용되지 않음

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) { // 이전에 이미 권한이 거부되었을때 설명
                alert("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다", "권한이 필요한 이유") {
                    yesButton {
                        // 권한 요청
                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    noButton {  }
                }.show()
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE)
            }
        } else { // 권한이 이미 허용됨
            getAllPhotos()
        }
    }

    private fun getAllPhotos() {
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 어떤 데이터를 가져오느냐를 URI형태로 지정 (외부 저장소 데이터 EXTERNAL_CONTENT_URI)
            null, // 어떤 항목의 데이터를 가져올지 String 배열로 지정 (모든항목 null)
            null, // 데이터를 가져올 조건을 지정 (모든 항목은 null)
            null, // 세번째 인자와 조합아혀 조건 지정 (사용하지 않으면 null)
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC") // 정렬 방법 지정 (촬영 날짜의 내림차순)

        if(cursor != null) { // 사진 정보를 담고있는 Cursor 객체는 내부적으로 데이터를 이동하는 포인터 존재. moveToNext() 메서드로 다음 정보로 이동하고 결과를 true 반환 (만약 사진이 없다면 객체는 null)
            while(cursor.moveToNext()) {
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)) // 사진 경로 Uri 가져오기
                Log.d("MainActivity", uri)                                                       // 사진의 경로가 저장된 데이터베이스의 컬럼명은 DATA 상수에 정의
            }                                                                                         // getColumnIndexOrThrow() 메서드를 사용하면 해당 컬럼이 몇 번째 인덱스인지 알 수 있음
            cursor.close() // 메모리 누수 방지
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray // 요청한 권한들의 결과 전달 (현재 하나의 권한만 요청했으므로 0번 인덱스값만 확인)
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 권한 허용
                    getAllPhotos()
                } else { // 권한 거부
                    toast("권한 거부 됨")
                }
                return
            }
        }
    }
}
