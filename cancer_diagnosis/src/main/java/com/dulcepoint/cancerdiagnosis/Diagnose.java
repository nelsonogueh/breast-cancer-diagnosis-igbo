package com.dulcepoint.cancerdiagnosis;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.dulcepoint.cancerdiagnosis.MyComponent.randomNumber;

public class Diagnose extends AppCompatActivity {

    Button submitBTN;
    ArrayList symptomsNameArrayList, symptomImageArrayList;

    MyComponent myComponent;
    String referenceCode = randomNumber();

    EditText patientNameEt;
    String patientName = null;
    private SQLiteDatabase db;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnose_activity);
        myComponent = new MyComponent(getApplicationContext());

        db = openOrCreateDatabase("MySQLITEDB", MODE_PRIVATE, null);


        // Patient's name Edittext
        patientNameEt = findViewById(R.id.patientNameET);
        symptomsNameArrayList = new ArrayList();

        submitBTN = findViewById(R.id.testSubmitBTN);
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((patientNameEt.getText().toString()).isEmpty()){
                    Toast.makeText(Diagnose.this, "Please enter patient\'s name", Toast.LENGTH_SHORT).show();
                    return;
                }
                myComponent.updateNameInTestDB(db, referenceCode, getPatientName());
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("reference_code", referenceCode);
                startActivity(intent);
                finish();

            }
        });

    }

    public void selectedCheckBox(View view) {

        final CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) { // Checkbox checked
            myComponent.addToTestTable(db, referenceCode, checkBox.getText().toString());

        } else { // Checkbox unchecked
            myComponent.deleteToTestTable(db, referenceCode, checkBox.getText().toString());
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) { // Checkbox checked
                    myComponent.addToTestTable(db, referenceCode, checkBox.getText().toString());

                } else { // Checkbox unchecked
                    myComponent.deleteToTestTable(db, referenceCode, checkBox.getText().toString());
                }
            }
        });

    }

    public String getPatientName() {
        if (patientNameEt.getText().toString().equalsIgnoreCase("")) {
            patientName = "Patient";
        } else {
            patientName = patientNameEt.getText().toString();
        }

        return patientName;
    }


    public void getAllSymptomsToArrayList() {
        // Get the questions relating to that level from the database
        db = openOrCreateDatabase("MySQLITEDB", MODE_PRIVATE, null);
        Cursor c;
        try {
            // Select 20 records from the db
            c = db.rawQuery("SELECT * FROM symptoms ORDER BY random()", null);

            while (c.moveToNext()) { // We select the session Id first, and use it to select the question
                symptomsNameArrayList.add(c.getString(c.getColumnIndex("symptom_description")));
            }
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

}

// The class for the custom adapter
class SymptomAdapter extends ArrayAdapter {
    private ArrayList symptom_description;

    public SymptomAdapter(Context context, ArrayList symptom_description) {
        //Overriding Default Constructor off ArrayAdapter
        super(context, R.layout.symptoms_row, R.id.checkboxSymptomCB, symptom_description);
        this.symptom_description = symptom_description;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflating the layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.symptoms_row, parent, false);

        CheckBox symptomCheckBox = row.findViewById(R.id.checkboxSymptomCB);

        symptomCheckBox.setText(symptom_description.get(position) + "");

        return row;
    }
}

