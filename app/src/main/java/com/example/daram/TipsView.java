package com.example.daram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TipsView extends View {

    //터치 할 때 화면에 표현되는 점
    private Paint mPaint = null;
    //터치 한 곳의 좌표를 저장할 변수
    private float x = -1, y = -1;

    //java Code에서 뷰를 생성 할 떄 호출되는 생성자
    public TipsView(Context context) {
        super(context);

//표현될 점을 빨간색으로 표현하기 위해 Paint객체 생성
        mPaint = new Paint();
//Paint의 경계면을 부드럽게 처리할 지 설정(boolean)
        mPaint.setAntiAlias(true);
//Paint의 색 설정
        mPaint.setColor(Color.RED);
    }

    //xml에서 뷰를 생성할 때 호출되는 생성자
    public TipsView(Context context, AttributeSet attrs) {
        super(context, attrs);

//내용은 위에 동일
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
    }

    //뷰에 그림 그리는 행위를 담당하는 메소드
    protected void onDraw(Canvas canvas) {
//뷰의 배경색을 흰색으로 칠한다
        canvas.drawColor(Color.WHITE);

//터치 행위가 발생한 경우 해당 위치에 원을 그린다
        if (x > 0 && y > 0) {
// (x - 5, y - 5)를 시작으로 지름이 10인 원을 그린다.
            canvas.drawCircle(x - 5, y - 5, 10, mPaint);
        }
    }

    //터치 이벤트를 처리하는 콜백 메소드
    public boolean onTouchEvent(MotionEvent event) {
//상위 클래스인 View 클래스에 발생한 이벤트를 전달
        super.onTouchEvent(event);

//어떤 이벤트가 처리했는지에 따라 처리를 달리함
        switch (event.getAction()) {
//Action Down이 발생 했을 경우
            case MotionEvent.ACTION_DOWN :
//Action Down 한 위치의 x, y좌표를 받아와 변수에 저장한다
                x = event.getX();
                y = event.getY();
//뷰를 갱신
                invalidate();
                break;
//Action Move를 설정하고 싶은 경우 이곳에 코딩
            case MotionEvent.ACTION_MOVE :
                break;

//Action Up이 발생한 경우
            case MotionEvent.ACTION_UP :
//Up이 발생한 경우 그리기 좌표에 -1을 저장하고 뷰를 갱신한다
                y = x = -1;
//뷰를 갱신
                invalidate();
                break;
        }

//true를 반환하여 더이상의 이벤트 처리가 이루어지지 않도록 완료한다
        return true;
    }
}
