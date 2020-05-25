//socket연결(1번째 페이지)
package com.example.daram;

        import android.content.Intent;
        import android.os.Bundle;

        import androidx.appcompat.app.AppCompatActivity;

public class socket_ extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

        finish();
    }
}
