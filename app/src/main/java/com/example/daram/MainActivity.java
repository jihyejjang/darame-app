package com.example.daram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1) ;

        imageView1.setImageResource(R.drawable.darame) ;

        Button button=(Button)findViewById(R.id.newActivity);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),CameraActivity.class);
                startActivity(intent);
            }
        });

        Button button2=(Button)findViewById(R.id.newActivity2);
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent2=new Intent(getApplicationContext(),CompositeActivity.class);
                startActivity(intent2);
            }
        });

        Button button3=(Button)findViewById(R.id.newActivity3);
        button3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                Intent intent3=new Intent(getApplicationContext(),MosaicActivity.class);
//                startActivity(intent3);
                Intent intent3 = new Intent(Intent.ACTION_PICK);
                intent3.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent3, GET_GALLERY_IMAGE);
            }

        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
//            imageview.setImageURI(selectedImageUri);
//            imageview2.setImageURI(selectedImageUri);
            Intent intent4 = new Intent(getApplicationContext(), MosaicActivity.class);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
            intent4.putExtra("image",selectedImageUri);
            startActivity(intent4);
        }

    }

}
