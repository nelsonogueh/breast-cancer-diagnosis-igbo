package com.dulcepoint.cancerdiagnosis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    String referenceCode = null;
    MyComponent myComponent = null;
    private SQLiteDatabase db;
    boolean breast_cancer_suspected = false;
    private ArrayList<String> breastCancerSymptomAL;

    String patientNameString = null;
    String responseQRCodeString = null;

    public boolean isPlaying = false;


    private ListView selectedSymptomsLV;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        myComponent = new MyComponent(getApplicationContext());
        selectedSymptomsLV = findViewById(R.id.listSelectedSymptoms);

        referenceCode = getIntent().getStringExtra("reference_code");
        breastCancerSymptomAL = new ArrayList<String>();

//        mPlayer = MediaPlayer.create(ResultActivity.this,R.raw.zxing_beep);
        mPlayer = MediaPlayer.create(ResultActivity.this, R.raw.alarm_mixdown);

//        Toast.makeText(this, referenceCode, Toast.LENGTH_SHORT).show();
        getThisSelectionToArrayList(referenceCode); // Getting values from the database based on the passed referenceCode

        ResultAdapter resultAdapter = new ResultAdapter(getApplicationContext(), breastCancerSymptomAL);
        selectedSymptomsLV.setAdapter(resultAdapter);

        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        assert vibrator != null;

        if (breast_cancer_suspected) { // if Breast cancer is suspected
            responseQRCodeString = "Breast Cancer Suspected!";

            mPlayer = MediaPlayer.create(ResultActivity.this, R.raw.breast_cancer_suspected);
            if (!isPlaying) {
                mPlayer.start();
                isPlaying = true;
                vibrator.vibrate(1000);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        isPlaying = false;
                    }
                }, 8000);


            } else {
                mPlayer.stop();
                mPlayer.release();
            }

            breast_cancer_suspected = false;
        } else {
            responseQRCodeString = "Breast Cancer Not Suspected!";


            mPlayer = MediaPlayer.create(ResultActivity.this, R.raw.breast_cancer_not_detected);
            if (!isPlaying) {
                mPlayer.start();
                isPlaying = true;
                vibrator.vibrate(1000);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        isPlaying = false;
                    }
                }, 8000);

            } else {
                mPlayer.stop();
                mPlayer.release();
            }
        }
        ImageView imageView = findViewById(R.id.qrCodeIV);
        imageView.setImageBitmap(myComponent.returnQRCodeAsBitmap(responseQRCodeString));

    }

    public void getThisSelectionToArrayList(String referenceCodeStr) {
        // Get the questions relating to that level from the database
        db = openOrCreateDatabase("MySQLITEDB", MODE_PRIVATE, null);
        Cursor c, cursor2;
        try {
            c = db.rawQuery("SELECT * FROM test_record WHERE test_reference_code='" + referenceCodeStr + "'", null);
            String thisSymptom;

            while (c.moveToNext()) { // We select the session Id first, and use it to select the question
                thisSymptom = c.getString(c.getColumnIndex("symptom_description"));

                cursor2 = db.rawQuery("SELECT * FROM symptoms WHERE symptom_description='" + thisSymptom + "'", null);
                if (cursor2.moveToFirst()) {
                    breast_cancer_suspected = true;
                }
                cursor2.close();

                breastCancerSymptomAL.add(c.getString(c.getColumnIndex("symptom_description")));
                patientNameString = c.getString(c.getColumnIndex("patient_name"));

                Log.d("SYMPTOM", c.getString(c.getColumnIndex("symptom_description")));
            }
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }


}


class ResultAdapter extends ArrayAdapter {
    private ArrayList symptomAL;

    public ResultAdapter(Context context, ArrayList symptom_description) {
        //Overriding Default Constructor off ArrayAdapter
        super(context, R.layout.single_view_row, R.id.symptomTV, symptom_description);
        this.symptomAL = symptom_description;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflating the layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_view_row, parent, false);

        TextView symptomTV = row.findViewById(R.id.symptomTV);

        symptomTV.setText(symptomAL.get(position) + "");

        return row;
    }
}
