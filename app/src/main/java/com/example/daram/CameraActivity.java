package com.example.daram;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class CameraActivity extends Activity {
    Button camera_btn;
    ImageView img_view;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camera_btn = (Button)findViewById(R.id.camera_btn);
        img_view = (ImageView)findViewById(R.id.img_view);

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
                ActivityCompat.requestPermissions(CameraActivity.this
                        , new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , 1);
            }
        }

        // 카메라 버튼 클릭시 - startActivityForResult 끝난 후 onActivityResult 실행됨.
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }

    // 사진 고르고 나서 실행되는 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        img_view.setImageBitmap(bitmap);
    }

    // 권한 요청 결과 받는 함수
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