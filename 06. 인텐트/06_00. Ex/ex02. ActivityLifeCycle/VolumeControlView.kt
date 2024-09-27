package com.frost.ex02activitylifecycle

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.PI
import kotlin.math.atan2

class VolumeControlView(context: Context, attrs: AttributeSet?) :
    AppCompatImageView(context, attrs) {
    var mx = 0.0f
    var my = 0.0f//중심에서의 떨어진 좌표
    var tx = 0.0f
    var ty = 0.0f//터치한 좌표
    var angle = 180.0f

    var listener:VolumeListener?=null

    interface VolumeListener{//클래스간의 독립성을 유지하기 위해서 인터페이스로 접근하도록 처리
        fun onChanged(angle:Float):Unit
        //이 인터페이스 구현하는 곳에서 onChanged() 메서드를 정의해서 사용가능
    }
    fun setVolumeListener(listener:VolumeListener){//멤버 초기화
        this.listener = listener
    }

    fun getAngle(x1:Float, y1:Float):Float{//각도 계산
        mx = x1-(width/2.0f)
        my = (height/2.0f)-y1
        return (atan2(mx, my)*180.0f/PI).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {//터치 이벤트처리
        if(event!=null){
            tx = event.getX(0)//여러 곳 터치 가능해서 첫 번째 터치한 x좌표 대입
            ty = event.getY(0)
            angle = getAngle(tx, ty)
            invalidate()//onDraw()를 다시 호출해 다시 화면 그리기
            if(listener!=null){
                listener?.onChanged(angle)
                //해당 리스너는 onChanged() 메서드를 호출
            }
            return true
        }
        return false
        
    }

    override fun onDraw(canvas: Canvas?) {//화면 그리기
        canvas?.rotate(angle, width/2.0f, height/2.0f)//가운데에서
        super.onDraw(canvas)

    }
}