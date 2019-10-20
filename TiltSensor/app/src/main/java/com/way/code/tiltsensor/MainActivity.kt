package com.way.code.tiltsensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var tiltView: TiltView // TiltView의 늦은 초기화 선언

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // values[0] x축 값 : 위로 기울이면 -10~0, 아래로 기울이면 0~10
        // values[1] y축 값 : 왼쪽으로 기울이면 -10~0, 오른쪽으로 기울이면 0~10
        // values[2] z축 값 : 미사용
        event?.let{
            Log.d("MainActivity", "onSensorChanged: x :" + "${event.values[0]}, y : ${event.values [1]}, z : ${event.values[2]}")

            tiltView.onSensorEvent(event)
        }
    }

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE // super 클래스 생정자 호출 전에 화면 가로모드 고정
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // 화면 꺼지지 않게 하기
        super.onCreate(savedInstanceState)

        tiltView = TiltView(this) // onCreate() 메서드에서 생성자에 this 넘겨서 TiltView 초기화
        setContentView(tiltView) // 기존의 R.layout.activity_main 대신 tiltView를 setContentView() 메서드에 전달. tileView가 전체 레이아웃임
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, // registerListener() 메소드로 사용할 센서 등록, 첫 인자는 센서값을 받을 SensorEventListener (this = 현재 액티비티)
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), // getDefaultSensor() 메소드로 사용할 센서 종류 지정
            SensorManager.SENSOR_DELAY_NORMAL) // 딜레이
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this) // unregisterListener() 메서드로 센서 해제, 인자는 SensorEventListener 객체 지정 (MainActivity 클래스가 이 객체 구현중이므로 this)
    }
}
