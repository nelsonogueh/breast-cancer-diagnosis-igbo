package com.dulcepoint.cancerdiagnosis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_screen);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(5000);  // wait for two seconds before you do the following things
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }; // ends the Thread

        // calling the thread
        myThread.start();

    }
}
