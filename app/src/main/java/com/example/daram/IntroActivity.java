package com.example.daram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.PrintWriter;


public class IntroActivity extends AppCompatActivity {
    private String html = "";
    private Handler mHandler;

//    private Socket socket;

    private BufferedReader networkReader;
    private PrintWriter networkWriter;

//    private DataOutputStream dos;
//    private DataInputStream dis;
//
//    private String ip = "172.16.24.152";            // IP 번호
//    private int port = 9999;                          // port 번호

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout); //xml , java 소스 연결
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
//                connect();
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent); //다음화면으로 넘어감
                finish();
            }
        },3000); //3초 뒤에 Runner객체 실행하도록
       //소켓연결
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

//    // 로그인 정보 db에 넣어주고 연결시켜야 함. connect_btn누르면 실행되는 함수
//    void connect() {
//        mHandler = new Handler();
//        Log.w("connect", "연결 하는중");
//        // 받아오는거
//        Thread checkUpdate = new Thread() {
//            public void run() {
//                // ip받기
//                String newip ="172.16.24.152"; //ip값
//                // 서버 접속
//                try {
//                    socket = new Socket(newip, port);  //연결됨.
//                    Log.w("서버 접속됨", "서버 접속됨");
//                } catch (IOException e1) {
//                    Log.w("서버접속못함", "서버접속못함");
//                    e1.printStackTrace();
//                }
//
//                Log.w("edit 넘어가야 할 값 : ", "안드로이드에서 서버로 연결요청");
//
//                // Buffered가 잘못된듯.
//                try {
//                    dos = new DataOutputStream(socket.getOutputStream());   // output에 보낼꺼 넣음
//                    dis = new DataInputStream(socket.getInputStream());     // input에 받을꺼 넣어짐
//                    dos.writeUTF("안드로이드에서 서버로 연결요청");  //str가 쥬피터에서 출력됨.
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.w("버퍼", "버퍼생성 잘못됨");
//                }
//                Log.w("버퍼", "버퍼생성 잘됨");
//
//                while (true) {
//                    // 서버에서 받아옴
//                    try {
//                        String line = "";
//                        int line2;
//                        while (true) {
//                            //line = (String) dis.readUTF();
//                            line2 = (int) dis.read();
//
//                            if (line2 > 0) {
//                                Log.w("------서버에서 받아온 값 ", "" + line2); //내 log에 찍힘.
//                                dos.writeUTF("하나 받았습니다. : " + line2);  //주피터로 보내짐.
//                                dos.flush();
//                            }
//                            if (line2 == 99) {
//                                Log.w("------서버에서 받아온 값 ", "" + line2);
//                                socket.close();
//                                break;
//                            }
//                        }
//                    } catch (Exception e) {
//
//                    }
//                }
//
//            }
//        };
//        // 소켓 접속 시도, 버퍼생성
//        checkUpdate.start();
//    }
}
