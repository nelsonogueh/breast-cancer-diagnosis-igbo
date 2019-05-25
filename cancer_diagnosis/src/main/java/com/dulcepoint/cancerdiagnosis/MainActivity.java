package com.dulcepoint.cancerdiagnosis;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    public static String youtubeURL = "https://www.youtube.com/results?search_query=breast+cancer";
    private MyComponent myComponent;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("MySQLITEDB", MODE_PRIVATE, null);
        myComponent = new MyComponent(getApplicationContext());
        myComponent.createAllTables(db);

        CardView diagnoseCV, qrCodeCVCV, historyCV, enlightenmentCV, vidoeTutorialCV;

        diagnoseCV = findViewById(R.id.diagnoseCV);
        qrCodeCVCV = findViewById(R.id.qrCodeCVCV);
        historyCV = findViewById(R.id.historyCV);
        enlightenmentCV = findViewById(R.id.enlightenmentCV);
        vidoeTutorialCV = findViewById(R.id.videoTutorialCV);

        diagnoseCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Diagnose.class);
                startActivity(intent);
            }
        });
        qrCodeCVCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QRScan.class);
                startActivity(intent);
            }
        });
        historyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),History.class);
                startActivity(intent);
            }
        });

        enlightenmentCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Enlightenment.class);
                startActivity(intent);
            }
        });

        vidoeTutorialCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VideoTutorial.class);
                startActivity(intent);
            }
        });

    }
}
