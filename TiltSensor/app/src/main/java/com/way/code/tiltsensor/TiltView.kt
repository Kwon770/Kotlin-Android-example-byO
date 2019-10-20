package com.way.code.tiltsensor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorEvent
import android.view.View

// 커스텀 뷰 작성
// 1. View 클래스를 상속받는 새로운 클래스를 작성
// 2. 필요한 메서드를 오버라이드

class TiltView(context: Context?) : View(context) {

    private val greenPaint : Paint = Paint()
    private val blackPaint : Paint = Paint()

    private var cX = 0f
    private var cY = 0f

    private var xCoord = 0f
    private var yCoord = 0f

    init {
        greenPaint.color = Color.GREEN
        blackPaint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cX = w / 2f
        cY = h / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCircle(cX, cY, 100f, blackPaint)
        canvas?.drawCircle(xCoord + cX, yCoord + cY, 100f, greenPaint)
        canvas?.drawLine(cX - 20, cY, cX + 20, cY, blackPaint)
        canvas?.drawLine(cX, cY - 20, cX, cY + 20, blackPaint)
    }

    fun onSensorEvent(event: SensorEvent) { // 이 메서드는 SensorEvent값을 인자로 받음
        // 화면을 가로로 돌렸으므로 반전
        yCoord = event.values[0] * 20
        xCoord = event.values[1] * 20

        invalidate() // invalidate() 메소드는 뷰의 onDraw() 메서드를 다시 호출하는 메서드
    }
}