package com.example.daram;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventActivity extends AppCompatActivity implements View.OnTouchListener{

    //좌표를 출력할 텍스트뷰
    private TextView mTvCoord;
    //터치 위에 원이 그려지는 사용자 정의 뷰
    private TipsView mTipsView;

    //터치 이벤트의 좌표를 받아올 변수
    private float x = -1, y = -1;
    //최상단 RelativeLayout
    private RelativeLayout RL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //xml에 정의한 뷰들을 불러옴
        RL = (RelativeLayout)findViewById(R.id.RL);
        mTvCoord = (TextView)findViewById(R.id.tvCoord);
        mTipsView = (TipsView)findViewById(R.id.tipsView);

        //텍스트뷰에 텍스트 삽입
        mTvCoord.setText("Touch!!!");
        //Tips뷰를 터치 클릭 리스너 등록
        mTipsView.setOnTouchListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            //Down이 발생한 경우
            case MotionEvent.ACTION_DOWN :
                x = event.getX();
                y = event.getY();

                //내가 누른 자리의 좌표를 String 값으로 표현할 변수
                String str;
                str = "Coordinate : ( " + (int)x + ", " + (int)y + " )";
                mTvCoord.setText(str);
                //터치 한 곳에 이미지를 표현하기 위해 동적으로 ImageView 생성
//                ImageView img = new ImageView(this);
//                img.setImageResource(R.drawable.ic_squirrel);

                //이미지가 저장될 곳의 x,y좌표를 표현
//                img.setX(x-40);
//                img.setY(y - 90);
                //최상단 릴레이티브 레이아웃에 이미지를 Add
//                RL.addView(img);
                break;
            //Up이 발생한 경우
            case MotionEvent.ACTION_UP :
                mTvCoord.setText("Touch !!!");
                break;

        }
        //false를 반환하여 뷰 내에 재정의한 onTouchEvent 메소드로 이벤트를 전달한다
        return false;
    }
}