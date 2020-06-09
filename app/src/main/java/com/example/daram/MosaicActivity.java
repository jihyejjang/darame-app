package com.example.daram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

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

        private String ip = "172.16.24.163";            // IP : .py 돌아가는 python server ip 입력 (Cmd -> ipconfig)
        private int port = 9999;                          // port 번호 : 9999고정


        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(MosaicActivity.this, "완료", Toast.LENGTH_SHORT).show();
            }
        };



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

                //dos.writeUTF("모자이크");

                dos.close();
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

