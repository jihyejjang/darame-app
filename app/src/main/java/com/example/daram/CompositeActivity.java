package com.example.daram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class CompositeActivity extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview1;
    private ImageView imageview2;
    private  boolean num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite);

        Button button=(Button)findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent_next=new Intent(getApplicationContext(),SaveActivity.class);
                startActivity(intent_next);
            }
        });

        imageview1 = (ImageView)findViewById(R.id.imageView1);
        imageview2 = (ImageView)findViewById(R.id.imageView2);

        imageview1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                num=true;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        imageview2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                num=false;
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent2, GET_GALLERY_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(num==true)
        {
            if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                imageview1.setImageURI(selectedImageUri);
            }
        }
        else {
            if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                imageview2.setImageURI(selectedImageUri);
            }

        }
    }
}