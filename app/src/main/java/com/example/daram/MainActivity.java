package com.example.daram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;
    private boolean num;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1) ;
        imageView1.setImageResource(R.drawable.darame) ;

        // 체크해야할 퍼미션
        int cameraPermission = checkSelfPermission(Manifest.permission.CAMERA);    // PERMISSION_GRANTED 여야 권한설정된것.
        int writeExternalStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // 어플 실행시 먼저 체크하고 실행됨.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 퍼미션 체크가 PERMISSION_GRANTED 여야 권한설정이 완료된 것.
            if (cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                Log.d("권한", "권한 설정 완료");
            } else {
                Log.d("권한", "권한 설정 요청");
                ActivityCompat.requestPermissions(MainActivity.this
                        , new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , 1);
            }
        }
        Button camera_btn=(Button)findViewById(R.id.newActivity);  //카메라
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                num=false;
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        Button button2=(Button)findViewById(R.id.newActivity2); //합성
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent2=new Intent(getApplicationContext(),Send_img.class); //사진전송class로 연동
                startActivity(intent2);
            }
        });

        Button button3=(Button)findViewById(R.id.newActivity3);  //모자이크
        button3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                num=true;
                Intent intent3 = new Intent(Intent.ACTION_PICK);
                intent3.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent3, GET_GALLERY_IMAGE);
            }

        });

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  //사진전송
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(num==true)//모자이크
        {
            if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                Intent intent4 = new Intent(getApplicationContext(), MosaicActivity.class);
                intent4.putExtra("image",selectedImageUri);
                startActivity(intent4);
            }
        }
        else if(num==false) //카메라촬영한거
        {
            super.onActivityResult(requestCode, resultCode, data);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Intent intent_camera=new Intent(getApplicationContext(),CameraActivity.class);
            intent_camera.putExtra("camera",bitmap);
            startActivity(intent_camera);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("권한요청결과", "결과");
        // 권한요청 결과 출력.
        for(int i=0; i<grantResults.length; i++){
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                Log.d("권한받음", "Permission: " + grantResults[i]);
            }
            else
                Log.d("권한못받음", "Permission: " + grantResults[i]);
        }
    }

}