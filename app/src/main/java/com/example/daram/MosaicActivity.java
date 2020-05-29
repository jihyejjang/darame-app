package com.example.daram;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MosaicActivity extends AppCompatActivity {

    //안드로이드 내 저장 파일 이름
    String folder_name;     // 폴더경로 및 이름
    String files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);

        // 버튼
        send_btn = (Button) findViewById(R.id.button2);


        // 내부저장소 내의 폴더명과 파일명
        folder_name = "/data/data/com.example.servertest3/files";
        files = folder_name + "/test.txt";

        // 버튼을 눌렀을때 thread 실행 -> Override한 run() 함수를 실행시킴. -> run()함수는 실행할 것을 한 뒤 handler 실행
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread th = new Thread((Runnable) MosaicActivity.this);
                th.start(); // run() 실행
            }
        });
    }

    // handler = 백그라운드 thread에서 전달된 메시지 처리(UI변경 등을 여기서 해줌.)
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MosaicActivity.this, "다전송함", Toast.LENGTH_SHORT).show();
        }
    };

    // 통신시 필요한 문자들
    String lineEnd = "\r\n";    // 통신할때의 데이터 개행문자
    String twoHyphens = "--";
    String boundary = "*****";

    public void run() {
        StringBuffer sb = new StringBuffer();
        Log.d("버튼 눌림", "버튼 눌림");
        try {
            String urlString = "http://192.168.55.193:8080/uploadFile";
            String params = "";
            String fileName = "/data/data/com.example.servertest3/files/test.txt";
            URL connectUrl = new URL(urlString);

            try {
                // 파일을 바이트단위로 읽어와 보냄
                File sourceFile = new File(fileName);
                DataOutputStream dos;
                FileInputStream mFileInputStream = new FileInputStream(sourceFile);

                // 통신에 필요한 것들 먼저 설정
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("new_file", fileName);

                // 프로토콜 규칙대로 먼저 적어주고, 데이터 전송함.
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"new_file\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // 파일 내의 데이터를 바이트단위로 읽어와 전송
                int bytesAvailable = mFileInputStream.available();
                int maxBufferSize = 1024 * 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = mFileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                }

                // 데이터 끝남 표시
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                mFileInputStream.close();

                dos.flush();

                if (conn.getResponseCode() == 200) {
                    Log.d("전송", "전송잘됨");
                }

                // stream 닫음
                mFileInputStream.close();
                dos.close();

            } catch (Exception e) {
                Log.d("오류", "데이터전송오류");
            }

        } catch (Exception e) {
            Log.d("오류", "오류남");
        }
        handler.sendEmptyMessage(0);    // 핸들러 불러서 UI 처리
    }
}