//socket연결(1번째 페이지)
package com.example.daram;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;

        import androidx.appcompat.app.AppCompatActivity;

public class socket_ extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_);

        Intent intent=getIntent();
        Log.d("ACTIVITY_LC",intent.getStringExtra("message")); //로그로 찍어주기
    }

}
