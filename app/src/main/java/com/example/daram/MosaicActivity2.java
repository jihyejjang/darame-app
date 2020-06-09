package com.example.daram;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daram.R;

public class MosaicActivity2 extends Activity {
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic2);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Intent intent=getIntent();
        byte[] arr=getIntent().getByteArrayExtra("image");
        Bitmap image= BitmapFactory.decodeByteArray(arr,0,arr.length);
        ImageView imageView2=(ImageView)findViewById(R.id.imageView2);
        imageView2.setImageBitmap(image);

        Log.d("화면전환","모자이크할 사진 mask하여 보여줌");

    }
}