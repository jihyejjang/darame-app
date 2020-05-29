package com.example.daram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ImageView img_view;
        img_view = (ImageView)findViewById(R.id.img_view);


        Intent intent = getIntent();
        Bitmap bitmap = intent.getParcelableExtra("camera");
        img_view.setImageBitmap(bitmap);

        Button button=(Button)findViewById(R.id.home);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent2=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent2);
            }
        });
//        Button button2=(Button)findViewById(R.id.camera_btn);
//        button2.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                Intent intent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent2, 1);
//            }
//        });
    }
}