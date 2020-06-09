package com.example.daram;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.zip.CheckedOutputStream;

public class MosaicActivity extends AppCompatActivity {
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        Uri receiveUri = intent.getParcelableExtra("image");
        Log.d("사진 선택함", "사진 선택");
        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView1.setImageURI(receiveUri);

//        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
//        imageView2.setImageBitmap(bit);

        Log.d("수신" , "마스크 사진 디스플레이");




        Button button = (Button) findViewById(R.id.newActivity);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = (Button) findViewById(R.id.newActivity2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SaveActivity.class);
                startActivity(intent);
            }
        });

        Button send_btn = (Button) findViewById(R.id.newActivity3);
        send_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mosaicThread th = new mosaicThread();
                Log.d("스레드 시작", "스레드 시작");
                th.start();

            }
        });
    }

    class mosaicThread extends Thread {

        private String ip = "172.16.24.239";            // IP : .py 돌아가는 python server ip 입력 (Cmd -> ipconfig)
        private int port = 9999;                          // port 번호 : 9999고정


        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(MosaicActivity.this, "모자이크 하지 않을 얼굴을 터치하세요:)", Toast.LENGTH_SHORT).show();
            }
        };

        private Bitmap displayImg(InputStream in) throws IOException
        {
            Log.d("수신" , "사진 수신 준비");

            BufferedInputStream bis = new BufferedInputStream(in);

            Bitmap bit = BitmapFactory.decodeStream(bis);


            Log.d("수신" , "사진 받아옴");


            in.close();
            bis.close();

            return bit;

        }



        public void DoFileUpload(String absolutePath) {
//            HttpFileUpload(apiUrl, "", absolutePath);
            FileUpload(port, ip, " ", absolutePath);

        }


        public void FileUpload(int port, String ip, String params, String fileName) { //param은 서버로 전송하는 숫자인건가..? 그럼 mosaic와 composite를 구별할 수 있지 않을까?
            try {

                String twoHyphens = "-";

                Log.d("연결", "접속 시작");
                FileInputStream mFileInputStream = new FileInputStream(fileName);


                Socket socket = new Socket(ip, port);
                Log.w("서버 접속됨", "서버 접속됨");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                //dos.writeUTF("-");

                Log.d("연결", "보낼 파일 준비");
                int bytesAvailable = mFileInputStream.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                byte[] buffer = new byte[bufferSize];
                int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                Log.d("Test", "image byte is " + bytesRead);

                // read image
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = mFileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                }

                Log.d("전송" , "전송완료");

                InputStream in = socket.getInputStream();

                dos.close();

                Bitmap bit = displayImg(in);

                Intent intent = new Intent(getApplicationContext(), MosaicActivity2.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bit.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image",byteArray);
                startActivity(intent);

                socket.close();


            } catch (Exception e) {
                Log.d("Test", "exception " + e.getMessage());

            }
        }




        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            StringBuffer sb = new StringBuffer();
            Log.d("버튼 눌림", "버튼 눌림");
            try {
                Log.d("서버", "접속중");
                //String urlString = "https://172.16.26.172:9999/darame_server.ipynb"; //서버에 전송할 API URL(?)
                String params = "";
                Intent intent = getIntent();
                Uri receiveUri = intent.getParcelableExtra("image");
                Log.d("보낼 사진", "받아옴");
                //String fileName = getPathFromUri(receiveUri) +"/"+ getFileName(receiveUri);
                Log.d("파일이름", receiveUri.getPath());

                //절대경로를 획득
                Cursor c = getContentResolver().query(Uri.parse(receiveUri.toString()), null, null, null, null);
                c.moveToNext();
                String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                Log.d("절대경로", absolutePath);
                DoFileUpload(absolutePath);


            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    }
}



