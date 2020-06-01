package com.example.daram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView img_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        img_view = (ImageView)findViewById(R.id.img_view);

        Button sendImageButton = (Button)findViewById(R.id.send_image_button);
        sendImageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            img_view.setImageURI(selectedImageUri);
//            Log.d("ing", "sendPicture부른다~");
            Cursor c = getContentResolver().query(Uri.parse(selectedImageUri.toString()), null,null,null,null);
            c.moveToNext();
            String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
            Log.d("절대경로",absolutePath);
//            byte a[]=bitmapToByteArray(bitmap);
//            Log.d("이미지데이터", String.valueOf(a.length));
        }

    }


    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }

//    public void read_image(String absolutePath)
//    {
//
//    }

//    private void sendPicture(Uri imgUri) {
//
//        String imagePath = getRealPathFromURI(imgUri);
//        Log.e("image_path", imagePath);
//
//        File file = new File(imagePath);
////        sendMessage("Start"+file.length());
//
//
//        try
//        {
//            FileInputStream fis = new FileInputStream(imagePath);
//
//            byte[] buffer = new byte[4096];
//
//            int totalSize = fis.available();
//            int readSize = 0;
//            while ( (readSize=fis.read(buffer)) > 0) {
////                System.out.println("data : " + buffer) ;
//                Log.d("파일쓰는중", fis.toString());
////                mConnectedTask.mOutputStream.write(buffer, 0, readSize);
//            }
//
//            fis.close();
//            ;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
////        mConversationArrayAdapter.insert("Me:  " + "Send Image" , 0);
//
//
//    }
//
//    private String getRealPathFromURI(Uri contentUri) {
//
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        cursor.moveToFirst();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//        return cursor.getString(column_index);
//    }

}
