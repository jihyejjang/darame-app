package com.example.daram;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
    private Button send_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);



        Intent intent = getIntent();
        Uri receiveUri= intent.getParcelableExtra("image");

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

        send_btn=(Button)findViewById(R.id.newActivity3);



        send_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Thread th = new Thread((Runnable) MosaicActivity.this);
                th.start();
            }
        });
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MosaicActivity.this, "다전송함", Toast.LENGTH_SHORT).show();
        }
    };

    String lineEnd = "\r\n";    // 통신할때의 데이터 개행문자
    String twoHyphens = "--";
    String boundary = "*****";

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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
    public void run() {
        StringBuffer sb = new StringBuffer();
        Log.d("버튼 눌림", "버튼 눌림");
        try {
            String urlString = "http://172.16.26.172:9999/notebooks/Untitled"; //자신의 ip주소와 포트
            String params = "";
            Intent intent = getIntent();
            Uri receiveUri= intent.getParcelableExtra("image");
            String fileName = getPathFromUri(receiveUri) +"/"+ getFileName(receiveUri);
            //String fileName = "/data/data/com.example.servertest3/files/test.txt";
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
