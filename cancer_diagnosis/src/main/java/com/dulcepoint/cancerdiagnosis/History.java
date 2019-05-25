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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private SQLiteDatabase db;
    private ArrayList test_reference_code, patient_name, test_date, test_time, testId;
    //    MyComponent myComponent;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

//        myComponent = new MyComponent(getApplicationContext());

        // Initialize the array lists
        patient_name = new ArrayList();
        test_reference_code = new ArrayList();
        test_date = new ArrayList();
        test_time = new ArrayList();
        testId = new ArrayList();

        // Populate the array lists
        getAllHistoryToArrayList();

        // Populate the listview
        lv = findViewById(R.id.historyListView);
        HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(),patient_name, test_reference_code, test_date, test_time);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), patient_name.get(i) + "", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("reference_code", test_reference_code.get(i) + "");
                startActivity(intent);

            }
        });


        TextView noHistory = findViewById(R.id.noHistoryTV);
        if (testId.size() < 1) {
            noHistory.setVisibility(View.VISIBLE);
        } else {
            noHistory.setVisibility(View.GONE);
        }


    }


    public void getAllHistoryToArrayList() {
        // Get the questions relating to that level from the database
        db = openOrCreateDatabase("MySQLITEDB", MODE_PRIVATE, null);
        Cursor c;
        try {
            // Select 20 records from the db
            c = db.rawQuery("SELECT * FROM test_record GROUP BY test_reference_code ORDER BY id DESC", null);

            while (c.moveToNext()) { // We select the session Id first, and use it to select the question
                String test_id = c.getString(c.getColumnIndex("id"));
                testId.add(test_id);
                patient_name.add(c.getString(c.getColumnIndex("patient_name")));
                test_reference_code.add(c.getString(c.getColumnIndex("test_reference_code")));
                test_date.add(c.getString(c.getColumnIndex("taken_date")));
                test_time.add(c.getString(c.getColumnIndex("taken_time")));
            }
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

}

// The class for the custom adapter
class HistoryAdapter extends ArrayAdapter {
    private ArrayList patient_name;
    private ArrayList test_reference_code;
    private ArrayList date;
    private ArrayList time;

    public HistoryAdapter(Context context, ArrayList patient_name, ArrayList test_reference_code, ArrayList date, ArrayList time) {
        //Overriding Default Constructor off ArrayAdapter
        super(context, R.layout.history_row_listview, R.id.patientNameTV, test_reference_code);
        this.patient_name = patient_name;
        this.test_reference_code = test_reference_code;
        this.date = date;
        this.time = time;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflating the layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.history_row_listview, parent, false);

        TextView patientNameTV = row.findViewById(R.id.patientNameTV);
        TextView dateTV = row.findViewById(R.id.dateTV);
        TextView timeTV = row.findViewById(R.id.timeTV);

        patientNameTV.setText(patient_name.get(position) + "");
        dateTV.setText(date.get(position) + "");
        timeTV.setText(time.get(position) + "");

        return row;
    }
}
