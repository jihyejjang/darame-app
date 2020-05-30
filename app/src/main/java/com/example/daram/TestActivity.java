package com.example.daram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        File file = new File("file.txt") ;
        FileWriter fw = null ;
        BufferedWriter bufwr = null ;

        String str = "STR" ;

        try {
            // open file.
            fw = new FileWriter(file) ;
            bufwr = new BufferedWriter(fw) ;

            // write data to the file.
            bufwr.write(str) ;

        } catch (Exception e) {
            e.printStackTrace() ;
        }

        // close file.
        try {
            if (bufwr != null)
                bufwr.close() ;

            if (fw != null)
                fw.close() ;
        } catch (Exception e) {
            e.printStackTrace();
        }




        File file2 = new File("file.txt") ;
        FileReader fr = null ;
        BufferedReader bufrd = null ;

        char ch ;

        try {
            // open file.
            fr = new FileReader(file2) ;
            bufrd = new BufferedReader(fr) ;

            // read 1 char from file.
            while ((ch = (char) bufrd.read()) != -1) {
                System.out.println("char : " + ch) ;
            }

            // close file.
            bufrd.close() ;
            fr.close() ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }

    }
}
