package com.example.daram;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MosaicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);
        Intent intent = getIntent();
        Uri receiveUri= intent.getParcelableExtra("image");
        Log.d("사진 선택함", "사진 선택");
        ImageView imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView1.setImageURI(receiveUri);

        Button button=(Button)findViewById(R.id.newActivity);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        Button button2=(Button)findViewById(R.id.newActivity2);
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),SaveActivity.class);
                startActivity(intent);
            }
        });

        Button send_btn = (Button) findViewById(R.id.newActivity3);

        send_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mosaicThread th = new mosaicThread();
                Log.d("스레드 시작", "스레드 시작");
                th.start();

            }
        });
    }

    class mosaicThread extends Thread {
        String lineEnd = "\r\n";    // 통신할때의 데이터 개행문자
        String twoHyphens = "--";
        String boundary = "*****";
        @SuppressLint("HandlerLeak")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public String getPathFromUri(Uri uri) throws Exception {

            Cursor cursor = getContentResolver().query(uri, null, null, null, null );

            cursor.moveToNext();

            String path = cursor.getString( cursor.getColumnIndex( "_data" ) );

            AutoCloseable c = null;
            c.close();

            return path;

        }

        public String getFileName(Uri uri) {
            String result = null;
            if (uri.getScheme().equals("content")) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            if (result == null) {
                result = uri.getLastPathSegment();
            }
            return result;
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(MosaicActivity.this, "완료", Toast.LENGTH_SHORT).show();
            }
        };

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run()  {
            StringBuffer sb = new StringBuffer();
            Log.d("버튼 눌림", "버튼 눌림");
            try {
                Log.d("서버", "접속중");
                String urlString = "http://192.168.0.6:9999/server.py"; //.py 돌아가는 jupyter notebook 주소 입력
                String params = "";
                Intent intent = getIntent();
                Uri receiveUri= intent.getParcelableExtra("image");
                Log.d("보낼 사진", "받아옴");
                //String fileName = getPathFromUri(receiveUri) +"/"+ getFileName(receiveUri);
                Log.d("파일이름", receiveUri.getPath());
                //String fileName = "/data/data/com.example.servertest3/files/test.txt";
                URL connectUrl = new URL(urlString);
                Log.d("연결", "활성화");

                try {
                    // 파일을 바이트단위로 읽어와 보냄
                    File sourceFile = new File(receiveUri.getPath());
                    Log.d("사진", "생성");
                    DataOutputStream dos;
                    FileInputStream mFileInputStream = new FileInputStream(sourceFile);
                    Log.d("사진", "스트림에전송");
                    // 통신에 필요한 것들 먼저 설정
                    HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("new_file", String.valueOf(receiveUri));
                    Log.d("사진", "스트림에전송2");
                    // 프로토콜 규칙대로 먼저 적어주고, 데이터 전송함.
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"new_file\";filename=\"" + String.valueOf(receiveUri) + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    Log.d("사진", "스트림에전송3");
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
}
