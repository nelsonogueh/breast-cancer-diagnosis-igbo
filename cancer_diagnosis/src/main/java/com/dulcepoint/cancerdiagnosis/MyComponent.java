package com.dulcepoint.cancerdiagnosis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nelson on 9/24/2018.
 */

public class MyComponent extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MySQLITEDB.db";
    private Context context;


    public MyComponent(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }


    public boolean createAllTables(SQLiteDatabase db) {
        createTable_test_record(db);
        createTable_symptoms(db);

        insertAllToSymptomsTable(db); // Insert values to the database
        return true;
    }

    public void createTable_test_record(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS test_record (id INTEGER PRIMARY KEY AUTOINCREMENT, patient_name VARCHAR, symptom_description TEXT, test_reference_code VARCHAR, taken_date VARCHAR, taken_time VARCHAR)");
        Log.d("DB", "TEST RECORD TABLE CREATED");
    }

    public void createTable_symptoms(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS symptoms (symptom_id INTEGER PRIMARY KEY AUTOINCREMENT, symptom_description VARCHAR, symptom_image VARCHAR)");
        Log.d("DB", "SYMPTOMS TABLE CREATED");
    }


    public void insertAllToSymptomsTable(SQLiteDatabase db) {
        try {
            db.execSQL("INSERT INTO symptoms (symptom_description) VALUES('Changes in appearance or nipple direction')");
            db.execSQL("INSERT INTO symptoms (symptom_description) VALUES('Colour changes')");
            db.execSQL("INSERT INTO symptoms (symptom_description) VALUES('Lump in breast')");
            db.execSQL("INSERT INTO symptoms (symptom_description) VALUES('Nipple discharge')");
            db.execSQL("INSERT INTO symptoms (symptom_description) VALUES('Rash or crusting in breast')");
            db.execSQL("INSERT INTO symptoms (symptom_description) VALUES('Changes in size or shape')");
            db.execSQL("INSERT INTO symptoms (symptom_description) VALUES('Changes in skin texture')");
            Log.d("DB RESPONSE", "inserted into db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E dd  - MM - yyyy");
        String finalDate = dateFormat.format(date);
        return finalDate;
    }

    public String getTime() {
        Date date = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh : mm a");
        String finalDate = timeFormat.format(date);
        return finalDate;
    }

    public static String randomNumber() {
        Date date = new Date();
        long randomNum = date.getTime();
        return randomNum + "";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addToTestTable(SQLiteDatabase db, String referenceCode, String checkBoxText) {
        try {
            db.execSQL("INSERT INTO test_record (test_reference_code, symptom_description, taken_date, taken_time) VALUES('" + referenceCode + "', '" + checkBoxText + "', '" + getDate() + "','" + getTime() + "')");
            Log.d("DB RESPONSE", "Added to db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteToTestTable(SQLiteDatabase db, String referenceCode, String checkBoxText) {
        try {
            db.execSQL("DELETE FROM test_record WHERE (test_reference_code = '" + referenceCode + "' AND symptom_description='" + checkBoxText + "')");
            Log.d("DB RESPONSE", "Removed from db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNameInTestDB(SQLiteDatabase db, String referenceCode, String patientName) {
        try {
            db.execSQL("UPDATE test_record SET patient_name='" + patientName + "' WHERE (test_reference_code = '" + referenceCode + "')");
            Log.d("DB RESPONSE", "Patient name updated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Bitmap returnQRCodeAsBitmap(String textToConvert) {
        Bitmap bitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textToConvert, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
