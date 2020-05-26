package com.example.daram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

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
                Intent intent3=new Intent(getApplicationContext(),MosaicActivity.class);
                startActivity(intent3);
            }
        });
    }
}
